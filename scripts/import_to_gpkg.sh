#!/bin/bash

# Load PBF file into Geopackage format
INFILE=colorado-latest-motorways.osm.pbf
OUTFILE=colorado-latest-motorways.osm.gpkg
INPATH="$PWD/$INFILE"
OUTPATH="$PWD/$OUTFILE"

if [[ ! -f $OUTPATH ]]
then
    echo "Importing $INPATH into Geopackage $OUTPATH"
    ogr2ogr -f GPKG $OUTPATH $INPATH
else
    echo "File $OUTPATH already exists, not creating it"
fi