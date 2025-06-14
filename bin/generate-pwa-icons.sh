#!/bin/bash

# Create output directory if it doesn't exist
mkdir -p ../app/src/main/resources/static/img

# Convert icon to various sizes
magick ../app/src/main/resources/static/img/icon.png -resize 192x192 ../app/src/main/resources/static/img/icon-192x192.png
magick ../app/src/main/resources/static/img/icon.png -resize 512x512 ../app/src/main/resources/static/img/icon-512x512.png

# Create a maskable icon with padding
magick ../app/src/main/resources/static/img/icon.png -resize 512x512 \
  -gravity center -background transparent -extent 512x512 \
  -fill white -draw "circle 256,256 256,0" \
  -alpha off -compose CopyOpacity -composite \
  ../app/src/main/resources/static/img/icon-maskable-512x512.png

# Create a maskable icon with padding
magick ../app/src/main/resources/static/img/icon.png -resize 192x192 \
  -gravity center -background transparent -extent 192x192 \
  -fill white -draw "circle 96,96 96,0" \
  -alpha off -compose CopyOpacity -composite \
  ../app/src/main/resources/static/img/icon-maskable-192x192.png

echo "PWA icons generated successfully!"
