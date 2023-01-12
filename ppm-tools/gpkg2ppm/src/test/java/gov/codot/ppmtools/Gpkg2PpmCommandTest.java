package gov.codot.ppmtools;

import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;



public class Gpkg2PpmCommandTest {

    @Test
    public void appCanBeInstantiated() {
        var theApp = new Gpkg2PpmCommand();
        assertThat(theApp, notNullValue());
    }

    @Test
    public void runCommandLine_NoParams() {
        var exitCode = new CommandLine(new Gpkg2PpmCommand()).execute();
        assertThat(exitCode, not(equalTo(0)));
    }
}
