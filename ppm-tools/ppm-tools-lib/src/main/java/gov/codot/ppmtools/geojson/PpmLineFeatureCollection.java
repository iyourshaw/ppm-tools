package gov.codot.ppmtools.geojson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.wololo.geojson.BaseFeatureCollection;


import java.io.File;
import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PpmLineFeatureCollection extends BaseFeatureCollection<PpmLineFeature> {

    @JsonCreator
    public PpmLineFeatureCollection(@JsonProperty("features") PpmLineFeature[] features) {
        super(features);
    }

    static final ObjectMapper jsonMapper = new ObjectMapper();

    public static PpmLineFeatureCollection readGeojsonFile(File file) throws IOException {
        return jsonMapper.readValue(file, PpmLineFeatureCollection.class);
    }
}
