[![GitPitch](https://gitpitch.com/assets/badge.svg)](https://gitpitch.com/mduesterhoeft/kotlin-graalvm-custom-aws-lambda-runtime-talk#/)

# Fighting cold startup issues for your Kotlin Lambda with GraalVM

This is the demo code for a talk on improving cold startup times for JVM-based lambdas using [GraalVM](https://www.graalvm.org/) and [Custom AWS Lambda Runtimes](https://docs.aws.amazon.com/lambda/latest/dg/runtimes-custom.html).

## Slides

The presentation slides can be found here - https://gitpitch.com/mduesterhoeft/kotlin-graalvm-custom-aws-lambda-runtime-talk#/

## Running the sample

The `master` branch contains a runnable sample of an application that exposes the same [handler](src/main/kotlin/com/github/md/Handler.kt) and exposes it via the standard `java8` runtime and a custom runtime.

To build and deploy the function run the following:

```
# build the GraalVM native image and package the runime
./package.sh 
# deploy the application
serverless deploy
```

Invoke the function running the `java8` runtime
```
http https://<function-host>/dev/hello/sample
```

Invoke the function running the `custom` runtime
```
http https://<function-host>/dev/hello-runtime/sample
```

