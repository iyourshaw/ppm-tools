package org.wololo.geojson;

import org.junit.jupiter.api.Test;
import org.wololo.geojson.examples.ExampleImmutablePojoFeature;
import org.wololo.geojson.examples.ExampleImmutablePojoProperties;
import org.wololo.geojson.examples.ExamplePojoFeature;
import org.wololo.geojson.examples.ExamplePojoProperties;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

public class BaseFeatureTest {

    @Test
    public void parsePojoFeatureTest() {
        var id = UUID.fromString("f085e9c9-f1e9-41e7-92f9-a37979c7a23d");
        var geometry = new Point(new double[]{1, 1});
        var properties = new ExamplePojoProperties();
        properties.setStr("test");
        properties.setIdx(1);
        var feature = new ExamplePojoFeature(id, geometry, properties);
        final var expected = "{\"type\":\"Feature\",\"id\":\"f085e9c9-f1e9-41e7-92f9-a37979c7a23d\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[1.0,1.0]},\"properties\":{\"str\":\"test\",\"idx\":1}}";
        var json = feature.toString();
        assertThat(expected, equalTo(json));
        var createdFeature = GeoJSONFactory.createFeature(json, ExamplePojoFeature.class);
        assertThat(createdFeature, instanceOf(ExamplePojoFeature.class));
        assertThat(expected, equalTo(createdFeature.toString()));
    }

    @Test
    public void parseImmutablePojoFeatureTest() {
        var id = UUID.fromString("f085e9c9-f1e9-41e7-92f9-a37979c7a23d");
        var geometry = new Point(new double[]{1, 1});
        var properties = new ExampleImmutablePojoProperties("test", 1);
        final var feature = new ExampleImmutablePojoFeature(id, geometry, properties);

        final var expected = "{\"type\":\"Feature\",\"id\":\"f085e9c9-f1e9-41e7-92f9-a37979c7a23d\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[1.0,1.0]},\"properties\":{\"str\":\"test\",\"idx\":1}}";
        var json = feature.toString();
        assertThat(expected, equalTo(json));
        var createdFeature = GeoJSONFactory.createFeature(json, ExampleImmutablePojoFeature.class);
        assertThat(createdFeature, instanceOf(ExampleImmutablePojoFeature.class));
        assertThat(expected, equalTo(createdFeature.toString()));
    }
}
