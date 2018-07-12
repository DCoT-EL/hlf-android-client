#!/bin/bash

rm ./build/outputs/jar/hlf-android-client-release.zip
rm -rf ./build/outputs/jar/hlf-android-client-release
rm ./build/outputs/jar/hlf-android-client.jar
./gradlew assembleRelease
mkdir ./build/outputs/jar
cp ./build/outputs/aar/hlf-android-client-release.aar ./build/outputs/jar/hlf-android-client-release.zip
unzip  -o ./build/outputs/jar/hlf-android-client-release.zip -d ./build/outputs/jar/hlf-android-client-release
mv  ./build/outputs/jar/hlf-android-client-release/classes.jar  ./build/outputs/jar/hlf-android-client.jar
cp  ./build/outputs/jar/hlf-android-client.jar ../MyApplication/app/libs/hlf-android-client.jar
