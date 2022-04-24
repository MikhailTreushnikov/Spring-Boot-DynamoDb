package com.dynamoDb.dynamoDbTest.model

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id

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
@DynamoDBTable(tableName = "characters")
class Character {

    @Id
    @JsonIgnore
    @JvmField
    var id: CharacterId? = null

    @get:DynamoDBHashKey(attributeName = "playerId")
    var playerId: String?
        get() {
            return id?.playerId
        }
        set(value) {
            id = id ?: CharacterId()
            id?.playerId = value
        }

    @get:DynamoDBRangeKey(attributeName = "characterId")
    var characterId: String?
        get() {
            return id?.characterId
        }
        set(value) {
            id = id ?: CharacterId()
            id?.characterId = value
        }

    @DynamoDBAttribute
    var playerRegion: String? = null

    @DynamoDBAttribute
    var playerEmail: String? = null

    @DynamoDBAttribute
    var currentServer: String? = null

    @DynamoDBAttribute
    var race: String? = null

    @DynamoDBAttribute
    var classAttribute: String? = null

    @DynamoDBAttribute
    var health: String? = null

    @DynamoDBAttribute
    var mana: String? = null

    @DynamoDBAttribute
    var strength: String? = null

    @DynamoDBAttribute
    var speed: String? = null

    @DynamoDBAttribute
    var level: String? = null
}

data class CharacterId(
    @field:DynamoDBHashKey(attributeName = "playerId") var playerId: String? = null,
    @field:DynamoDBRangeKey(attributeName = "characterId") var characterId: String? = null,
)