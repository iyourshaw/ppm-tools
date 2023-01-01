package gov.codot.ppmtools.mapfile;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PpmEdgeTest {

    @ParameterizedTest
    @MethodSource("testData")
    public void parseCsvTest(String csv, long expectedId, String expectedWayId, String expectedWayType) {
        PpmEdge edge = PpmEdge.parseCsv(csv);
        assertThat(edge, hasProperty("id", equalTo(expectedId)));
        assertThat(edge, hasProperty("type", equalTo("edge")));
        assertThat(edge, hasProperty("wayId", equalTo(expectedWayId)));
        assertThat(edge, hasProperty("attributes", notNullValue()));
        assertThat(edge.getAttributes(), hasProperty("wayType", equalTo(expectedWayType)));
        assertThat(edge, hasProperty("geography", notNullValue()));
    }



    public static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of("edge,7119,7119;39.148021;-104.8594924:7120;39.1446368;-104.8607628,way_type=motorway:way_id=I-25-5",
                        7119L, "I-25-5", "motorway"),
                Arguments.of("edge,43,43;41.2466311;-111.0207324:44;41.246664;-111.0204262,way_type=user_defined:way_id=80E",
                        43, "80E", "user_defined")
        );
    }
}
