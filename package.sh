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
                    --delay-class-initialization-to-runtime=software.amazon.awssdk.awscore.client.builder.AwsDefaultClientBuilder \
                    --delay-class-initialization-to-runtime=software.amazon.awssdk.auth.signer.internal.AbstractAws4Signer \
                    --delay-class-initialization-to-runtime=software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider \
                    --delay-class-initialization-to-runtime=software.amazon.awssdk.utils.IoUtils \
                    --delay-class-initialization-to-runtime=software.amazon.awssdk.utils.internal.SystemSettingUtils \
                    --delay-class-initialization-to-runtime=software.amazon.awssdk.protocols.json.internal.unmarshall.JsonErrorCodeParser \
                    --delay-class-initialization-to-runtime=software.amazon.awssdk.core.internal.http.pipeline.stages.HandleResponseStage \
                    --delay-class-initialization-to-runtime=software.amazon.awssdk.core.internal.http.pipeline.stages.RetryableStage \
                    --delay-class-initialization-to-runtime=software.amazon.awssdk.regions.providers.AwsRegionProviderChain \
                    --delay-class-initialization-to-runtime=software.amazon.awssdk.core.io.ReleasableInputStream \
                    --delay-class-initialization-to-runtime=software.amazon.awssdk.regions.util.HttpResourcesUtils \
                    --delay-class-initialization-to-runtime=software.amazon.awssdk.http.apache.ApacheHttpClient \
                    --delay-class-initialization-to-runtime=software.amazon.awssdk.http.apache.internal.RepeatableInputStreamRequestEntity \
                    --delay-class-initialization-to-runtime=software.amazon.awssdk.http.apache.internal.net.SdkSslSocket \
                    --delay-class-initialization-to-runtime=software.amazon.awssdk.http.apache.internal.net.SdkSocket \
                    --delay-class-initialization-to-runtime=software.amazon.awssdk.core.internal.util.UserAgentUtils \
                    --delay-class-initialization-to-runtime=software.amazon.awssdk.core.interceptor.ExecutionInterceptorChain \
                    --delay-class-initialization-to-runtime=software.amazon.awssdk.core.internal.http.StreamManagingStage \
                    --delay-class-initialization-to-runtime=software.amazon.awssdk.core.internal.http.pipeline.stages.utils.ExceptionReportingUtils \
                    --delay-class-initialization-to-runtime=software.amazon.awssdk.profiles.internal.ProfileFileReader \
                    -H:+ReportUnsupportedElementsAtRuntime --no-server -jar /working/build/libs/package-1.0.jar \
                    ; \
                    cp package-1.0 /working/build/package/server; \
                    cp /opt/graalvm-ce-${GRAAL_VERSION}/jre/lib/security/cacerts /working/build/package/cacerts; \
                    cp /opt/graalvm-ce-${GRAAL_VERSION}/jre/lib/amd64/libsunec.so /working/build/package/libsunec.so"

cp runtime/* build/package

zip -j build/package/bundle.zip build/package/*