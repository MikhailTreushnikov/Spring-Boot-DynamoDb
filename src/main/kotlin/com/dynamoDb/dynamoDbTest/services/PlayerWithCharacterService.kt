package com.dynamoDb.dynamoDbTest.services

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.BillingMode
import com.amazonaws.services.dynamodbv2.model.CreateGlobalSecondaryIndexAction
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndexUpdate
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement
import com.amazonaws.services.dynamodbv2.model.KeyType
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex
import com.amazonaws.services.dynamodbv2.model.Projection
import com.amazonaws.services.dynamodbv2.model.ProjectionType
import com.amazonaws.services.dynamodbv2.model.PutItemRequest
import com.amazonaws.services.dynamodbv2.model.QueryRequest
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType
import com.amazonaws.services.dynamodbv2.model.UpdateTableRequest
import org.slf4j.LoggerFactory
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
/**
 *type Player {          -------->  Item = {
 *id: string                          'playerId':      'string',    # partition key
 *region: string                      'characterId':   'string',    # sort key
 *server: string                      'playerRegion':  'string',
 *email: string                       'playerEmail':   'string',
 *}type Character {                   'currentServer': 'string',
 *id: string                          'race':          'string',
 *player: Player          ------->    'class':         'string',
 *race: string                        'health':        123,
 *class: string                       'mana':          123,
 *health: number                      'strength':      123,
 *mana: number                        'speed':         123,
 *strength: number                    'level':         10
 *speed: number                     }
 *level: number           ------->
 *}
 *
 * */
@Service
class PlayerWithCharacterService (
    private val dynamoDbClient: AmazonDynamoDB,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @EventListener(ContextRefreshedEvent::class)
    fun test() {
        createTableWithLSI()
//        putItem()
//        queryItems()
//        addGSIToTable()
    }

    /**
     * Creating table with Local Secondary Indexes (LSI)
     * */
    fun createTableWithLSI() {
        val result = try {
            dynamoDbClient.createTable(
                CreateTableRequest()
                    .withTableName("local-dev-characters")
                    .withAttributeDefinitions(
                        AttributeDefinition("playerId", ScalarAttributeType.S),
                        AttributeDefinition("characterId", ScalarAttributeType.S),
                        AttributeDefinition("currentServer", ScalarAttributeType.S)
                    )
                    .withKeySchema(
                        KeySchemaElement("playerId", KeyType.HASH),
                        KeySchemaElement("characterId", KeyType.RANGE),
                    )
                    .withBillingMode(BillingMode.PAY_PER_REQUEST)
                    .withLocalSecondaryIndexes(
                        LocalSecondaryIndex()
                            .withIndexName("server")
                            .withKeySchema(
                                mutableListOf(
                                    KeySchemaElement("playerId", KeyType.HASH),
                                    KeySchemaElement("currentServer", KeyType.RANGE)
                                )
                            )
                            .withProjection(Projection().withProjectionType(ProjectionType.KEYS_ONLY))
                    )
            )
        } catch (e: Exception) {
            log.error("cannot create table", e)
            throw e
        }
        log.info(result.toString())
    }

    /**
     * adding Global Secondary Indexes
     * */
    fun addGSIToTable() {
        val result = try {
            dynamoDbClient.updateTable(
                UpdateTableRequest()
                    .withTableName("local-dev-characters")
                    .withAttributeDefinitions(
                        AttributeDefinition("playerRegion", ScalarAttributeType.S),
                        AttributeDefinition("currentServer", ScalarAttributeType.S)
                    )
                    .withGlobalSecondaryIndexUpdates(
                        GlobalSecondaryIndexUpdate()
                            .withCreate(
                                CreateGlobalSecondaryIndexAction()
                                    .withIndexName("region")
                                    .withKeySchema(
                                        mutableListOf(
                                            KeySchemaElement("playerRegion", KeyType.HASH),
                                            KeySchemaElement("currentServer", KeyType.RANGE)
                                        )
                                    )
                                    .withProjection(Projection().withProjectionType(ProjectionType.ALL))
                            )
                    )
            )
        } catch (e: Exception) {
            log.error("cannot update table")
            throw e
        }
    }

    /**
     * Putting item to table
     * */
    fun putItem() {
        val result = try {
            dynamoDbClient.putItem(
                PutItemRequest()
                    .withTableName("local-dev-characters")
                    .withItem(
                        mapOf(
                            "playerId" to AttributeValue("7877e1b90fe2"),
                            "characterId" to AttributeValue("74477fae0c9f"),
                            "playerRegion" to AttributeValue("us"),
                            "playerEmail" to AttributeValue("foo@bar.com"),
                            "currentServer" to AttributeValue("srv01.us-east.bestrpg.com"),
                            "race" to AttributeValue("human"),
                            "class" to AttributeValue("knight"),
                            "health" to AttributeValue("9000"),
                            "mana" to AttributeValue("10"),
                            "strength" to AttributeValue("42"),
                            "speed" to AttributeValue("23"),
                            "level" to AttributeValue("7"),
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
     * Querying Items
     * */
    fun queryItems() {
        val result = try {
            dynamoDbClient.query(
                QueryRequest()
                    .withTableName("local-dev-characters")
                    .withIndexName("server")
                    .withKeyConditionExpression("playerId = :playerVal and currentServer = :srvVal")
                    .withExpressionAttributeValues(
                        mapOf(
                            ":playerVal" to AttributeValue("7877e1b90fe2"),
                            ":srvVal" to AttributeValue("srv01.us-east.bestrpg.com")
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