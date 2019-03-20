package com.github.md

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import java.util.UUID

class Handler: RequestHandler<ApiGatewayRequest, ApiGatewayResponse> {

  private val dynamoClient = DynamoDbClient.create()

  override fun handleRequest(input: ApiGatewayRequest, context:Context): ApiGatewayResponse {

      val name = input.pathParameters?.values?.first()

      dynamoClient.putItem(
          PutItemRequest.builder()
              .tableName("greetings")
              .item(mutableMapOf(
                  "id" to AttributeValue.builder().s(UUID.randomUUID().toString()).build(),
                  "name" to AttributeValue.builder().s(name).build()
              )).build())

      return ApiGatewayResponse.build {
          statusCode = 200
          objectBody = HelloResponse("Hello $name")
          headers = mapOf("X-Powered-By" to "AWS Lambda & serverless")
      }
  }
}
