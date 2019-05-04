package com.github.md

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.util.UUID

class MockRuntime(val request: ApiGatewayRequest): RuntimeApi {
    private val objectMapper = jacksonObjectMapper()

    override val currentRequestId: String = UUID.randomUUID().toString()

    init {
        objectMapper.writeValueAsString(request).let { objectMapper.readValue<ApiGatewayRequest>(it) }
    }

    override fun getInvocation(): ApiGatewayRequest = request.also { objectMapper.writeValueAsString(it) }

    override fun sendResponse(response: ApiGatewayResponse) {
        println("received response ${objectMapper.writeValueAsString(response)}")
    }
}