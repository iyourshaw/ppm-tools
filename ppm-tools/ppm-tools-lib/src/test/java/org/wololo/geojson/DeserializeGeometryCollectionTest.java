package org.wololo.geojson;


import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class DeserializeGeometryCollectionTest {

    @Test
    public void deserializeGeometryCollectionTest() {
        var geoJSON = "{\"type\": \"GeometryCollection\", \"geometries\": [{\"type\": \"Point\",  \"coordinates\": [1.1, 2.2] }]}";
        var json = GeoJSONFactory.create(geoJSON);
        assertThat(json, instanceOf(GeometryCollection.class));
        var gc = (GeometryCollection)json;
        assertThat(gc.getGeometries(), arrayWithSize(greaterThan(0)));
        var g = gc.getGeometries()[0];
        assertThat(g, notNullValue());
        assertThat(g, instanceOf(Point.class));
        var point = (Point)g;
        assertThat(point.getCoordinates().length, equalTo(2));
        assertThat(point.getCoordinates()[0], equalTo(1.1D));
        assertThat(point.getCoordinates()[1], equalTo(2.2D));
    }
}
