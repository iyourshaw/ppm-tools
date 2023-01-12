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


public class Ppm2GeojsonCommandTest {

    @Test
    public void appCanBeInstantiated() {
        var theApp = new Ppm2GeojsonCommand();
        assertThat(theApp, notNullValue());
    }

    @Test
    public void edgesToGeojsonTest() throws IOException {
        Path inDir = Path.of("", "src/test/resources");
        File inFile = inDir.resolve("test.edges").toFile();
        String inFilePath =  inFile.getPath();

        File outFile = File.createTempFile("test", ".geojson");
        outFile.deleteOnExit();
        String outFilePath = outFile.getPath();

        var exitCode = new CommandLine(new Ppm2GeojsonCommand()).execute("-i", inFilePath, "-o", outFilePath, "-p", "-f");
        assertThat(exitCode, equalTo(0));
        assertTrue(outFile.exists(), "Output file exists");
        String writtenFile = FileUtils.readFileToString(outFile, StandardCharsets.UTF_8);
        assertThat(writtenFile, containsString("FeatureCollection"));
    }

    @Test
    public void geojsonToPpmTest() throws IOException {
        Path inDir = Path.of("", "src/test/resources");
        File inFile = inDir.resolve("featureCollection.geojson").toFile();
        String inFilePath = inFile.getPath();

        File outFile = File.createTempFile("test", ".edges");
        //outFile.deleteOnExit();
        String outFilePath = outFile.getPath();

        var exitCode = new CommandLine(new Ppm2GeojsonCommand()).execute("-i", inFilePath, "-o", outFilePath, "-g", "-f");
        assertThat(exitCode, equalTo(0));
        assertTrue(outFile.exists(), "Output file exists");
        String writtenFile = FileUtils.readFileToString(outFile, StandardCharsets.UTF_8);
        assertThat(writtenFile, containsString("edge"));
    }

    @Test
    public void edgesToGeojsonTest_WrongInputFileType() {
        var exitCode = new CommandLine(new Ppm2GeojsonCommand()).execute("-i", "test.wrong", "-o", "test.geojson", "-p");
        assertThat(exitCode, not(equalTo(0)));
    }

    @Test
    public void edgesToGeojsonTest_WrongOutputFileType() {
        var exitCode = new CommandLine(new Ppm2GeojsonCommand()).execute("-i", "test.edges", "-o", "test.wrong", "-p");
        assertThat(exitCode, not(equalTo(0)));
    }

    @Test
    public void geojsonToPpmTest_WrongInputFileType() {
        var exitCode = new CommandLine(new Ppm2GeojsonCommand()).execute("-i", "test.wrong", "-o", "test.edges", "-g");
        assertThat(exitCode, not(equalTo(0)));
    }

    @Test
    public void geojsonToPpmTest_WrongOutputFiletype() {
        var exitCode = new CommandLine(new Ppm2GeojsonCommand()).execute("-i", "test.geojson", "-o", "test.wrong", "-g");
        assertThat(exitCode, not(equalTo(0)));
    }


}
