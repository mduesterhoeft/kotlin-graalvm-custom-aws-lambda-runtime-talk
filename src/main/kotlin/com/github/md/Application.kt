package com.github.md

import com.amazonaws.services.lambda.runtime.ClientContext
import com.amazonaws.services.lambda.runtime.CognitoIdentity
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.LambdaLogger
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.http4k.client.JavaHttpClient
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Method.*
import org.http4k.core.Request
import org.http4k.core.Response

const val requestIdHeaderName = "lambda-runtime-aws-request-id"

val client: HttpHandler = JavaHttpClient()

val json = jacksonObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

@Suppress("UNCHECKED_CAST")
fun main() {
    val runtimeApiEndpoint = System.getenv("AWS_LAMBDA_RUNTIME_API")
    val handler = System.getenv("_HANDLER")
    val handlerInstance = Class.forName(handler).newInstance() as RequestHandler<ApiGatewayRequest, ApiGatewayResponse>

    while(true) {
        val invocationResponse: Response =
                client(Request(GET, "http://$runtimeApiEndpoint/2018-06-01/runtime/invocation/next"))

        val requestId = invocationResponse.header(requestIdHeaderName)!!

        val request = json.readValue<ApiGatewayRequest>(invocationResponse.body.stream)

        val response = handlerInstance.handleRequest(request, CustomContext(requestId))

        client(Request(POST, "http://$runtimeApiEndpoint/2018-06-01/runtime/invocation/$requestId/response"
        ).body(json.writeValueAsString(response)))
    }
}

class CustomContext(val requestId: String): Context {
    override fun getAwsRequestId(): String = requestId

    override fun getLogStreamName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

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

    override fun getMemoryLimitInMB(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLogGroupName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFunctionVersion(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getIdentity(): CognitoIdentity {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}