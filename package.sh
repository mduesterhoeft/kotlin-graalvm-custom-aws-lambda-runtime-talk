#!/usr/bin/env bash
./gradlew shadowJar

rm -rf build/package
mkdir build/package

docker run --rm --name graal -v $(pwd):/working oracle/graalvm-ce:1.0.0-rc12 \
    /bin/bash -c "native-image --enable-url-protocols=http \
                    -Djava.net.preferIPv4Stack=true \
                    -H:ReflectionConfigurationFiles=/working/reflect.json \
                    -H:+ReportUnsupportedElementsAtRuntime --no-server -jar /working/build/libs/package-1.0.jar \
                    ; \
                    cp package-1.0 /working/build/package/server"

cp runtime/bootstrap build/package

zip -j build/package/bundle.zip build/package/*