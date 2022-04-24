package com.dynamoDb.dynamoDbTest.controllers

import com.dynamoDb.dynamoDbTest.model.Character
import com.dynamoDb.dynamoDbTest.model.CharacterId
import com.dynamoDb.dynamoDbTest.repositories.CharacterRepository
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/character")
class CharacterController(
    private val characterRepository: CharacterRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @GetMapping("/{playerId}")
    fun getCharacter(@PathVariable playerId: CharacterId): Character {
        return characterRepository.findById(playerId).orElseThrow { RuntimeException("incorrectPlayerId") }
    }

    @GetMapping("/findByRegion/{playerRegion}")
    fun getCharactersByRegion(@PathVariable playerRegion: String): List<Character> {
        return characterRepository.findAllByPlayerRegion(playerRegion)
    }

    @PostMapping
    fun createCharacter(@RequestBody character: Character): Character {
        return try {
            characterRepository.save(character)
        } catch (e: Exception) {
            log.error("cannot perform action", e)
            throw e
        }
    }

    @GetMapping
    fun getAllCharacters() : MutableIterable<Character> {
        return characterRepository.findAll()
    }
}