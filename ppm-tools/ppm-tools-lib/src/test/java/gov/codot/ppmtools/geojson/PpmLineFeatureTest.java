package gov.codot.ppmtools.geojson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class PpmLineFeatureTest {

    final String json = "{\"type\":\"Feature\",\"id\":0,\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-104.8779369,39.5562657],[-104.8779369,39.5562657],[-104.8780743,39.5563214]]},\"properties\":{\"wayType\":\"motorway\",\"wayId\":\"CO-470-1\"}}";

    final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void canDeserialize() throws JsonProcessingException {
        PpmLineFeature feature = mapper.readValue(json, PpmLineFeature.class);
        assertThat(feature, notNullValue());
        assertThat(feature.getType(), equalTo("Feature"));
        assertThat(feature.getGeometry(), notNullValue());
        assertThat(feature.getGeometry().getType(), equalTo("LineString"));
        assertThat(feature.getGeometry().getCoordinates().length, equalTo(3));
        assertThat(feature.getGeometry().getCoordinates()[0].length, equalTo(2));
        assertThat(feature.getGeometry().getCoordinates()[0][0], equalTo(-104.8779369d));
        assertThat(feature.getGeometry().getCoordinates()[0][1], equalTo(39.5562657d));
        assertThat(feature.getId(), equalTo(0L));
        PpmLineProperties props = feature.getProperties();
        assertThat(props, notNullValue());
        assertThat(props.getWayType(), equalTo("motorway"));
        assertThat(props.getWayId(), equalTo("CO-470-1"));
    }

    @Test
    public void canSerialize() throws JsonProcessingException {
        PpmLineFeature feature = mapper.readValue(json, PpmLineFeature.class);
        String serialized = feature.toString();
        assertThat(serialized, equalTo(json));
    }


}
