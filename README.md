# Introduction 
Tools for working with the "map file" format that the ODE PPM uses to represent geofences.

* `scripts` - Shell scripts that use open source geospatial tools to download Open Street Map data and convert it to Geopackage format.
* `ppm-tools` - Java command line tools:
    * `gpkg2ppm` - Tool for extracting Open Street Map road geometries from a Geopackage database and converting them into the "map file" format used by the ODE PPM module.
    * `ppm2geojson` - Tool for interconverting between PPM map files and Geojson.

# Getting Started

## PPM Map file format
The map file format used by the PPM for geofencing is defined here:

[PPM User Manual (Word Document)](https://github.com/usdot-jpo-ode/jpo-cvdp/blob/master/docs/ppm_user_manual.docx)

The file format as used by the PPM module is equivalent to an ordered collection of line edges.

The format consists of a CSV file with four fields:

```csv
type,id,geography,attributes
edge,7660,7660;38.7702364;-104.7798347:7661;38.7696286;-104.7794443,way_type=motorway:way_id=I-25-5
edge,7661,7661;38.7696286;-104.7794443:7662;38.7693753;-104.7792652,way_type=motorway:way_id=I-25-5
edge,7662,7662;38.7693753;-104.7792652:7663;38.7690526;-104.7790455,way_type=motorway:way_id=I-25-5
```

* `type` is always the word "edge"
* `id` is a unique long integer
* `geography` consists of a colon and semicolon delimted pair of geographic points with the following format:
```csv
<id1>;<lat1>;<long1>:<id2>;<lat2>;<long2>
```
* `attributes` consists of colon-delimited pairs of attributes including a `way_type` and `way_id` with the follwoing format:
```csv
way_type=<OSM or user defined way type>:way_id=<way_id>
```
The `way_type` determines the width in meters of the geofence and can be one of the Open Street Map way types or user defined constants defined in the following C++ class:
https://github.com/usdot-jpo-ode/jpo-cvdp/blob/master/cv-lib/src/osm.cpp

The `way_id` is effectively a human readable identifier for a road segment.



## Prerequisites:
* Bash shell
    * Built into any Linux disto 
    * On Windows: the Git Bash shell from Git for Windows: https://git-scm.com/download/win or from Cygwin: https://www.cygwin.com/install.html
* Curl
    * Linux (Ubuntu): apt install curl
    * Windows: https://curl.se/windows/
* OpenJDK 11
    * Linux: apt install openjdk-11-jdk
    * Windows: https://adoptopenjdk.net/
* GDAL (includes the ogr2ogr tool)
    * See the documentation for the ogr2ogr tool at https://gdal.org/programs/ogr2ogr.html
    * Version: 3.2.1, released 2020/12/29, or later.
    * Ubuntu Linux:
    ```bash
      sudo add-apt-repository ppa:ubuntugis/ppa
      sudo apt update
      sudo apt install gdal-bin
    ```
    * Windows: install from OSGeo4W: https://www.osgeo.org/projects/osgeo4w/, or bundled with QGIS (OSGeo4W Network Installer at https://www.qgis.org/en/site/forusers/download.html)
    * (untested) Docker: Probably might need the full version osgeo/gdal:ubuntu-full-latest to include the OSM stuff? https://github.com/OSGeo/gdal/tree/master/gdal/docker
* Osmosis
    * Version: 0.48
    * Reference: https://wiki.openstreetmap.org/wiki/Osmosis/Detailed_Usage_0.48
    * Linux: See https://wiki.openstreetmap.org/wiki/Osmosis/Installation#Linux
    * Windows: See https://wiki.openstreetmap.org/wiki/Osmosis/Quick_Install_(Windows)
    * (untested) Docker: See https://github.com/yagajs/docker-osmosis
* Gradle https://gradle.org/install/

## Software dependencies

### Build dependencies:
* GeoTools/JTS
* JTS2Geojson tool (https://github.com/iyourshaw/jts2geojson)
* PicoCLI
* JUnit Jupiter
* Guava
* Lombok


# Using the tools
To download OSM data and convert it to Geopackage format, run the three scripts in order in an environment with Curl, GDAL ogr2ogr, and Osmosis available on the command line:
```bash
download_pbf.sh
extract_motorways.sh
import_to_gpkg.sh
```

### Command Line Tools
Either download the gpkg2ppm.zip and ppm2geojson.zip tools from the distribution or build them from source as follows:

### Building from source
From within the `ppm-tools/ppm-tools` directory, build the tools using Gradle:
```bash
./gradlew clean build
```

### Running the gpkg2ppm command line tool
Use the downloaded zip file, or locate the `gpkg2ppm.zip` or `gpkg2ppm.tar` file in the ppm-tools/ppm-tools/gpkg2ppm/build/distributions folder, unzip or untar it, and within the unzipped directory structure, in the `bin` folder, run:
```bash
./gpkg2ppm <options> <infile>
```

### Running the ppm2geojson command line tool
Use the downloaded zip file, or locate the `ppm2geojson.zip` or `ppm2geojson.tar` file in the ppm-tools/ppm-tools/ppm2geojson/build/distrubitions flder, unzip of untar it, and within the uzippeed directory structure, in the `bin` folder, run:
```bash
./ppm2geojson -i infile -o outfile <options>
```

#### Gpkg2PPM Command Line Options

```bash
Usage: gpkg2ppm [-v] [-o=<outfile>] [-r=<routeNames>]... [-w=<wayTypes>]...
                <infile>
Converts road features from Open Street Map in Geopackage format to an 'edges'
file for the ODE PPM
      <infile>               The path to a Geopackage (.gpkg) file created from
                               Open Street Map XML or PBF data via the GDAL
                               ogr2ogr tool.  The gpkg must contain a
                               layer/table named 'lines' with road geometries
                               with standard OSM attributes.
  -o, --outfile=<outfile>    Output file path
  -r, --route=<routeNames>   Route names to include.  Examples: 'I 25', 'I 25
                               Express', 'E-470'
  -v, --verbose              Print out a lot of details.
  -w, --way-type=<wayTypes>  OSM way types to extract.
```

##### Command line example

To generate an edge file for I-70, I-25, and CO-470, including express lanes and the toll road, copy the `colorado-latest-motorways.osm.gpkg` file to `gpkg2ppm/bin` directory and execute the following command line:
```bash
./gpkg2ppm-cli -o CO-motorways.edges -r='I 25' -r='I 25 Express' -r='I 70' -r='CO 470' -r='E-470' colorado-latest-motorways.osm.gpkg
```

#### PPM2Geojson Command Line Options

```bash
Usage: ppm2geojson [-f] -i=<infile> -o=<outfile> (-p | -g)
Converts between ODE PPM 'edges' files and Geojson
  -f, --force               Force overwriting output file if it exists already.
  -i, --infile=<infile>     The path to the input file, either a PPM '.edges'
                              file or Geojson file.
  -o, --outfile=<outfile>   The path to the output file, either a PPM '.edges'
                              file or Geojson file.
Conversion Direction
  -g, --geojson-to-ppm      Convert from Geojson to PPM .edges file.
  -p, --ppm-to-geojson      Convert from PPM .edges file to Geojson.

```

##### Command line example

To convert the above '.edges' file to geojson, copy the `CO-motorways.edges` file to the `ppm2geojson/bin` directory and execute the following command line:

```bash
./ppm2geojson -i CO-motorways.edges -o CO-motorways.geojson -p
```

To geojson file can then be edited, for example using QGIS, and then converted back to the '.edges' format with the following command:

```bash
ppm2geojson/bin/ppm2geojson -i CO-motorways.geojson -o CO-motorways-edited.edges -g
```

