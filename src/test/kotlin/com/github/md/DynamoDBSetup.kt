package com.github.md

import org.slf4j.LoggerFactory
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.BillingMode
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement
import software.amazon.awssdk.services.dynamodb.model.KeyType.HASH
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType.S
import software.amazon.awssdk.services.dynamodb.model.TableStatus

class DynamoDBSetup(private val dynamoDbClient: DynamoDbClient) {

    private val log = LoggerFactory.getLogger(DynamoDBSetup::class.java)

    private val tableName = "greetings"

    fun createTables() {
        deleteTables()
        dynamoDbClient.createTable {
            it.tableName(tableName)
            it.billingMode(BillingMode.PAY_PER_REQUEST)
            it.attributeDefinitions(AttributeDefinition.builder()
                    .attributeName("id")
                    .attributeType(S)
                    .build()
            )
            it.keySchema(KeySchemaElement.builder()
                    .attributeName("id")
                    .keyType(HASH)
                    .build())
        }
    }

    fun deleteTables() =
            try {
                log.info("Deleting table $tableName")
                dynamoDbClient.deleteTable { it.tableName(tableName) }
                while (getTableStatus(tableName) == TableStatus.DELETING) Thread.sleep(500)
            } catch (e: ResourceNotFoundException) {
                // intentionally ignored
            }


    private fun waitUntilTableActive(tableName: String) {
        while (getTableStatus(tableName) != TableStatus.ACTIVE) Thread.sleep(500)
        log.info("Table $tableName is active")
    }

    private fun getTableStatus(tableName: String): TableStatus =
        dynamoDbClient.describeTable { it.tableName(tableName) }.table().tableStatus()!!

}