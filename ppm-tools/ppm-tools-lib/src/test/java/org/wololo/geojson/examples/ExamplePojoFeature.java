package org.wololo.geojson.examples;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.wololo.geojson.BaseFeature;
import org.wololo.geojson.Point;

import java.util.UUID;

public class ExamplePojoFeature extends BaseFeature<UUID, Point, ExamplePojoProperties> {

    @JsonCreator
    public ExamplePojoFeature(@JsonProperty("id") UUID id, @JsonProperty("geometry") Point geometry, @JsonProperty("properties") ExamplePojoProperties properties) {
        super(id, geometry, properties);
    }

}
