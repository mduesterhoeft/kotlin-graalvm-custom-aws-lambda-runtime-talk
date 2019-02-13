package com.github.md

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler

class Handler: RequestHandler<ApiGatewayRequest, ApiGatewayResponse> {

  override fun handleRequest(input: ApiGatewayRequest, context:Context): ApiGatewayResponse {
    println("received: $input")

    return ApiGatewayResponse.build {
      statusCode = 200
      objectBody = HelloResponse("Hello ${input.pathParameters?.values?.first()}")
      headers = mapOf("X-Powered-By" to "AWS Lambda & serverless")
    }
  }
}
