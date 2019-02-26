---?image=slides/title.jpg

<h2 style="color:white;">Fighting cold startup issues for your Kotlin Lambda with GraalVM</h2>

---

![](slides/intro.png)

---?color=#0047b3

## Cold Startup

---

### What is Cold Startup

![](slides/cold-start.png)

---

### Cold Startup and the JVM

What is happening during JVM startup?

- JIT compilation
- garbage collection
- class loading
- static initialization
- ...

---

### Cold Startup and the JVM

<img src="slides/lambda-coldstart.png" style="width:80%" />

<a href="https://theburningmonk.com/2017/06/aws-lambda-compare-coldstart-time-with-different-languages-memory-and-code-sizes/" style="font-size:small;">Yan Cui - aws lambda – compare coldstart time...</a>


---

![](slides/tim-bray-languange-choices.png)

<a href="https://www.youtube.com/watch?v=IPOvrK3S3gQ" style="font-size:small;">AWS re:Invent 2018: [REPEAT 1] Inside AWS: Technology Choices for Modern Applications (SRV305-R1)</a>


---?color=#0099cc

## GraalVM

---

### GraalVM

@quote[For existing Java applications, GraalVM can provide benefits by running them faster, ... creating ahead-of-time compiled native images.](https://www.graalvm.org/docs/why-graal/)

---

### GraalVM - Native Images

@ul

- GraalVM can create native images for existing JVM-based applications
- native image generation employs static analysis to find any code reachable from the main Java method
- the reachable code is then compiled AOT into machine code
- the resulting executable is self-contained (contains VM components)

@ulend

---

### GraalVM

![](slides/netty-startup.png)

<a href="https://medium.com/graalvm/instant-netty-startup-using-graalvm-native-image-generation-ed6f14ff7692" style="font-size:small;">Codrut Stancu - Instant Netty Startup using GraalVM Native Image Generation</a>

---?color=#b7410e

## Custom AWS Lambda Runtimes

---

<img src="slides/custom-runtime-tweet.png" style="width:80%" />

---

### Custom AWS Lambda Runtimes

@ul

- open up AWS Lambda for any programming language
- a runtime is a program that runs the Lambda handler
- can be included in the deployment package

@ulend

---

### Custom AWS Lambda Runtimes

![](slides/custom-runtime.png)

---?color=#092736

## Demo

---

## Show me the numbers

Observed cold startup times

<canvas data-chart="line">
<!-- 
{
 "data": {
  "datasets": [
   {
    "data":[142,154,135,173,173],
    "label":"custom runtime", "backgroundColor":"rgba(13,143,143,.8)"
   },
   {
    "data":[327,386,308,342,310],
    "label":"custom runtime with DynamoDB", "backgroundColor":"rgba(20,220,220,.8)"
   },
   {
    "data":[2240,2038,2150,2110,2080],
    "label":"java8 runtime", "backgroundColor":"rgba(219,86,20,.8)"
   },
   {
    "data":[3800,3730,3950,4160,3470],
    "label":"java8 runtime with DynamoDB","backgroundColor":"rgba(143,35,13,.8)"
   }
  ]
 }, 
 "options": { "responsive": "true" }
}
-->
</canvas>

---

## Conclusion

@ul

- GraalVM native images greatly reduce startup time
- native image also seem to improve runtime performance
- creating native images for an application is cumbersome
- native image generation is slow

@ulend

---

@size[2em](Questions?)

---

![](slides/link.png)

@fa[eye] [https://gitpitch.com/mduesterhoeft/kotlin-graalvm-custom-aws-lambda-runtime-talk](https://gitpitch.com/mduesterhoeft/kotlin-graalvm-custom-aws-lambda-runtime-talk)

@fa[github] [mduesterhoeft/kotlin-graalvm-custom-aws-lambda-runtime-talk](https://github.com/mduesterhoeft/kotlin-graalvm-custom-aws-lambda-runtime-talk)

@fa[medium] [Blog: Fighting cold startup issues for your Kotlin Lambda with GraalVM](https://medium.com/@mathiasdpunkt/fighting-cold-startup-issues-for-your-kotlin-lambda-with-graalvm-39d19b297730)



