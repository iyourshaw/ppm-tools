package org.wololo.jts2geojson;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GeoJSONReaderTest {

    public static GeoJSONReader reader;
    public static GeoJSONWriter writer;
    public static GeometryFactory factory;
    public static int srid;
    public static GeometryFactory factorySrid;
    public static Coordinate[] coordArray;

    @BeforeAll
    public static void setup() {
        reader = new GeoJSONReader();
        writer = new GeoJSONWriter();
        factory = new GeometryFactory();
        srid = 25832;
        factorySrid = new GeometryFactory(new PrecisionModel(), srid);
        coordArray = new Coordinate[] {
                new Coordinate(1, 1),
                new Coordinate(1, 2),
                new Coordinate(2, 2),
                new Coordinate(1, 1)
        };
    }

    @Test
    public void readPointTest() {
        var expected = factory.createPoint(new Coordinate(1, 1));
        var expectedSrid = factorySrid.createPoint(new Coordinate(1, 1));
        var json = "{\"type\":\"Point\",\"coordinates\":[1.0,1.0]}";

        var geometry = reader.read(json);
        assertThat(expected, equalTo(geometry));
        assertThat(0, equalTo(geometry.getSRID()));

        geometry = reader.read(json, factorySrid);
        assertThat(expectedSrid, equalTo(geometry));
        assertThat(srid, equalTo(geometry.getSRID()));
    }

    @Test
    public void readMultiPointTest() {
        var expected = factory.createMultiPointFromCoords(coordArray);
        var expectedSrid = factorySrid.createMultiPointFromCoords(coordArray);
        var json = "{\"type\":\"MultiPoint\",\"coordinates\":[[1.0,1.0],[1.0,2.0],[2.0,2.0],[1.0,1.0]]}";

        var geometry = reader.read(json);
        assertThat(expected, equalTo(geometry));
        assertThat(0, equalTo(geometry.getSRID()));

        geometry = reader.read(json, factorySrid);
        assertThat(expectedSrid, equalTo(geometry));
        assertThat(srid, equalTo(geometry.getSRID()));
    }


}
