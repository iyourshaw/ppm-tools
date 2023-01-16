package gov.codot.ppmtools.mapfile;

import gov.codot.ppmtools.geojson.PpmLineFeature;
import gov.codot.ppmtools.geojson.PpmLineProperties;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.CoordinateXY;
import org.wololo.geojson.LineString;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * An ordered list of contiguous PpmEdges, representing one carriageway
 * of a road.
 * 
 * The PpmEdges within the line are numbered with connected point UIDs.
 * 
 * @author Ivan Yourshaw
 */
@Getter
@Setter
public class PpmLine
    extends ArrayList<PpmEdge> {

    @Generated
    public String wayType;

    @Generated
    public String wayId;

    public int getNumberOfEdges() {
        return size();
    }

    public long maxId() {
        long maxId = this.stream()
                .map(edge -> edge.getGeography().getId2()).max(Comparator.comparingLong(i -> i))
                .orElseThrow();
        return maxId;
    }

    public PpmLineFeature toGeojson(long id) {
        var props = new PpmLineProperties();
        props.setWayType(wayType);
        props.setWayId(wayId);


        var coords = new ArrayList<double[]>();
        PpmEdge firstEdge = this.get(0);
        double x0 = firstEdge.getGeography().getCoord1().getX();
        double y0 = firstEdge.getGeography().getCoord1().getY();
        coords.add(new double[] { x0, y0 });
        for (PpmEdge edge : this) {
            double x = edge.getGeography().getCoord2().getX();
            double y = edge.getGeography().getCoord2().getY();
            coords.add(new double[] { x, y });
        }
        var coordArr = coords.toArray(new double[0][0]);
        var geometry = new LineString(coordArr);

        return new PpmLineFeature(id, geometry, props);
    }

    public static PpmLine fromGeojson(PpmLineFeature feature, final long startId) {
        final var wayId = feature.getProperties().getWayId();
        final var wayType = feature.getProperties().getWayType();
        var line = new PpmLine();
        line.setWayId(wayId);
        line.setWayType(wayType);

        LineString lineString = feature.getGeometry();
        double[][] coords = lineString.getCoordinates();
        long id = startId;
        for (int i = 0; i < coords.length - 1; i++) {
            double[] coord1 = coords[i];
            double[] coord2 = coords[i + 1];
            var x1 = coord1[0];
            var y1 = coord1[1];
            var x2 = coord2[0];
            var y2 = coord2[1];
            var edge = new PpmEdge();
            var geography = new PpmEdge.Geography();
            geography.setCoord1(new CoordinateXY(x1, y1));
            geography.setCoord2(new CoordinateXY(x2, y2));
            geography.setId1(id);
            edge.setId(id);
            ++id;
            geography.setId2(id);
            var attributes = new PpmEdge.Attributes();
            attributes.setWayId(wayId);
            attributes.setWayType(wayType);
            edge.setAttributes(attributes);
            edge.setGeography(geography);
            line.add(edge);
        }
        return line;
    }

}
