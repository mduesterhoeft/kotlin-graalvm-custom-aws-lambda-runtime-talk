#!/usr/bin/env bash
./gradlew shadowJar

rm -rf build/package
mkdir build/package

GRAAL_VERSION="1.0.0-rc15"
NATIVE_IMAGE_CONFIG_DIR="/working/build/native-image-config"

docker run --rm --name graal -v $(pwd):/working oracle/graalvm-ce:${GRAAL_VERSION} \
    /bin/bash -c "native-image --enable-http --enable-https --enable-url-protocols=http,https --enable-all-security-services \
                    -Djava.net.preferIPv4Stack=true \
                    -H:ConfigurationFileDirectories=${NATIVE_IMAGE_CONFIG_DIR} \
                    -H:+ReportUnsupportedElementsAtRuntime --no-server -jar /working/build/libs/package-1.0.jar; \

                    cp package-1.0 /working/build/package/server; \
                    cp /opt/graalvm-ce-${GRAAL_VERSION}/jre/lib/security/cacerts /working/build/package/cacerts; \
                    cp /opt/graalvm-ce-${GRAAL_VERSION}/jre/lib/amd64/libsunec.so /working/build/package/libsunec.so"

cp runtime/* build/package

zip -j build/package/bundle.zip build/package/*