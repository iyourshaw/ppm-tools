package org.wololo.jts2geojson;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GeoJSONWriterXYZTest {

    public static GeoJSONReader reader;
    public static GeoJSONWriter writer;
    public static GeometryFactory factory;
    public static Point point;
    public static LineString lineString;
    public static Polygon polygon;

    @BeforeAll
    public static void setup() {
        reader = new GeoJSONReader();
        writer = new GeoJSONWriter();
        factory = new GeometryFactory();
        point = factory.createPoint(new Coordinate(1, 1, 1));
        lineString = factory.createLineString(new Coordinate[] {
                new Coordinate(1, 1, 1),
                new Coordinate(1, 2, 1),
                new Coordinate(2, 2, 2),
                new Coordinate(1, 1, 1)
        });
        polygon = factory.createPolygon(lineString.getCoordinates());

    }

    @Test
    public void writePointTest() {
        var expected = "{\"type\":\"Point\",\"coordinates\":[1.0,1.0,1.0]}";

        var json = writer.write(point);
        assertThat(expected, equalTo(json.toString()));

        var geometry = reader.read(json);
        assertThat("POINT (1 1)", equalTo(geometry.toString()));
    }

    @Test
    public void writeLineStringTest() {
        var json = writer.write(lineString);
        assertThat("{\"type\":\"LineString\",\"coordinates\":[[1.0,1.0,1.0],[1.0,2.0,1.0],[2.0,2.0,2.0],[1.0,1.0,1.0]]}", equalTo(json.toString()));
    }

    @Test
    public void writePolygonTest() {
        var json = writer.write(polygon);
        assertThat("{\"type\":\"Polygon\",\"coordinates\":[[[1.0,1.0,1.0],[1.0,2.0,1.0],[2.0,2.0,2.0],[1.0,1.0,1.0]]]}", equalTo(json.toString()));
    }

    @Test
    public void writeMultiPointTest() {
        var multiPoint = factory.createMultiPointFromCoords(lineString.getCoordinates());
        var json = writer.write(multiPoint);
        assertThat("{\"type\":\"MultiPoint\",\"coordinates\":[[1.0,1.0,1.0],[1.0,2.0,1.0],[2.0,2.0,2.0],[1.0,1.0,1.0]]}", equalTo(json.toString()));
    }

    @Test
    public void writeMultiLineStringTest() {
        var multiLineString = factory.createMultiLineString(new LineString[]{lineString, lineString});
        var json = writer.write(multiLineString);
        assertThat("{\"type\":\"MultiLineString\",\"coordinates\":[[[1.0,1.0,1.0],[1.0,2.0,1.0],[2.0,2.0,2.0],[1.0,1.0,1.0]],[[1.0,1.0,1.0],[1.0,2.0,1.0],[2.0,2.0,2.0],[1.0,1.0,1.0]]]}", equalTo(json.toString()));

    }

    @Test
    public void writeMultiPolygonTest() {
        var multiPolygon = factory.createMultiPolygon(new Polygon[]{polygon, polygon});
        var json = writer.write(multiPolygon);
        assertThat("{\"type\":\"MultiPolygon\",\"coordinates\":[[[[1.0,1.0,1.0],[1.0,2.0,1.0],[2.0,2.0,2.0],[1.0,1.0,1.0]]],[[[1.0,1.0,1.0],[1.0,2.0,1.0],[2.0,2.0,2.0],[1.0,1.0,1.0]]]]}", equalTo(json.toString()));
    }

    @Test
    public void writeFeatureCollectionTest() {
        var json = writer.write(point);
        var feature1 = new Feature(json, null);
        var feature2 = new Feature(json, null);
        var featureCollection = new FeatureCollection(new Feature[]{feature1, feature2});
        assertThat("{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[1.0,1.0,1.0]},\"properties\":null},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[1.0,1.0,1.0]},\"properties\":null}]}", equalTo(featureCollection.toString()));
    }
}
