package gov.codot.ppmtools.geojson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class PpmLineFeatureCollectionTest {

    final String json = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":0,\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-104.8779369,39.5562657],[-104.8779369,39.5562657],[-104.8780743,39.5563214]]},\"properties\":{\"wayType\":\"motorway\",\"wayId\":\"CO-470-1\"}},{\"type\":\"Feature\",\"id\":1,\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-105.2029141,39.7271533],[-105.2029141,39.7271533],[-105.2026237,39.7269373]]},\"properties\":{\"wayType\":\"motorway\",\"wayId\":\"CO-470-2\"}}]}";

    final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void canDeserialize() throws JsonProcessingException {
        PpmLineFeatureCollection fc = mapper.readValue(json, PpmLineFeatureCollection.class);
        assertThat(fc, notNullValue());
        assertThat(fc.getType(), equalTo("FeatureCollection"));
        assertThat(fc.getFeatures(), arrayWithSize(equalTo(2)));
        PpmLineFeature feature = fc.getFeatures()[0];
        assertThat(feature, notNullValue());
        assertThat(feature.getType(), equalTo("Feature"));
    }

    @Test
    public void canSerialize() throws JsonProcessingException {
        PpmLineFeatureCollection featureColl = mapper.readValue(json, PpmLineFeatureCollection.class);
        String serialized = featureColl.toString();
        assertThat(serialized, equalTo(json));
    }

    @Test
    public void canReadFromFile() throws IOException {
        Path dir = Path.of("", "src/test/resources");
        File file = dir.resolve("featureCollection.geojson").toFile();
        var featureColl = PpmLineFeatureCollection.readGeojsonFile(file);
        assertThat(featureColl, notNullValue());
        assertThat(featureColl.getType(), equalTo("FeatureCollection"));
    }
}
