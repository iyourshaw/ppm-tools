package gov.codot.ppmtools;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


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

    @Test
    public void gpkgToPpmTest() throws IOException {
        Path inDir = Path.of("", "src/test/resources");
        File inFile = inDir.resolve("test.gpkg").toFile();
        String inFilePath = inFile.getPath();

        File outFile = File.createTempFile("test", ".edges");
        outFile.deleteOnExit();
        String outFilePath = outFile.getPath();

        var exitCode = new CommandLine(new Gpkg2PpmCommand()).execute("-v", "-o", outFilePath, "-w", "motorway", "-r", "I 70", inFilePath);
        assertThat(exitCode, equalTo(0));
        assertTrue(outFile.exists(), "Output file exists");
        String writtenFile = FileUtils.readFileToString(outFile, StandardCharsets.UTF_8);
        assertThat(writtenFile, containsString("edge"));
        assertThat(writtenFile, containsString("I-70"));
    }
}
