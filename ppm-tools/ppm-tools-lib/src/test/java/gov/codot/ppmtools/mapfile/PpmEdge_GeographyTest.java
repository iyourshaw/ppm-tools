package gov.codot.ppmtools.mapfile;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.locationtech.jts.geom.Coordinate;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@PpmEdge.Geography}
 */
public class PpmEdge_GeographyTest {
    
    @ParameterizedTest
    @CsvSource({
        "0,1,-104.8779369,39.5562657,-104.8780743,39.5563214",
        "23,24,-104.8814797,39.5583534,-104.8815312,39.5583986"
    })
    public void toStringTest(long id1, long id2, String lon1, String lat1, String lon2, String lat2) {
        PpmEdge.Geography geography = new PpmEdge.Geography();

        Coordinate coord1 = new Coordinate(Double.parseDouble(lon1), Double.parseDouble(lat1));
        Coordinate coord2 = new Coordinate(Double.parseDouble(lon2), Double.parseDouble(lat2));

        geography.setId1(id1);
        geography.setId2(id2);
        geography.setCoord1(coord1);
        geography.setCoord2(coord2);
        String result = geography.toString();

        assertThat(result, startsWith(Long.toString(id1)));
        
        assertThat(result, endsWith(Double.toString(coord2.getX())));

        assertThat(result, stringContainsInOrder(
            Long.toString(id1) + ";",
            lat1,
            lon1,
            Long.toString(id2) + ";",
            lat2,
            lon2));
        
    }

   @Test
    public void parseGeographyTest() {
        final String str = "1167;39.7239714;-105.198741:1168;39.7241547;-105.1988367";
        PpmEdge.Geography geography = PpmEdge.Geography.parseGeography(str);
        assertThat(geography, notNullValue());
        assertThat(geography.getId1(), equalTo(1167L));
        assertThat(geography.getId2(), equalTo(1168L));
        var coord1 = geography.getCoord1();
        var coord2 = geography.getCoord2();
        assertThat(coord1, notNullValue());
        assertThat(coord2, notNullValue());
        final double tolerance = 1e-7;
        assertThat(coord1, hasProperty("x", closeTo(-105.198741, tolerance)));
        assertThat(coord1, hasProperty("y", closeTo(39.7239714, tolerance)));
        assertThat(coord2, hasProperty("x", closeTo(-105.1988367, tolerance)));
        assertThat(coord2, hasProperty("y", closeTo(39.7241547, tolerance)));
    }

    @Test
    public void parseGeographyTest_Null() {
        assertThrows(RuntimeException.class,
                () -> {
                    PpmEdge.Geography.parseGeography(null);
                }
        );
    }

    @Test
    public void parseGeographyTest_Invalid() {
        assertThrows(RuntimeException.class,
                () -> {
                    PpmEdge.Geography.parseGeography("0;0;0:1");
                }
        );
    }
}
