package org.wololo.geojson;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * General-purpose GeoJSON properties backed by a LinkedHashMap.
 */
public class Properties extends LinkedHashMap<String, Object> {


    public Properties() {
    }

    public Properties(Map<String, Object> propertyMap) {
        this();
        putAll(propertyMap);
    }


}
