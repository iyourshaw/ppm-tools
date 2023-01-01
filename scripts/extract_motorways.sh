#!/bin/bash

# Extract motorways from OSM data
INFILE=colorado-latest.osm.pbf
OUTFILE=colorado-latest-motorways.osm.pbf
INPATH="$PWD/$INFILE"
OUTPATH="$PWD/$OUTFILE"

if [[ ! -f $OUTPATH ]]
then
    echo "Extracting motorways from $INPATH to $OUTPATH"
    osmosis --read-pbf-fast $INPATH --tf accept-ways highway=motorway --tf reject-relations  --used-node --write-pbf $OUTPATH
else
    echo "File $OUTPATH already exists, not extracting motorways"
fi