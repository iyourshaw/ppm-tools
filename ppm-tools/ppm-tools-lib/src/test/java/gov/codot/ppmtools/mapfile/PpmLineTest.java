package gov.codot.ppmtools.mapfile;

import gov.codot.ppmtools.geojson.PpmLineFeature;
import gov.codot.ppmtools.geojson.PpmLineProperties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.CoordinateXY;
import org.wololo.geojson.LineString;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PpmLineTest {

    static long id = 1;
    static String wayType = "motorway";
    static String wayId = "I70";
    static LineString geometry;
    static PpmLineProperties properties;

    @BeforeAll
    public static void setup() {
        properties = new PpmLineProperties();
        properties.setWayType(wayType);
        properties.setWayId(wayId);
        var coordArr = new double[3][2];
        coordArr[0][0] = 0;
        coordArr[0][1] = 0;
        coordArr[1][0] = 1;
        coordArr[1][1] = 1;
        coordArr[2][0] = 2;
        coordArr[2][1] = 2;
        geometry = new LineString(coordArr);
    }

    @Test
    public void fromGeojsonTest() {
        var geojsonFeature = new PpmLineFeature(id, geometry, properties);
        var ppmLine = PpmLine.fromGeojson(geojsonFeature, id);
        assertThat(ppmLine, notNullValue());
        assertThat("PpmLine.wayId", ppmLine.getWayId(), equalTo(wayId));
        assertThat("PpmLine.wayType", ppmLine.getWayType(), equalTo(wayType));
        assertThat("PpmLine.numberOfEdges", ppmLine.getNumberOfEdges(), equalTo(2));
        assertThat("PpmLine.maxId", ppmLine.maxId(), equalTo(3L));
        assertThat(ppmLine, hasSize(2));
        for (int i = 0; i < ppmLine.size(); i++) {
            PpmEdge edge = ppmLine.get(i);
            assertThat(edge.getAttributes(), notNullValue());
            assertThat("edge.wayType", edge.getAttributes().getWayType(), equalTo(wayType));
            assertThat("edge.wayId", edge.getAttributes().getWayId(), equalTo(wayId));
            var geog = edge.getGeography();
            assertThat("edge.geography", geog, notNullValue());
            var expectCoord1 = new CoordinateXY(i, i);
            var expectCoord2 = new CoordinateXY(i + 1, i + 1);
            assertThat("edge.coord1", geog.getCoord1(), equalTo(expectCoord1));
            assertThat("edge.coord2", geog.getCoord2(), equalTo(expectCoord2));
            var expectId1 = id + i;
            var expectId2 = id + i + 1;
            assertThat("edge.id1", geog.getId1(), equalTo(expectId1));
            assertThat("edge.id2", geog.getId2(), equalTo(expectId2));
        }
    }

    @Test
    public void toGeojsonTest() {
        var init = new PpmLineFeature(id, geometry, properties);
        var ppmLine = PpmLine.fromGeojson(init, id);
        var geojson = ppmLine.toGeojson(id);
        assertThat(geojson, notNullValue());
        assertThat("id", geojson.getId(), equalTo(id));
        assertThat("type", geojson.getType(), equalTo("Feature"));
        var props = geojson.getProperties();
        assertThat("properties", props, notNullValue());
        assertThat("properties.wayType", props.getWayType(), equalTo(wayType));
        assertThat("properties.wayId", props.getWayId(), equalTo(wayId));
        var geom = geojson.getGeometry();
        assertThat("geometry", geom, notNullValue());
        assertThat("geometry.type", geom.getType(), equalTo("LineString"));
        var coords = geom.getCoordinates();
        assertThat("geometry.coordinates", coords, notNullValue());
        assertThat("geometry.coordinates.length", coords.length, equalTo(3));
        for (int i = 0; i < coords.length; i++) {
            double expectCoord = i;
            assertThat("x", coords[i][0], equalTo(expectCoord));
            assertThat("y", coords[i][1], equalTo(expectCoord));
        }
    }


}
