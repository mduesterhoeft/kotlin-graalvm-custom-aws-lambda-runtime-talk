## Fighting cold startup issues for your Kotlin Lambda with GraalVM

---

![](slides/intro.png)

---?color=#0047b3

## Cold Startup and the JVM

---

### Cold Startup and the JVM

<img src="slides/lambda-coldstart.png" style="width:80%" />

<a href="https://theburningmonk.com/2017/06/aws-lambda-compare-coldstart-time-with-different-languages-memory-and-code-sizes/" style="font-size:small;">Yan Cui - aws lambda – compare coldstart time...</a>

---?color=#0099cc

## GraalVM

---

### GraalVM

- compile ahead-of-time into a native executable
- resulting program does not run on the HotSpot VM
- faster startup time and lower runtime memory overhead

---

### GraalVM

![](slides/netty-startup.png)

---?color=#b7410e

## Custom AWS Lambda Runtimes

---

<img src="slides/custom-runtime-tweet.png" style="width:80%" />

---

### Custom AWS Lambda Runtimes

- open up AWS Lambda for any programming language
- is a program that runs the Lambda handler
- can be included in the deployment package

---

### Custom AWS Lambda Runtimes

![](slides/custom-runtime.png)


