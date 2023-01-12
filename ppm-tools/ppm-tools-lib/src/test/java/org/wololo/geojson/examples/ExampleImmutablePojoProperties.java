package org.wololo.geojson.examples;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExampleImmutablePojoProperties {
    
    private final String str;
    private final int idx;

    @JsonCreator
    public ExampleImmutablePojoProperties(@JsonProperty("str") String str, @JsonProperty("idx") int idx) {
        this.str = str;
        this.idx = idx;
    }

    public String getStr() {
        return str;
    }
    public int getIdx() {
        return idx;
    }
}
