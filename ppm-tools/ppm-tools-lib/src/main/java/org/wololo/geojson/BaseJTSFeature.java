package org.wololo.geojson;

/**
 * A GeoJSON feature with a JTS geomtry
 * 
 * Not serializable to JSON without converting to a "wololo" Feature
 */
public abstract class BaseJTSFeature<TGeometry extends org.locationtech.jts.geom.Geometry, TProperties> {
    
    protected final TGeometry geometry;
    protected final TProperties properties;

    public BaseJTSFeature(TGeometry geometry, TProperties properties) {
        this.geometry = geometry;
        this.properties = properties;
    }

    public final TGeometry getGeometry(){
        return geometry;
    }
    public final TProperties getProperties() {
        return properties;
    }
}
