package org.wololo.jts2geojson;



/**
 * Static utility methods to convert org.wololo derived Geometries to JTS Geometries
 */
public class JTSConverter {
    
    public static org.locationtech.jts.geom.MultiLineString convertToJTS(org.wololo.geojson.MultiLineString multiLineString) {
        GeoJSONReader reader = new GeoJSONReader();
        return (org.locationtech.jts.geom.MultiLineString)reader.convert(multiLineString, GeoJSONReader.FACTORY);
    }

    public static org.locationtech.jts.geom.LineString convertToJTS(org.wololo.geojson.LineString lineString) {
        GeoJSONReader reader = new GeoJSONReader();
        return (org.locationtech.jts.geom.LineString)reader.convert(lineString, GeoJSONReader.FACTORY);
    }

    public static org.locationtech.jts.geom.Point convertToJTS(org.wololo.geojson.Point point) {
        GeoJSONReader reader = new GeoJSONReader();
        return (org.locationtech.jts.geom.Point)reader.convert(point, GeoJSONReader.FACTORY);
    }

    public static org.locationtech.jts.geom.Polygon convertoToJTS(org.wololo.geojson.Polygon polygon) {
        GeoJSONReader reader = new GeoJSONReader();
        return (org.locationtech.jts.geom.Polygon)reader.convert(polygon, GeoJSONReader.FACTORY);
    }

    public static org.locationtech.jts.geom.MultiPolygon convertoToJTS(org.wololo.geojson.MultiPolygon multiPolygon) {
        GeoJSONReader reader = new GeoJSONReader();
        return (org.locationtech.jts.geom.MultiPolygon)reader.convert(multiPolygon, GeoJSONReader.FACTORY);
    }

    public static org.locationtech.jts.geom.MultiPoint convertoToJTS(org.wololo.geojson.MultiPoint polygon) {
        GeoJSONReader reader = new GeoJSONReader();
        return (org.locationtech.jts.geom.MultiPoint)reader.convert(polygon, GeoJSONReader.FACTORY);
    }

    public static org.wololo.geojson.MultiLineString convertFromJTS(org.locationtech.jts.geom.MultiLineString multiLineString) {
        GeoJSONWriter writer = new GeoJSONWriter();
        return (org.wololo.geojson.MultiLineString)writer.convert(multiLineString);
    }

    public static org.wololo.geojson.LineString convertFromJTS(org.locationtech.jts.geom.LineString lineString) {
        GeoJSONWriter writer = new GeoJSONWriter();
        return (org.wololo.geojson.LineString)writer.convert(lineString);
    }

    public static org.wololo.geojson.Point convertFromJTS(org.locationtech.jts.geom.Point point) {
        GeoJSONWriter writer = new GeoJSONWriter();
        return (org.wololo.geojson.Point)writer.convert(point);
    }

    public static org.wololo.geojson.MultiPoint convertFromJTS(org.locationtech.jts.geom.MultiPoint multiPoint) {
        GeoJSONWriter writer = new GeoJSONWriter();
        return (org.wololo.geojson.MultiPoint)writer.convert(multiPoint);
    }

    public static org.wololo.geojson.Polygon convertFromJTS(org.locationtech.jts.geom.Polygon polygon) {
        GeoJSONWriter writer = new GeoJSONWriter();
        return (org.wololo.geojson.Polygon)writer.convert(polygon);
    }

    public static org.wololo.geojson.MultiPolygon convertFromJTS(org.locationtech.jts.geom.MultiPolygon multiPolygon) {
        GeoJSONWriter writer = new GeoJSONWriter();
        return (org.wololo.geojson.MultiPolygon)writer.convert(multiPolygon);
    }
}
