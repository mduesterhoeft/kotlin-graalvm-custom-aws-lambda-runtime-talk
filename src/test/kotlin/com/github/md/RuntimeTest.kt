package com.github.md

import com.amazonaws.services.lambda.runtime.RequestHandler
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region.EU_CENTRAL_1
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import java.lang.Exception
import java.net.URI


fun main() {
    val dynamoPort = System.getenv("DYNAMO_PORT") ?: "8000"
    val dynamoHost = System.getenv("DYNAMO_HOST") ?: "localhost"

    val dynamoDbClient = DynamoDbClient.builder()
            .region(EU_CENTRAL_1)
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("dummy", "dummy")))
            .endpointOverride(URI("http://$dynamoHost:$dynamoPort")).build()
            .also { DynamoDBSetup(it).createTables() }

    val handlerClass = Class.forName("com.github.md.Handler")

    // the no-args constructor also needs to be called to be included in the reflection config by native-image-agent
    try { handlerClass.newInstance() } catch (e: Exception) {}

    Runtime(MockRuntime(ApiGatewayRequest()
            .apply {
                pathParameters = mapOf("name" to "some")
            }), Handler(dynamoDbClient)).processInvocation()
}