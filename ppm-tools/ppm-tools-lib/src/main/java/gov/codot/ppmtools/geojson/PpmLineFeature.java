package gov.codot.ppmtools.geojson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import org.wololo.geojson.BaseFeature;
import org.wololo.geojson.LineString;

public class PpmLineFeature extends BaseFeature<Long, LineString, PpmLineProperties> {

    @JsonCreator
    public PpmLineFeature(
           @JsonProperty("id") Long id,
           @JsonProperty("geometry") LineString geometry,
           @JsonProperty("properties") PpmLineProperties properties
    ) {
        super(id, geometry, properties);
    }
}
