package com.github.md

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.logging.log4j.LogManager

class Handler:RequestHandler<ApiGatewayRequest, ApiGatewayResponse> {

  override fun handleRequest(input: ApiGatewayRequest, context:Context): ApiGatewayResponse {
    LOG.info("received: $input")

    return ApiGatewayResponse.build {
      statusCode = 200
      objectBody = HelloResponse("Hello ${input.pathParameters?.values?.first()}")
      headers = mapOf("X-Powered-By" to "AWS Lambda & serverless")
    }
  }

  companion object {
    private val LOG = LogManager.getLogger(Handler::class.java)
  }
}
