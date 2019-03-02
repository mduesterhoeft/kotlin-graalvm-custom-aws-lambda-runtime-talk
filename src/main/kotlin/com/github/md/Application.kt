package com.github.md

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.http4k.client.JavaHttpClient
import org.http4k.core.HttpHandler

const val requestIdHeaderName = "lambda-runtime-aws-request-id"
const val runtimeApiEndpointVariableName = "AWS_LAMBDA_RUNTIME_API"
const val handlerVariableName = "_HANDLER"

val client: HttpHandler = JavaHttpClient()

val json = jacksonObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

@Suppress("UNCHECKED_CAST")
fun main() {

}