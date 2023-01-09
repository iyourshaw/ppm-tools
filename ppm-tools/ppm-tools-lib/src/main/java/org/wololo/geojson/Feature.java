package org.wololo.geojson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"type", "id", "geometry", "properties"})
public class Feature extends BaseFeature<Object, Geometry, Properties> {

    public Feature(
            @JsonProperty("geometry") Geometry geometry,
            @JsonProperty("properties") Properties properties) {
        super(geometry, properties);
    }

    @JsonCreator
    public Feature(
            @JsonProperty("id") Object id,
            @JsonProperty("geometry") Geometry geometry,
            @JsonProperty("properties") Properties properties) {
        super(id, geometry, properties);
    }

 
}
