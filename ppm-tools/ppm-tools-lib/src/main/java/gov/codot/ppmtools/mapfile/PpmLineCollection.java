package gov.codot.ppmtools.mapfile;

import gov.codot.ppmtools.geojson.PpmLineFeature;
import gov.codot.ppmtools.geojson.PpmLineFeatureCollection;

import java.io.*;
import java.util.*;

/**
 * A collection of PpmLine objects.
 * 
 * The line features represent carriageways of one or more roads.
 */
public class PpmLineCollection
    extends ArrayList<PpmLine> {

    public PpmLine findLineByWayId(final String wayId) {
        for (var ppmLine : this) {
            if (Objects.equals(ppmLine.getWayId(), wayId)) {
                return ppmLine;
            }
        }
        return null;
    }

    public void writeMapfile(File outfile) throws IOException {
        // Delete the file if it exists already
        if (outfile.exists()) {
            outfile.delete();
        }
        try (var printWriter = new PrintWriter( new FileWriter(outfile))) {
            printWriter.println(PpmEdge.CSV_HEADER);
            for (var line : this){
                for (var edge : line) {
                    printWriter.println(edge.getCsv());
                }
            }
        }
    }

    public PpmLineFeatureCollection toGeojson() {
        var features = new ArrayList<PpmLineFeature>();
        long id = 0L;
        for (PpmLine line : this) {
            var feature = line.toGeojson(id);
            ++id;
            features.add(feature);
        }
        return new PpmLineFeatureCollection(features.toArray(new PpmLineFeature[0]));
    }

    public static PpmLineCollection fromGeojson(PpmLineFeatureCollection featureCollection) {
        long id = 0;
        var lines = new PpmLineCollection();

        for (PpmLineFeature feature : featureCollection.getFeatures()) {
            PpmLine line = PpmLine.fromGeojson(feature, id);
            lines.add(line);
            id = line.maxId() + 1;
        }
        return lines;
    }

    public static PpmLineCollection readMapFile(File file) throws FileNotFoundException {
        var edges = new ArrayList<PpmEdge>();
        try (var scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!PpmEdge.CSV_HEADER.equals(line)) {
                    // Ignore header
                    PpmEdge edge = PpmEdge.parseCsv(line);
                    edges.add(edge);
                }
            }
        }

        // Sort by id
        edges.sort(Comparator.comparingLong(PpmEdge::getId));

        // Split edges into separate line features by Way ID
        PpmLineCollection lineCollection = new PpmLineCollection();
        for (var edge : edges) {
            final String wayId = edge.getWayId();
            PpmLine ppmLine = lineCollection.findLineByWayId(wayId);
            if (ppmLine == null) {
                // Way Id not found, start a new line
                ppmLine = new PpmLine();
                ppmLine.setWayId(wayId);
                ppmLine.setWayType(edge.getAttributes().getWayType());
                lineCollection.add(ppmLine);
            }
            ppmLine.add(edge);
        }
        return lineCollection;
    }


}
