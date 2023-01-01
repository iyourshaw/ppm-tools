package gov.codot.ppmtools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Sets;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.dissolve.LineDissolver;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.util.LineStringExtracter;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeatureType;

import gov.codot.ppmtools.mapfile.PpmEdge;
import gov.codot.ppmtools.mapfile.PpmLine;
import gov.codot.ppmtools.mapfile.PpmLineCollection;

/**
 * Methods for extracting Open Street Map line features contained in a 
 * Geopackage database and processing them into ODE PPM "Map File" format.
 * 
 * @author Ivan Yourshaw
 * 
 */
public class PpmProcessor {

    // Constants
    public static final String HIGHWAY = "highway";
    public static final String MOTORWAY = "motorway";
    public static final String NAME = "name";
    public static final String OTHER_TAGS = "other_tags";
    public static final String REF = "ref";
    public static final String GEOM = "geom";
    public static final String HIGHWAY_NAME = "highway_name";
    public static final String HIGHWAY_ID = "highway_id";
    public static final String WAY_TYPE = "way_type";

    static final SimpleFeatureType HIGHWAY_MULTILINE_TYPE;
    static final SimpleFeatureType HIGHWAY_LINE_TYPE;
    
    static {
        HIGHWAY_MULTILINE_TYPE = createMultiLineStringFeatureType();
        HIGHWAY_LINE_TYPE = createLineStringFeatureType();
    }

 
    List<Feature> filteredFeatures;
    ListMultimap<String, Feature> highwayFeatureMap;
    Map<String, Feature> dissolvedHighwayFeatureMap;
    PpmLineCollection lineCollection;

    public ListMultimap<String, Feature> getHighwayFeatureMap() { return highwayFeatureMap; }
    public Map<String, Feature> getDissolvedHighwayFeatureMap() { return dissolvedHighwayFeatureMap; }
    public PpmLineCollection getEdges() { return lineCollection; }

    public PpmProcessor(SimpleFeatureCollection featureColl) {
        filteredFeatures = new ArrayList<Feature>();
        try (SimpleFeatureIterator iterator = featureColl.features()){
            while (iterator.hasNext()) {
                filteredFeatures.add(iterator.next());
            }
        }
    }

    public void filterByWayTypes(String[] wayTypes) {
        var wayTypeSet = Sets.newHashSet(wayTypes);
        var wayTypeFilteredFeatures = new ArrayList<Feature>();
        for (var feature : filteredFeatures) {
            var wayType = feature.getProperty(HIGHWAY).getValue().toString();
            if (wayTypeSet.contains(wayType)) {
                wayTypeFilteredFeatures.add(feature);
            }
        }
        filteredFeatures = wayTypeFilteredFeatures;
    }

    public void filterByHighwayNames(String[] highwayNames) {
        var nameSet = Sets.newHashSet(highwayNames);
        var nameFilteredFeatures = new ArrayList<Feature>();
        for (var feature : filteredFeatures) {
            var highwayName = getHighwayName(feature);
            if (nameSet.contains(highwayName)) {
                nameFilteredFeatures.add(feature);
            }
        }
        filteredFeatures = nameFilteredFeatures;
    }

    public void sortByHighwayName() {
        var highwayMap
            = ArrayListMultimap.<String, Feature>create();
       
        for (var feature : filteredFeatures) {
            var highwayName = getHighwayName(feature);
            if (highwayName != null) {
                highwayMap.put(highwayName, feature);
            }
        }

        highwayFeatureMap = highwayMap;
     }

    public void dissolveHighways() {
        var dissolvedMap = new TreeMap<String, Feature>();
        for (var highwayName : highwayFeatureMap.keySet()) {
            var features = highwayFeatureMap.get(highwayName);
            
            // Get the way types for all the features for the highway
            Set<String> wayTypes = 
                features.stream().map(
                    feat -> feat.getProperty(HIGHWAY).getValue().toString()).collect(Collectors.toSet());

            // If not all parts of the road with the same name have the same way type
            // it is an error.
            if (wayTypes.size() > 1) {
                throw new RuntimeException(
                    String.format("Road features with the same name, %s, have different way types: %s",
                                    highwayName, wayTypes));
            }

            String wayType = wayTypes.iterator().next();

            var dissolver = new LineDissolver();
            for (var feature : features) {
                var line = (LineString)feature.getDefaultGeometryProperty().getValue();
                dissolver.add(line);
            }
            Geometry dissolved = dissolver.getResult();
            if (dissolved instanceof MultiLineString) {
                List<LineString> lineStrings = splitMultiLine((MultiLineString)dissolved);
                for (int i = 0; i < lineStrings.size(); ++i) {
                    var highwayId = String.format("%s-%s", highwayName.replace(" ", "-"), i+1);
                    var lineFeature = buildLineFeature(lineStrings.get(i), highwayName, highwayId, wayType);
                    dissolvedMap.put(highwayId, lineFeature);
                }
            } else if (dissolved instanceof LineString) {
                var highwayId = highwayName.replace(" ", "-");
                var lineFeature = buildLineFeature((LineString)dissolved, highwayName, highwayId, wayType);
                dissolvedMap.put(highwayId, lineFeature);
            }
            
        }
        dissolvedHighwayFeatureMap = dissolvedMap;
    }

