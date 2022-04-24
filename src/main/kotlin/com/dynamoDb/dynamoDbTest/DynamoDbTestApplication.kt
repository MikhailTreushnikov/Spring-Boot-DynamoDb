package com.dynamoDb.dynamoDbTest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DynamoDbTestApplication

fun main(args: Array<String>) {
	runApplication<DynamoDbTestApplication>(*args)
}
