package gov.codot.ppmtools.mapfile;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PpmEdge_AttributesTest {

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of("way_type=motorway:way_id=CO-470-1", "motorway", "CO-470-1"),
                Arguments.of("way_type=user_defined:way_id=80E", "user_defined", "80E")
        );
    }

    @ParameterizedTest
    @MethodSource("testData")
    public void parseAttributesTest(String str, String expectedType, String expectedId) {
        PpmEdge.Attributes attribs = PpmEdge.Attributes.parseAttributes(str);
        assertThat(attribs, notNullValue());
        assertThat(attribs, hasProperty("wayType", equalTo(expectedType)));
        assertThat(attribs, hasProperty("wayId", equalTo(expectedId)));
    }

    @Test
    public void parseAttributesTest_Null() {
        assertThrows(RuntimeException.class,
                () -> {
                    PpmEdge.Attributes.parseAttributes(null);
                }
        );
    }

    @Test
    public void parseAttributesTest_Invalid() {
        assertThrows(RuntimeException.class,
                () -> {
                    PpmEdge.Attributes.parseAttributes("invalid1=x:invalid2=y");
                }
        );
    }

    @ParameterizedTest
    @MethodSource("testData")
    public void toStringTest(String expectedString, String wayType, String wayId) {
        PpmEdge.Attributes attribs = new PpmEdge.Attributes();
        attribs.setWayType(wayType);
        attribs.setWayId(wayId);
        String str = attribs.toString();
        assertThat(str, equalTo(expectedString));
    }
}
