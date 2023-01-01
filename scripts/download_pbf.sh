#!/bin/bash

# Download all OSM data for Colorado
FILENAME=colorado-latest.osm.pbf
FILEPATH="$PWD/$FILENAME"
URL=http://download.geofabrik.de/north-america/us/colorado-latest.osm.pbf
if [[ ! -f $FILEPATH ]]
then
    echo "Downloading $FILEPATH"
    curl -o $FILENAME $URL
else
    echo "File $FILEPATH already exists, not downloading."
fi

