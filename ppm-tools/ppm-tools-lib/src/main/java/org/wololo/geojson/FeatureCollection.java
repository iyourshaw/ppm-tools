package org.wololo.geojson;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FeatureCollection extends BaseFeatureCollection<Feature> {

    public FeatureCollection(@JsonProperty("features") Feature[] features) {
        super(features);
    }
}
