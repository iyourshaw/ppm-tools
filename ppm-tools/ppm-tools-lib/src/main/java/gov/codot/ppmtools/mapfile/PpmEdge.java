package gov.codot.ppmtools.mapfile;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.geotools.gml.producer.CoordinateFormatter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateXY;

/**
 * POJO represents one edge in the geofence, and one line in the ode-ppm map file.
 * 
 * @author Ivan Yourshaw
 */
@Getter
@Setter
public class PpmEdge {

    @Generated
    String type = "edge";   // Default type is 'edge'

    @Generated
    long id;

    @Generated
    Geography geography = new Geography();

    @Generated
    Attributes attributes = new Attributes();

    public static final String CSV_HEADER = "type,id,geography,attributes";


    public String getCsv() {
        return String.format("%s,%s,%s,%s",type,id,geography,attributes);
    }

    public static PpmEdge parseCsv(String str) {
        if (str == null) {
            throw new RuntimeException("Edge string is null.");
        }
        String[] parts = str.trim().split(",");
        if (parts.length != 4) {
            throw new RuntimeException(String.format("Invalid edge string: %s", str));
        }
        var edge = new PpmEdge();
        edge.setType(parts[0]);
        edge.setId(Long.parseLong(parts[1]));
        edge.setGeography(Geography.parseGeography(parts[2]));
        edge.setAttributes(Attributes.parseAttributes(parts[3]));
        return edge;
    }

    public String getWayId() {
        return attributes != null ? attributes.wayId : null;
    }


    @Getter
    @Setter
    public static class Geography {

        @Generated
        long id1;

        @Generated
        Coordinate coord1;

        @Generated
        long id2;

        @Generated
        Coordinate coord2;

        public static Geography parseGeography(String str) {
            if (str == null) {
                throw new RuntimeException("Geography string is null");
            }
            String[] coords = str.split("[:;]");
            if (coords.length != 6) {
                throw new RuntimeException(String.format("Invalid Geography string: %s", str));
            }

            var geography = new Geography();
            geography.setId1(Long.parseLong(coords[0]));
            double y1 = Double.parseDouble(coords[1]);
            double x1 = Double.parseDouble(coords[2]);
            geography.setCoord1(new CoordinateXY(x1, y1));
            geography.setId2(Long.parseLong(coords[3]));
            double y2 = Double.parseDouble(coords[4]);
            double x2 = Double.parseDouble(coords[5]);
            geography.setCoord2(new CoordinateXY(x2, y2));
            return geography;
        }



        @Override
        public String toString() {
            var formatter = new CoordinateFormatter(7);
            formatter.setForcedDecimal(true);
            return String.format("%s;%s;%s:%s;%s;%s", id1, 
                formatter.format(coord1.getY()), 
                formatter.format(coord1.getX()), 
                id2, 
                formatter.format(coord2.getY()), 
                formatter.format(coord2.getX()));
        }
    }

    @Getter
    @Setter
    public static class Attributes {

        @Generated
        String wayType;

        @Generated
        String wayId;

        @Override
        public String toString() {
            return String.format("way_type=%s:way_id=%s", wayType, wayId);
        }

        public static Attributes parseAttributes(String str) {
            if (str == null) {
                throw new RuntimeException("Attributes string is null");
            }
            String[] attribs = str.trim().split("[:=]");
            if (attribs.length != 4 || !"way_type".equals(attribs[0]) || !"way_id".equals(attribs[2]) ) {
                throw new RuntimeException(String.format("Invalid Attributes string: %s", str));
            }
            var attributes = new Attributes();
            attributes.setWayType(attribs[1]);
            attributes.setWayId(attribs[3]);
            return attributes;
        }
    }
}
