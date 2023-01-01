package gov.codot.ppmtools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Gpkg2PpmCommandTest {
    @Test void appCanBeInstantiated() {
        var theApp = new Gpkg2PpmCommand();
        assertNotNull(theApp, "Really ought to write some real tests for this.");
    }
}
