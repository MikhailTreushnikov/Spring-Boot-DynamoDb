package com.dynamoDb.dynamoDbTest.services

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.BillingMode
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest
import com.amazonaws.services.dynamodbv2.model.GetItemRequest
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement
import com.amazonaws.services.dynamodbv2.model.KeyType
import com.amazonaws.services.dynamodbv2.model.PutItemRequest
import com.amazonaws.services.dynamodbv2.model.QueryRequest
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType
import org.slf4j.LoggerFactory
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class CharacterService(
    private val dynamoDbClient: AmazonDynamoDB,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @EventListener(ContextRefreshedEvent::class)
    fun test() {
        // 1. create Table
//          createTable()
        // 2. putting incorrect item
//         putIncorrectItem()
        // 3. putting correct item
//         putCorrectItem()
        // 4. getting Item
//         getItem()
        // 5. getting incorrect item
//         getIncorrectItem()
        // 6. putting couple items
//         putCoupleItems()
        // 7. querying items
//        queryItems()
    }

    /**
     *  Creating table characters
     * {
     * "characterId":
     * "playerId":
     *  }
     *
     * */
    fun createTable() {
        val result = try {
            dynamoDbClient.createTable(
                CreateTableRequest()
                    .withTableName("local-dev-characters")
                    .withAttributeDefinitions(
                        AttributeDefinition("characterId", ScalarAttributeType.S),
                        AttributeDefinition("playerId", ScalarAttributeType.S),
                    )
                    .withKeySchema(
                        KeySchemaElement("playerId", KeyType.HASH),
                        KeySchemaElement("characterId", KeyType.RANGE),
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
     * Putting incorrect item to table
     * {
     *   "foo": "bar",
     * }
     *
     * */
    fun putIncorrectItem() {
        val result = try {
            dynamoDbClient.putItem(
                PutItemRequest()
                    .withTableName("local-dev-characters")
                    .withItem(mapOf("foo" to AttributeValue("bar")))
            )
        } catch (e: Exception) {
            log.error("cannot put item", e)
            throw e
        }
        log.info(result.attributes.toString())
    }

    /**
     * Putting item to table
     * {
     *   "characterId": "7877e1b90fe2",
     *   "playerId": "74477fae0c9f",
     *   "serverRegion": "us-east",
     *   "currentServer": "srv123.us-east.bestrpg.com",
     *   "race": "human",
     *   "class": "knight"
     * }
     *
     * */
    fun putCorrectItem() {
        val result = try {
            dynamoDbClient.putItem(
                PutItemRequest()
                    .withTableName("local-dev-characters")
                    .withItem(
                        mapOf(
                            "characterId" to AttributeValue("7877e1b90fe2"),
                            "playerId" to AttributeValue("74477fae0c9f"),
                            "serverRegion" to AttributeValue("us-east"),
                            "currentServer" to AttributeValue("srv123.us-east.bestrpg.com"),
                            "race" to AttributeValue("human"),
                            "class" to AttributeValue("knight")
                        )
                    )
            )
        } catch (e: Exception) {
            log.error("cannot put item", e)
            throw e
        }
        log.info(result.toString())
    }

    /**
     * Putting a couple items to table
     * */
    fun putCoupleItems() {
        try {
            dynamoDbClient.putItem(
                PutItemRequest()
                    .withTableName("local-dev-characters")
                    .withItem(
                        mapOf(
                            "characterId" to AttributeValue("char1"),
                            "playerId" to AttributeValue("player1"),
                        )
                    )
            )
            dynamoDbClient.putItem(
                PutItemRequest()
                    .withTableName("local-dev-characters")
                    .withItem(
                        mapOf(
                            "characterId" to AttributeValue("char2"),
                            "playerId" to AttributeValue("player1"),
                        )
                    )
            )
            dynamoDbClient.putItem(
                PutItemRequest()
                    .withTableName("local-dev-characters")
                    .withItem(
                        mapOf(
                            "characterId" to AttributeValue("char3"),
                            "playerId" to AttributeValue("player2"),
                        )
                    )
            )
            dynamoDbClient.putItem(
                PutItemRequest()
                    .withTableName("local-dev-characters")
                    .withItem(
                        mapOf(
                            "characterId" to AttributeValue("char4"),
                            "playerId" to AttributeValue("player2"),
                        )
                    )
            )
            dynamoDbClient.putItem(
                PutItemRequest()
                    .withTableName("local-dev-characters")
                    .withItem(
                        mapOf(
                            "characterId" to AttributeValue("char5"),
                            "playerId" to AttributeValue("player2"),
                        )
                    )
            )
        } catch (e: Exception) {
            log.error("cannot put item", e)
            throw e
        }
    }

    /**
     * Getting item (SELECT FROM)
     * */
    fun getItem() {
        val result = try {
            dynamoDbClient.getItem(
                GetItemRequest()
                    .withTableName("local-dev-characters")
                    .withKey(
                        mapOf(
                            "characterId" to AttributeValue("7877e1b90fe2"),
                            "playerId" to AttributeValue("74477fae0c9f")
                        )
                    )
                    .withConsistentRead(true)
            )
        } catch (e: Exception) {
            log.error("cannot get item", e)
            throw e
        }
        log.info(result.item.toString())
    }

    /**
     * Getting Incorrect item (SELECT FROM) because sort key is not defined
     * */
    fun getIncorrectItem() {
        val result = try {
            dynamoDbClient.getItem(
                GetItemRequest()
                    .withTableName("local-dev-characters")
                    .withKey(
                        mapOf(
                            "playerId" to AttributeValue("74477fae0c9f")
                        )
                    )
            )
        } catch (e: Exception) {
            log.error("cannot get item", e)
            throw e
        }
        log.info(result.item.toString())
    }

    /**
     * Querying Items
     * */
    fun queryItems() {
        val result = try {
            dynamoDbClient.query(
                QueryRequest()
                    .withTableName("local-dev-characters")
                    .withKeyConditionExpression("playerId = :playerVal")
                    .withExpressionAttributeValues(
                        mapOf(
                            ":playerVal" to AttributeValue("player1")
                        )
                    )
            )
        } catch (e: Exception) {
            log.error("cannot get item", e)
            throw e
        }
        result.items.forEach {
            log.info(it.toString())
        }
    }
}