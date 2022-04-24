package com.dynamoDb.dynamoDbTest.services

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.model.AttributeAction
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate
import com.amazonaws.services.dynamodbv2.model.BillingMode
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest
import com.amazonaws.services.dynamodbv2.model.GetItemRequest
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement
import com.amazonaws.services.dynamodbv2.model.KeyType
import com.amazonaws.services.dynamodbv2.model.PutItemRequest
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType
import com.amazonaws.services.dynamodbv2.model.ScanRequest
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest
import org.slf4j.LoggerFactory
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class ShoppingListService(
    private val dynamoDbClient: AmazonDynamoDB,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @EventListener(ContextRefreshedEvent::class)
    fun test() {
        // 1. create Table
//         createTableWithoutSortKey()
        // 2. putting couple items
//         putCoupleItems()
        // 3. getting all items
//         scanItems()
        // 4. replacing via putItem
//         replaceItemViaPutOperation()
        // 5. updating items
//         updateItem()
        // 6. getting one attribute from Item
//         gettingOneAttributeFromItem()
    }

    /**
     *  Creating table shopping list
     * {
     * "good"
     *  }
     *
     * */
    fun createTableWithoutSortKey() {
        val result = try {
            dynamoDbClient.createTable(
                CreateTableRequest()
                    .withTableName("local-dev-shoppingList")
                    .withAttributeDefinitions(
                        AttributeDefinition("good", ScalarAttributeType.S),
                    )
                    .withKeySchema(
                        KeySchemaElement("good", KeyType.HASH),
                        // here might be sort key
                    )
                    .withBillingMode(BillingMode.PAY_PER_REQUEST)
            )
        } catch (e: Exception) {
            log.error("cannot create table", e)
            throw e
        }
        log.info(result.toString())
    }

    /**
     * Putting couple items to table
     *
     * */
    fun putCoupleItems() {
        try {
            dynamoDbClient.putItem(
                PutItemRequest()
                    .withTableName("local-dev-shoppingList")
                    .withItem(
                        mapOf(
                            "good" to AttributeValue("apple"),
                            "number" to AttributeValue("10"),
                            "bought" to AttributeValue("False"),
                        )
                    )
            )

            dynamoDbClient.putItem(
                PutItemRequest()
                    .withTableName("local-dev-shoppingList")
                    .withItem(
                        mapOf(
                            "good" to AttributeValue("tomato"),
                            "number" to AttributeValue("7"),
                            "bought" to AttributeValue("False"),
                        )
                    )
            )

            dynamoDbClient.putItem(
                PutItemRequest()
                    .withTableName("local-dev-shoppingList")
                    .withItem(
                        mapOf(
                            "good" to AttributeValue("cucumber"),
                            "number" to AttributeValue("1"),
                            "bought" to AttributeValue("False"),
                        )
                    )
            )

            dynamoDbClient.putItem(
                PutItemRequest()
                    .withTableName("local-dev-shoppingList")
                    .withItem(
                        mapOf(
                            "good" to AttributeValue("orange"),
                            "number" to AttributeValue("10"),
                            "bought" to AttributeValue("False"),
                        )
                    )
            )
        } catch (e: Exception) {
            log.error("cannot put item", e)
            throw e
        }
    }

    /**
     * getting items via scan
     * */
    fun scanItems() {
    // it's very expensive and slow operation, don't use it in real life
        val result = try {
            dynamoDbClient.scan(
                ScanRequest()
                    .withTableName("local-dev-shoppingList")
            )
        } catch (e: Exception) {
            log.error("cannot perform scan operation")
            throw e
        }
        result.items.forEach {
            log.info(it.toString())
        }
    }

    /**
     * replacing item via put operation
     * */
    fun replaceItemViaPutOperation() {
        val result = try {
            dynamoDbClient.putItem(
                PutItemRequest()
                    .withTableName("local-dev-shoppingList")
                    .withItem(
                        mapOf(
                            "good" to AttributeValue("orange"),
                            "bought" to AttributeValue("true")
                        )
                    )
            )
        } catch (e: Exception) {
            log.error("cannot perform putItem()", e)
            throw e
        }
        // scan to see how it looks like after
        scanItems()
    }

    /**
     * updating Items
     * */
    fun updateItem() {
        val result = try {
            dynamoDbClient.updateItem(
                UpdateItemRequest()
                    .withTableName("local-dev-shoppingList")
                    .withKey(mapOf("good" to AttributeValue("orange")))
//                    .withUpdateExpression("SET bought=10")
                    .withAttributeUpdates(
                        mapOf(
                            "number" to AttributeValueUpdate(AttributeValue("10"),AttributeAction.PUT),
                            "bought" to AttributeValueUpdate(AttributeValue("true"),AttributeAction.PUT)
                        )
                    )
            )
        } catch (e: Exception) {
            log.error("cannot perform putItem()", e)
            throw e
        }
        // scan to see how it looks like after
        scanItems()
    }

    /**
     * getting definite attribute from Item
     * */
    fun gettingOneAttributeFromItem() {
        val result = try {
            dynamoDbClient.getItem(
                GetItemRequest()
                    .withTableName("local-dev-shoppingList")
                    .withKey(mapOf("good" to AttributeValue("orange")))
//                    .withAttributesToGet("bought")
                    .withProjectionExpression("bought")
            )
        } catch (e: Exception) {
            log.error("cannot perform gettingOneAttributeFromItem()", e)
            throw e
        }
        log.info(result.item.toString())
    }
}