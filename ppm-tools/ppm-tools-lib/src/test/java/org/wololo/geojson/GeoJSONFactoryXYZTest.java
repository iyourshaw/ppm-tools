package org.wololo.geojson;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GeoJSONFactoryXYZTest {

    public static Point geometry;
    public static Properties properties;

    @BeforeAll
    public static void setup() {
        geometry = new Point(new double[]{1, 1, 1});
        var map = new HashMap<String, Object>();
        map.put("test", 1);
        properties = new Properties(map);
    }

    @Test
    public void createFeatureTest() {
        var expected = "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[1.0,1.0,1.0]},\"properties\":{\"test\":1}}";
        var feature = new Feature(geometry, properties);
        var json = feature.toString();
        assertThat(expected, equalTo(json));

        var geoJSON = GeoJSONFactory.create(json);
        assertThat(expected, equalTo(geoJSON.toString()));
    }

    @Test
    public void createFeatureWithIdTest() {
        var feature = new Feature(1, geometry, properties);
        var expected = "{\"type\":\"Feature\",\"id\":1,\"geometry\":{\"type\":\"Point\",\"coordinates\":[1.0,1.0,1.0]},\"properties\":{\"test\":1}}";

        var json = feature.toString();
        assertThat(expected, equalTo(json));
        var geoJSON = GeoJSONFactory.create(json);
        assertThat(expected, equalTo(geoJSON.toString()));
    }

    @Test
    public void createFeatureWithoutGeometryTest() {
        var feature = new Feature(null, properties);
        var expected = "{\"type\":\"Feature\",\"geometry\":null,\"properties\":{\"test\":1}}";
        var json = feature.toString();
        assertThat(expected, equalTo(json));
        var geoJSON = GeoJSONFactory.create(json);
        assertThat(expected, equalTo(geoJSON.toString()));
    }

    @Test
    public void createFeatureCollectionTest() {
        var expected = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[1.0,1.0,1.0]},\"properties\":{\"test\":1}},{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[1.0,1.0,1.0]},\"properties\":{\"test\":1}}]}";
        var feature = new Feature(geometry, properties);
        var fc = new FeatureCollection(new Feature[]{feature, feature});
        assertThat(expected, equalTo(fc.toString()));
    }

    @Test
    public void bboxPropertyTest() {
        var geoJSON = "{\"type\": \"FeatureCollection\", \"features\": [{\"type\": \"Feature\", \"id\": 1, \"geometry\": {\"type\": \"Point\", \"coordinates\": [-8.311419016226296, 53.894485921596285], \"bbox\": [-8.311419016226296, 53.894485921596285, -8.311419016226296, 53.894485921596285] }, \"properties\": {\"FID\": 335, \"PLAN_REF\": \"151\", \"APP_TYPE\": \"RETENTION\", \"LOCATION\": \"Knockglass, Ballinameen, Co. Roscommon.\", \"REC_DATE\": \"05/01/2015\", \"DESCRIPT\": \"Of entrance to existing forest plantation for extraction of timber at various times at \", \"APPSTATUS\": \"Application Finalised\", \"DEC_DATE\": \"20/02/2015\", \"DECISION\": \"Granted (Conditional)\", \"APPE_DEC\": \"n/a\", \"APPE_DAT\": \"n/a\", \"MOREINFO\": \"http://www.eplanning.ie/roscommoneplan/FileRefDetails.aspx?file_number=151\", \"WGS_LONG\": \"-8.31141\", \"WGS_LAT\": \"53.89448\"} }] }";
        var json = GeoJSONFactory.create(geoJSON);
        assertThat(json, instanceOf(FeatureCollection.class));
        var fc = (FeatureCollection)json;
        assertThat(fc.getFeatures(), arrayWithSize(greaterThan(0)));
        var f = fc.getFeatures()[0];
        assertThat(f.getGeometry(), notNullValue());
        assertThat(f.getGeometry(), instanceOf(Point.class));
        var point = (Point)f.getGeometry();
        assertThat(point.getBbox(), notNullValue());
        assertThat(point.getBbox().length, equalTo(4));
        assertThat(point.getBbox()[0], equalTo(-8.311419016226296d));
        assertThat(point.getBbox()[1], equalTo(53.894485921596285d));
        assertThat(point.getBbox()[2], equalTo(-8.311419016226296d));
        assertThat(point.getBbox()[3], equalTo(53.894485921596285d));
    }

    @Test
    public void multiPointBboxPropertyTest() {
        var geoJSON = "{\"type\": \"FeatureCollection\", \"features\": [{\"type\": \"Feature\", \"id\": 1, \"geometry\": {\"type\": \"MultiPoint\", \"coordinates\": [[-8.311419016226296, 53.894485921596285]], \"bbox\": [-8.311419016226296, 53.894485921596285, -8.311419016226296, 53.894485921596285] }, \"properties\": {\"FID\": 335, \"PLAN_REF\": \"151\", \"APP_TYPE\": \"RETENTION\", \"LOCATION\": \"Knockglass, Ballinameen, Co. Roscommon.\", \"REC_DATE\": \"05/01/2015\", \"DESCRIPT\": \"Of entrance to existing forest plantation for extraction of timber at various times at \", \"APPSTATUS\": \"Application Finalised\", \"DEC_DATE\": \"20/02/2015\", \"DECISION\": \"Granted (Conditional)\", \"APPE_DEC\": \"n/a\", \"APPE_DAT\": \"n/a\", \"MOREINFO\": \"http://www.eplanning.ie/roscommoneplan/FileRefDetails.aspx?file_number=151\", \"WGS_LONG\": \"-8.31141\", \"WGS_LAT\": \"53.89448\"} }] }";
        var json = GeoJSONFactory.create(geoJSON);
        assertThat(json, instanceOf(FeatureCollection.class));
        var fc = (FeatureCollection)json;
        assertThat(fc.getFeatures(), arrayWithSize(greaterThan(0)));
        var f = fc.getFeatures()[0];
        assertThat(f.getGeometry(), notNullValue());
        assertThat(f.getGeometry(), instanceOf(MultiPoint.class));
        var point = (MultiPoint)f.getGeometry();
        assertThat(point.getBbox(), notNullValue());
        assertThat(point.getBbox().length, equalTo(4));
        assertThat(point.getBbox()[0], equalTo(-8.311419016226296d));
        assertThat(point.getBbox()[1], equalTo(53.894485921596285d));
        assertThat(point.getBbox()[2], equalTo(-8.311419016226296d));
        assertThat(point.getBbox()[3], equalTo(53.894485921596285d));
    }

    @Test
    public void lineStringBboxPropertyTest() {
        var geoJSON = "{\"type\": \"FeatureCollection\", \"features\": [{\"type\": \"Feature\", \"id\": 1, \"geometry\": {\"type\": \"LineString\", \"coordinates\": [[0.0,0.0],[1.0,1.0]], \"bbox\": [0.0,0.0,1.0,1.0] }, \"properties\": {\"FID\": 335, \"PLAN_REF\": \"151\", \"APP_TYPE\": \"RETENTION\", \"LOCATION\": \"Knockglass, Ballinameen, Co. Roscommon.\", \"REC_DATE\": \"05/01/2015\", \"DESCRIPT\": \"Of entrance to existing forest plantation for extraction of timber at various times at \", \"APPSTATUS\": \"Application Finalised\", \"DEC_DATE\": \"20/02/2015\", \"DECISION\": \"Granted (Conditional)\", \"APPE_DEC\": \"n/a\", \"APPE_DAT\": \"n/a\", \"MOREINFO\": \"http://www.eplanning.ie/roscommoneplan/FileRefDetails.aspx?file_number=151\", \"WGS_LONG\": \"-8.31141\", \"WGS_LAT\": \"53.89448\"} }] }";
        var json = GeoJSONFactory.create(geoJSON);
        assertThat(json, instanceOf(FeatureCollection.class));
        var fc = (FeatureCollection)json;
        assertThat(fc.getFeatures(), arrayWithSize(greaterThan(0)));
        var f = fc.getFeatures()[0];
        assertThat(f.getGeometry(), notNullValue());
        assertThat(f.getGeometry(), instanceOf(LineString.class));
        var point = (LineString)f.getGeometry();
        assertThat(point.getBbox(), notNullValue());
        assertThat(point.getBbox().length, equalTo(4));
        assertThat(point.getBbox()[0], equalTo(0.0));
        assertThat(point.getBbox()[1], equalTo(0.0));
        assertThat(point.getBbox()[2], equalTo(1.0));
        assertThat(point.getBbox()[3], equalTo(1.0));

    }

    @Test
    public void multiLineStringBboxPropertyTest() {
        var geoJSON = "{\"type\": \"FeatureCollection\", \"features\": [{\"type\": \"Feature\", \"id\": 1, \"geometry\": {\"type\": \"MultiLineString\", \"coordinates\": [[[0.0,0.0],[1.0,1.0]]], \"bbox\": [0.0,0.0,1.0,1.0] }, \"properties\": {\"FID\": 335, \"PLAN_REF\": \"151\", \"APP_TYPE\": \"RETENTION\", \"LOCATION\": \"Knockglass, Ballinameen, Co. Roscommon.\", \"REC_DATE\": \"05/01/2015\", \"DESCRIPT\": \"Of entrance to existing forest plantation for extraction of timber at various times at \", \"APPSTATUS\": \"Application Finalised\", \"DEC_DATE\": \"20/02/2015\", \"DECISION\": \"Granted (Conditional)\", \"APPE_DEC\": \"n/a\", \"APPE_DAT\": \"n/a\", \"MOREINFO\": \"http://www.eplanning.ie/roscommoneplan/FileRefDetails.aspx?file_number=151\", \"WGS_LONG\": \"-8.31141\", \"WGS_LAT\": \"53.89448\"} }] }";
        var json = GeoJSONFactory.create(geoJSON);
        assertThat(json, instanceOf(FeatureCollection.class));
        var fc = (FeatureCollection)json;
        assertThat(fc.getFeatures(), arrayWithSize(greaterThan(0)));
        var f = fc.getFeatures()[0];
        assertThat(f.getGeometry(), notNullValue());
        assertThat(f.getGeometry(), instanceOf(MultiLineString.class));
        var point = (MultiLineString)f.getGeometry();
        assertThat(point.getBbox(), notNullValue());
        assertThat(point.getBbox().length, equalTo(4));
        assertThat(point.getBbox()[0], equalTo(0.0));
        assertThat(point.getBbox()[1], equalTo(0.0));
        assertThat(point.getBbox()[2], equalTo(1.0));
        assertThat(point.getBbox()[3], equalTo(1.0));
    }

    @Test
    public void polygonBboxPropertyTest() {
        var geoJSON = "{\"type\": \"FeatureCollection\", \"features\": [{\"type\": \"Feature\", \"id\": 1, \"geometry\": {\"type\": \"Polygon\", \"coordinates\": [[[0.0,0.0],[0.0,1.0],[1.0,1.0],[0.0,1.0],[0.0,0.0]]], \"bbox\": [0.0,0.0,1.0,1.0] }, \"properties\": {\"FID\": 335, \"PLAN_REF\": \"151\", \"APP_TYPE\": \"RETENTION\", \"LOCATION\": \"Knockglass, Ballinameen, Co. Roscommon.\", \"REC_DATE\": \"05/01/2015\", \"DESCRIPT\": \"Of entrance to existing forest plantation for extraction of timber at various times at \", \"APPSTATUS\": \"Application Finalised\", \"DEC_DATE\": \"20/02/2015\", \"DECISION\": \"Granted (Conditional)\", \"APPE_DEC\": \"n/a\", \"APPE_DAT\": \"n/a\", \"MOREINFO\": \"http://www.eplanning.ie/roscommoneplan/FileRefDetails.aspx?file_number=151\", \"WGS_LONG\": \"-8.31141\", \"WGS_LAT\": \"53.89448\"} }] }";
        var json = GeoJSONFactory.create(geoJSON);
        assertThat(json, instanceOf(FeatureCollection.class));
        var fc = (FeatureCollection)json;
        assertThat(fc.getFeatures(), arrayWithSize(greaterThan(0)));
        var f = fc.getFeatures()[0];
        assertThat(f.getGeometry(), notNullValue());
        assertThat(f.getGeometry(), instanceOf(Polygon.class));
        var point = (Polygon)f.getGeometry();
        assertThat(point.getBbox(), notNullValue());
        assertThat(point.getBbox().length, equalTo(4));
        assertThat(point.getBbox()[0], equalTo(0.0));
        assertThat(point.getBbox()[1], equalTo(0.0));
        assertThat(point.getBbox()[2], equalTo(1.0));
        assertThat(point.getBbox()[3], equalTo(1.0));
    }

    @Test
    public void multiPolygonBboxProperty() {
        var geoJSON = "{\"type\": \"FeatureCollection\", \"features\": [{\"type\": \"Feature\", \"id\": 1, \"geometry\": {\"type\": \"MultiPolygon\", \"coordinates\": [[[[0.0,0.0],[0.0,1.0],[1.0,1.0],[0.0,1.0],[0.0,0.0]]]], \"bbox\": [0.0,0.0,1.0,1.0] }, \"properties\": {\"FID\": 335, \"PLAN_REF\": \"151\", \"APP_TYPE\": \"RETENTION\", \"LOCATION\": \"Knockglass, Ballinameen, Co. Roscommon.\", \"REC_DATE\": \"05/01/2015\", \"DESCRIPT\": \"Of entrance to existing forest plantation for extraction of timber at various times at \", \"APPSTATUS\": \"Application Finalised\", \"DEC_DATE\": \"20/02/2015\", \"DECISION\": \"Granted (Conditional)\", \"APPE_DEC\": \"n/a\", \"APPE_DAT\": \"n/a\", \"MOREINFO\": \"http://www.eplanning.ie/roscommoneplan/FileRefDetails.aspx?file_number=151\", \"WGS_LONG\": \"-8.31141\", \"WGS_LAT\": \"53.89448\"} }] }";
        var json = GeoJSONFactory.create(geoJSON);
        assertThat(json, instanceOf(FeatureCollection.class));
        var fc = (FeatureCollection)json;
        assertThat(fc.getFeatures(), arrayWithSize(greaterThan(0)));
        var f = fc.getFeatures()[0];
        assertThat(f.getGeometry(), notNullValue());
        assertThat(f.getGeometry(), instanceOf(MultiPolygon.class));
        var point = (MultiPolygon)f.getGeometry();
        assertThat(point.getBbox(), notNullValue());
        assertThat(point.getBbox().length, equalTo(4));
        assertThat(point.getBbox()[0], equalTo(0.0));
        assertThat(point.getBbox()[1], equalTo(0.0));
        assertThat(point.getBbox()[2], equalTo(1.0));
        assertThat(point.getBbox()[3], equalTo(1.0));
    }


}
