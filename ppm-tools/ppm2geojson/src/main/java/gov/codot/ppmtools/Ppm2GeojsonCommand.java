package gov.codot.ppmtools;

import gov.codot.ppmtools.geojson.PpmLineFeatureCollection;
import gov.codot.ppmtools.mapfile.PpmLineCollection;
import org.apache.commons.io.FilenameUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ArgGroup;

import java.io.*;


@Command(name = "ppm2geojson", description = "Converts between ODE PPM 'edges' files and Geojson")
public class Ppm2GeojsonCommand implements Runnable {

    public static void main(String... args) throws Exception {
        var exitCode = new CommandLine(new Ppm2GeojsonCommand()).execute(args);
        System.exit(exitCode);
    }

    @ArgGroup(exclusive = true, multiplicity = "1", heading = "Conversion Direction\n")
    ConversionDirection direction;
    static class ConversionDirection {
        @Option(names = {"-p", "--ppm-to-geojson"}, description = "Convert from PPM .edges file to Geojson.")
        public boolean ppmToGeojson;

        @Option(names = {"-g", "--geojson-to-ppm"}, description = "Convert from Geojson to PPM .edges file.")
        public boolean geojsonToPpm;
    }

    @Option(required = true, names = { "-i", "--infile" }, description = "The path to the input file, either a PPM '.edges' file or Geojson file.")
    private File infile;

    @Option(required = true, names = { "-o", "--outfile" }, description = "The path to the output file, either a PPM '.edges' file or Geojson file.")
    private File outfile;

    @Option(names = { "-f", "--force" }, description = "Force overwriting output file if it exists already.")
    private boolean force;



    @Override
    public void run()  {
        try {
            if (infile != null && !infile.exists()) {
                System.out.printf("File not found: %s%n", infile);
            } else {
                System.out.printf("File found: %s%n", infile);
            }

            if (direction.ppmToGeojson) {
                System.out.println("Converting PPM to Geojson.");
                ppmToGeojson();
            }

            if (direction.geojsonToPpm) {
                System.out.println("Converting Geojson to PPM.");
                geojsonToPpm();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof RuntimeException) {
                throw (RuntimeException)e;
            } else {
                throw new RuntimeException(e);
            }
        }

    }

    private void ppmToGeojson() throws IOException {
        // Validate file types
        String inExt = FilenameUtils.getExtension(infile.getName()).toLowerCase();
        String outExt = FilenameUtils.getExtension(outfile.getName()).toLowerCase();
        if (!"edges".equals(inExt)) {
            System.out.printf("Input file %s is not an '.edges' file.%n", infile.getName());
            throw new RuntimeException();
        }
        if (!"geojson".equals(outExt)) {
            System.out.printf("Output file %s is not a '.geojson' file.%n", outfile.getName());
            throw new RuntimeException();
        }

        PpmLineCollection lineCollection = PpmLineCollection.readMapFile(infile);
        PpmLineFeatureCollection geojson = lineCollection.toGeojson();

        if (!deleteExistingFile()) return;


        String strGeojson = geojson.toString();
        try (var writer = new FileWriter(outfile)) {
            writer.write(strGeojson);
        }
    }

    private void geojsonToPpm() throws IOException {
        // Validate file types
        String inExt = FilenameUtils.getExtension(infile.getName()).toLowerCase();
        String outExt = FilenameUtils.getExtension(outfile.getName()).toLowerCase();
        if (!"geojson".equals(inExt)) {
            System.out.printf("Input file %s is not a '.geojson' file.%n", infile.getName());
            throw new RuntimeException();
        }
        if (!"edges".equals(outExt)) {
            System.out.printf("Output file %s is not an '.edges' file.%n", outfile.getName());
            throw new RuntimeException();
        }

        var geojson = PpmLineFeatureCollection.readGeojsonFile(infile);
        var lineCollection = PpmLineCollection.fromGeojson(geojson);

        if (!deleteExistingFile()) return;

        lineCollection.writeMapfile(outfile);
    }

    private boolean deleteExistingFile() {
        if (!outfile.exists()) {
            return true;
        }
        if (force) {
            if (outfile.delete()) {
                System.out.printf("Deleted existing file %s%n", outfile);
                return true;
            } else {
                throw new RuntimeException("Could not delete existing file.");
            }
        } else {
            System.out.printf("Output file %s exists, not saved.  Use -f option to force overwriting.%n", outfile);
            return false;
        }
    }

}