package gov.codot.ppmtools.geojson;

import lombok.Data;

@Data
public class PpmLineProperties {
    long firstId;
    long lastId;
    String wayType;
    String wayId;
}
