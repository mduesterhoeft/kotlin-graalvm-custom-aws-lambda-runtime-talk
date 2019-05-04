#!/bin/sh
./gradlew testJar

rm -rf build/package
mkdir build/package
CONFIG_DIR="build/native-image-config"
rm -rf ${CONFIG_DIR}
mkdir ${CONFIG_DIR}
NATIVE_IMAGE_CONFIG_DIR="/working/${CONFIG_DIR}"

docker run --rm --name dynamodb-local --detach -p 8000:8000 amazon/dynamodb-local || true

# see https://github.com/oracle/graal/blob/vm-1.0.0-rc15/substratevm/CONFIGURE.md
GRAAL_VERSION="1.0.0-rc15"
docker run --rm --name graal -v $(pwd):/working oracle/graalvm-ce:${GRAAL_VERSION} \
    /bin/bash -c "
            set -o pipefail; \
            set -e; \
            echo '*** running test runtime with native-image-agent'; \
            DYNAMO_HOST=host.docker.internal java -agentlib:native-image-agent=trace-output=/tmp/trace.json -jar /working/build/libs/package-1.0-test.jar ; \

            echo '*** building native-image-configure'; \
            native-image --tool:native-image-configure; \

            echo '*** running native-image-configure'; \
            ./native-image-configure process-trace --output-dir=${NATIVE_IMAGE_CONFIG_DIR} /tmp/trace.json; \
           "