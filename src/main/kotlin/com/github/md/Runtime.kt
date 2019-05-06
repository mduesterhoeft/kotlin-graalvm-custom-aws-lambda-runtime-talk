package com.github.md

import com.amazonaws.services.lambda.runtime.ClientContext
import com.amazonaws.services.lambda.runtime.CognitoIdentity
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.LambdaLogger
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.http4k.client.JavaHttpClient
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request

const val handlerVariableName = "_HANDLER"

@Suppress("UNCHECKED_CAST")
fun main() {
    val handler = System.getenv(handlerVariableName)
            .let { Class.forName(it).newInstance() as RequestHandler<ApiGatewayRequest, ApiGatewayResponse> }

    val runtime = Runtime(AwsRuntimeApi(), handler)

    while(true) {
        runtime.processInvocation()
    }
}

interface RuntimeApi {
    val currentRequestId: String

    fun getInvocation(): ApiGatewayRequest

    fun sendResponse(response: ApiGatewayResponse)
}

class AwsRuntimeApi: RuntimeApi {

    //Aws Lambdas are invoked in a strictly sequential manner - so we can keep the request id here
    override var currentRequestId: String = ""

    private val client: HttpHandler = JavaHttpClient()
    private val json: ObjectMapper = jacksonObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    private val runtimeApiEndpoint = System.getenv(runtimeApiEndpointVariableName)

    override fun getInvocation(): ApiGatewayRequest =
        client(Request(GET, "http://$runtimeApiEndpoint/2018-06-01/runtime/invocation/next"))
                .also { currentRequestId = it.header(requestIdHeaderName)!! }
                .let { json.readValue(it.body.stream) }

    override fun sendResponse(response: ApiGatewayResponse) {
        client(Request(POST, "http://$runtimeApiEndpoint/2018-06-01/runtime/invocation/$currentRequestId/response")
                .body(json.writeValueAsString(response)))    }

    companion object {
        private const val requestIdHeaderName = "lambda-runtime-aws-request-id"
        private const val runtimeApiEndpointVariableName = "AWS_LAMBDA_RUNTIME_API"
    }
}


class Runtime (
        private val runtimeApi: RuntimeApi,
        private val handler: RequestHandler<ApiGatewayRequest, ApiGatewayResponse>) {


    fun processInvocation() {
        runtimeApi.getInvocation()
                .let { handler.handleRequest(it, CustomContext(runtimeApi.currentRequestId)) }
                .let { runtimeApi.sendResponse(it) }
    }
}

// see https://docs.aws.amazon.com/lambda/latest/dg/current-supported-versions.html
class CustomContext(private val requestId: String): Context {
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