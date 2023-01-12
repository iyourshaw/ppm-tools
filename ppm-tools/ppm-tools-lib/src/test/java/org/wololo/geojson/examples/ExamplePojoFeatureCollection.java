package org.wololo.geojson.examples;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.wololo.geojson.BaseFeatureCollection;

public class ExamplePojoFeatureCollection extends BaseFeatureCollection<ExamplePojoFeature> {

    @JsonCreator
    public ExamplePojoFeatureCollection(@JsonProperty("features") ExamplePojoFeature[] features) {
        super(features);
    }
}
