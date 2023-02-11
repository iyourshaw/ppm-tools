package gov.codot.ppmtools.geojson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Generated;

@Data
@Generated
@JsonIgnoreProperties(ignoreUnknown = true)
public class PpmLineProperties {
    String wayType;
    String wayId;
}
