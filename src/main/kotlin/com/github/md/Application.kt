package com.github.md

import com.amazonaws.services.lambda.runtime.ClientContext
import com.amazonaws.services.lambda.runtime.CognitoIdentity
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.LambdaLogger
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.log4j.Logger
import org.http4k.client.JavaHttpClient
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Method.*
import org.http4k.core.Request
import org.http4k.core.Response
import org.slf4j.LoggerFactory

const val requestIdHeaderName = "lambda-runtime-aws-request-id"
const val runtimeApiEndpointVariableName = "AWS_LAMBDA_RUNTIME_API"
const val handlerVariableName = "_HANDLER"

val log = LoggerFactory.getLogger("runtime")
val client: HttpHandler = JavaHttpClient()

val json = jacksonObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

@Suppress("UNCHECKED_CAST")
fun main() {
    val runtimeApiEndpoint = System.getenv(runtimeApiEndpointVariableName)
    val handler = System.getenv(handlerVariableName)
    val handlerInstance = Class.forName(handler).newInstance() as RequestHandler<ApiGatewayRequest, ApiGatewayResponse>

    while(true) {
        val invocationResponse: Response =
                client(Request(GET, "http://$runtimeApiEndpoint/2018-06-01/runtime/invocation/next"))

        val requestId = invocationResponse.header(requestIdHeaderName)!!

        try {
            val request = json.readValue<ApiGatewayRequest>(invocationResponse.body.stream)

            val response = handlerInstance.handleRequest(request, CustomContext(requestId))

            client(Request(POST, "http://$runtimeApiEndpoint/2018-06-01/runtime/invocation/$requestId/response")
                .body(json.writeValueAsString(response)))
        } catch(e: RuntimeException) {
           log.error("Error during invocation $requestId - ${e.message}", e)
            client(Request(POST, "http://$runtimeApiEndpoint/2018-06-01/runtime/invocation/$requestId/error")
                .body("""{ "errorMessage": "Error during invocation $requestId - ${e.message}""""))
        }
    }
}

// see https://docs.aws.amazon.com/lambda/latest/dg/current-supported-versions.html
class CustomContext(val requestId: String): Context {
    override fun getAwsRequestId(): String = requestId

    override fun getLogStreamName(): String = System.getenv("AWS_LAMBDA_LOG_STREAM_NAME")

    override fun getClientContext(): ClientContext {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFunctionName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRemainingTimeInMillis(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLogger(): LambdaLogger {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInvokedFunctionArn(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMemoryLimitInMB(): Int = System.getenv("AWS_LAMBDA_FUNCTION_MEMORY_SIZE").toInt()

    override fun getLogGroupName(): String = System.getenv("AWS_LAMBDA_LOG_GROUP_NAME")

    override fun getFunctionVersion(): String = System.getenv("AWS_LAMBDA_FUNCTION_VERSION")

    override fun getIdentity(): CognitoIdentity {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}