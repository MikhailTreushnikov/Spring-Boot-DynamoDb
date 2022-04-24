package com.dynamoDb.dynamoDbTest.repositories

import com.dynamoDb.dynamoDbTest.model.Character
import com.dynamoDb.dynamoDbTest.model.CharacterId
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@EnableScan
@Repository
interface CharacterRepository : CrudRepository<Character, CharacterId> {

    fun findAllByPlayerRegion(playerRegion: String): List<Character>
}