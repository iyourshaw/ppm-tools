package gov.codot.ppmtools.mapfile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.codot.ppmtools.geojson.PpmLineFeatureCollection;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PpmLineCollectionTest {

    static final String geojson = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":0,\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-104.8779369,39.5562657],[-104.8779369,39.5562657],[-104.8780743,39.5563214]]},\"properties\":{\"wayType\":\"motorway\",\"wayId\":\"CO-470-1\"}},{\"type\":\"Feature\",\"id\":1,\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-105.2029141,39.7271533],[-105.2029141,39.7271533],[-105.2026237,39.7269373]]},\"properties\":{\"wayType\":\"motorway\",\"wayId\":\"CO-470-2\"}}]}";

    static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void fromGeojson() throws JsonProcessingException {
        var featureCollection = mapper.readValue(geojson, PpmLineFeatureCollection.class);
        var lineColl = PpmLineCollection.fromGeojson(featureCollection);
        assertThat(lineColl, notNullValue());
        assertThat(lineColl, hasSize(2));
    }

    @Test
    public void toGeojson() throws JsonProcessingException {
        var featureCollection = mapper.readValue(geojson, PpmLineFeatureCollection.class);
        var lineColl = PpmLineCollection.fromGeojson(featureCollection);
        PpmLineFeatureCollection features = lineColl.toGeojson();
        assertThat(features, notNullValue());
        assertThat(features.getType(), equalTo("FeatureCollection"));
        assertThat(features.getFeatures(), arrayWithSize(2));
    }

    @Test
    public void readMapFileTest() throws FileNotFoundException {
        Path dir = Path.of("", "src/test/resources");
        File file = dir.resolve("test.edges").toFile();
        PpmLineCollection lineColl = PpmLineCollection.readMapFile(file);
        assertThat(lineColl, notNullValue());
        assertThat(lineColl, hasSize(2));
        PpmLine line1 = lineColl.get(0);
        PpmLine line2 = lineColl.get(1);

        final String wayType = "user_defined";
        assertThat(line1.getWayType(), equalTo(wayType));
        assertThat(line2.getWayType(), equalTo(wayType));
        assertThat(line1.getWayId(), equalTo("80E"));
        assertThat(line2.getWayId(), equalTo("80W"));
        assertThat(line1.getNumberOfEdges(), equalTo(2));
        assertThat(line2.getNumberOfEdges(), equalTo(2));
    }

    @Test
    public void writeMapFileTest() throws IOException {
        var featureCollection = mapper.readValue(geojson, PpmLineFeatureCollection.class);
        var lineColl = PpmLineCollection.fromGeojson(featureCollection);
        File tempFile = File.createTempFile("test", ".edges");
        tempFile.deleteOnExit();
        lineColl.writeMapfile(tempFile);
        assertTrue(tempFile.exists(), "file exists");
        String writtenFile = FileUtils.readFileToString(tempFile, StandardCharsets.UTF_8);
        assertThat(writtenFile, containsString("edge"));
    }


}
