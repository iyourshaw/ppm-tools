package gov.codot.ppmtools.geojson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.wololo.geojson.BaseFeature;
import org.wololo.geojson.LineString;

@JsonIgnoreProperties(ignoreUnknown = true)
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
