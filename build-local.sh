#!/bin/sh

cd covid-us-data
echo "Generating data set from https://github.com/CSSEGISandData/COVID-19 csse data..."
sbt run

echo "Copying data to frontend module..."
rm -rf ../covid-us-data-ui/src/data
mkdir ../covid-us-data-ui/src/data
cp output/* ../covid-us-data-ui/src/data

echo "Running fronent..."
cd ../covid-us-data-ui
npm install
npm run build

echo "Data processing & build is complete."
echo "Go to covid-us-data-ui and run 'npm start' to run ui locally..."