    public void extractEdges() {
        var lineColl = new PpmLineCollection();
        long pointId = 0;
        for (var highwayId : dissolvedHighwayFeatureMap.keySet()) {
            var line = new PpmLine();
            var lineFeature = dissolvedHighwayFeatureMap.get(highwayId);
            var wayType = lineFeature.getProperty(WAY_TYPE).getValue().toString();
            var geom = (LineString)lineFeature.getDefaultGeometryProperty().getValue();
            var coords = geom.getCoordinateSequence();
            for (int i = 0; i < coords.size() - 1; i++) {
                // Get two coordinates for each edge
                Coordinate coord1 = coords.getCoordinate(i);
                Coordinate coord2 = coords.getCoordinate(i + 1);
                var edge = new PpmEdge();
                edge.setId(pointId);
                edge.getAttributes().setWayId(highwayId);
                edge.getAttributes().setWayType(wayType);
                edge.getGeography().setCoord1(coord1);
                edge.getGeography().setCoord2(coord2);
                edge.getGeography().setId1(pointId);

                pointId++;
                edge.getGeography().setId2(pointId);

                line.add(edge);
            }
            // Increment id here so that separate lines do not
            // have points with the same uid.
            System.out.printf("Way ID: %s, Number of edges: %s%n", highwayId, line.getNumberOfEdges());
            lineColl.add(line);
            
            // Increment point ID, so that different lines don't share point uids
            pointId++;
        }
        lineCollection = lineColl;
    }

    public void writeMapfile(File outfile) throws IOException  {
        lineCollection.writeMapfile(outfile);
    }

    private String getHighwayName(Feature feature) {
        var prop = feature.getProperty(HIGHWAY);
        var highway = prop.getValue();
        if (highway == null) {
            return null;
        } else if (MOTORWAY.equals(highway)) {
            // If it is a motorway, the name is in other_tags/ref
            var otherTags = feature.getProperty(OTHER_TAGS);
            return getHighwayNameFromRef(otherTags);
        } else {
            // Not a motorway, the common name is likely in the "name" property
            var name = feature.getProperty(NAME);
            return name != null ? name.getValue().toString() : null;
        }
    } 

    private String getHighwayNameFromRef(Property otherTagsProp) {
        if (otherTagsProp != null) {
            var otherTags = otherTagsProp.getValue().toString();
            String[] splitTags = otherTags.replace("\"", "").replace("=>", "$").split(",");
            for (var tag : splitTags) {
                String[] tagParts = tag.split("\\$");
                var tagKey = tagParts[0];
                if (REF.equals(tagKey)) {
                    var tagValue = tagParts[1];
                    String[] refParts = tagValue.split(";");
                    //System.out.printf("Ref: %s%n", refParts[0]);
                    return refParts[0];
                }
            }
        }
        return null;
    }

    private static SimpleFeatureType createMultiLineStringFeatureType() {
        var builder = new SimpleFeatureTypeBuilder();
        builder.setName("HighwayMultiLine");
        builder.setCRS(DefaultGeographicCRS.WGS84);
        builder.add(GEOM, MultiLineString.class);
        builder.add(HIGHWAY_NAME, String.class);
        builder.add(WAY_TYPE, String.class);
        return builder.buildFeatureType();
    }

    private static SimpleFeatureType createLineStringFeatureType() {
        var builder = new SimpleFeatureTypeBuilder();
        builder.setName("HighwayLine");
        builder.setCRS(DefaultGeographicCRS.WGS84);
        builder.add(GEOM, LineString.class);
        builder.add(HIGHWAY_NAME, String.class);
        builder.add(HIGHWAY_ID, String.class);
        builder.add(WAY_TYPE, String.class);
        return builder.buildFeatureType();
    }

    private static Feature buildLineFeature(LineString line, 
            String highwayName, String highwayId, String wayType) {
        var featBuilder = new SimpleFeatureBuilder(HIGHWAY_LINE_TYPE);
        featBuilder.set(GEOM, line);
        featBuilder.set(HIGHWAY_NAME, highwayName);
        featBuilder.set(HIGHWAY_ID, highwayId);
        featBuilder.set(WAY_TYPE, wayType);
        Feature lineFeature = featBuilder.buildFeature(null);
        return lineFeature;
    }

    private static List<LineString> splitMultiLine(MultiLineString multiline) {
         List<?> lines = LineStringExtracter.getLines(multiline);
         var lsList = new ArrayList<LineString>();
         lines.forEach(x -> lsList.add((LineString)x));
         return lsList;
    }
}
