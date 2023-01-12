package org.wololo.geojson;

import org.junit.jupiter.api.Test;
import org.wololo.geojson.GeoJSONFactory;
import org.wololo.geojson.Point;
import org.wololo.geojson.examples.ExamplePojoFeature;
import org.wololo.geojson.examples.ExamplePojoFeatureCollection;
import org.wololo.geojson.examples.ExamplePojoProperties;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

public class BaseFeatureCollectionTest {

    @Test
    public void parsePojoFeatureCollectionTest()
    {
        var id1 = UUID.fromString("f085e9c9-f1e9-41e7-92f9-a37979c7a23d");
        var geometry1 = new Point(new double[]{1, 1});
        var properties1 = new ExamplePojoProperties();
        properties1.setStr("test");
        properties1.setIdx(1);
        var feature1 = new ExamplePojoFeature(id1, geometry1, properties1);

        var id2 = UUID.fromString("f085e9c9-f1e9-41e7-0000-a37979c7a23d");
        var geometry2 = new Point(new double[]{2, 2});
        var properties2 = new ExamplePojoProperties();
        properties2.setStr("test2");
        properties2.setIdx(2);
        var feature2 = new ExamplePojoFeature(id2, geometry2, properties2);

        var featureColl = new ExamplePojoFeatureCollection(new ExamplePojoFeature[] { feature1, feature2});

        final var expected = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":\"f085e9c9-f1e9-41e7-92f9-a37979c7a23d\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[1.0,1.0]},\"properties\":{\"str\":\"test\",\"idx\":1}},{\"type\":\"Feature\",\"id\":\"f085e9c9-f1e9-41e7-0000-a37979c7a23d\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[2.0,2.0]},\"properties\":{\"str\":\"test2\",\"idx\":2}}]}";
        var json = featureColl.toString();
        assertThat(expected, equalTo(json));
        var createdFeatureColl = GeoJSONFactory.createFeatureCollection(json, ExamplePojoFeatureCollection.class);
        assertThat(createdFeatureColl, instanceOf(ExamplePojoFeatureCollection.class));
        assertThat(expected, equalTo(createdFeatureColl.toString()));
    }
}
