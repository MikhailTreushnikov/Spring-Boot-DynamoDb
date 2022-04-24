package com.dynamoDb.dynamoDbTest

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.ConversionSchemas
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.*
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverterFactory
import com.dynamoDb.dynamoDbTest.repositories.CharacterRepository
import org.socialsignin.spring.data.dynamodb.core.DynamoDBOperations
import org.socialsignin.spring.data.dynamodb.core.DynamoDBTemplate
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
@EnableDynamoDBRepositories(
    basePackageClasses = [CharacterRepository::class],
    dynamoDBOperationsRef = "dynamoDBOperations"
)
class DynamoDbConfiguration(
    @Value("\${spring.data.dynamodb.table-prefix}") private val tablePrefix: String,
    @Value("\${spring.data.dynamodb.endpoint:#{null}}") private val endpoint: String?,
    @Value("\${spring.data.dynamodb.local:false}") private val local: Boolean,
    @Value("\${spring.data.dynamodb.accesskey}") private val amazonAWSAccessKey: String?,
    @Value("\${spring.data.dynamodb.secretkey}") private val amazonAWSSecretKey: String?,
) {

    /**
     * create mapper with custom settings. Can use default mapper
     * */
    @Bean
    @Primary
    fun dynamoDBMapperConfig(): DynamoDBMapperConfig {
        return builder()
            .withSaveBehavior(SaveBehavior.UPDATE)
            .withConsistentReads(ConsistentReads.EVENTUAL)
            .withPaginationLoadingStrategy(PaginationLoadingStrategy.LAZY_LOADING)
            .withTableNameResolver(DefaultTableNameResolver.INSTANCE)
            .withBatchWriteRetryStrategy(DefaultBatchWriteRetryStrategy.INSTANCE)
            .withBatchLoadRetryStrategy(DefaultBatchLoadRetryStrategy.INSTANCE)
            .withTypeConverterFactory(DynamoDBTypeConverterFactory.standard())
            .withConversionSchema(ConversionSchemas.V2)
            .withTableNameOverride(TableNameOverride.withTableNamePrefix(tablePrefix))
            .build()
    }

    @Bean
    fun amazonDynamoDB(): AmazonDynamoDB? {
        val builder = AmazonDynamoDBClientBuilder.standard()
        if (local) {
            builder.withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(endpoint
                ?: "http://localhost:8000", "local"))
        }
        return builder
            .withCredentials(AWSStaticCredentialsProvider(amazonAWSCredentials()))
            .build()
    }

    @Bean
    @Primary
    fun dynamoDBMapper(): DynamoDBMapper {
        return DynamoDBMapper(amazonDynamoDB(), dynamoDBMapperConfig())
    }

    @Bean
    @Primary
    fun dynamoDBOperations(): DynamoDBOperations {
        return DynamoDBTemplate(amazonDynamoDB(), dynamoDBMapper(),dynamoDBMapperConfig())
    }

    @Bean
    fun amazonAWSCredentials(): AWSCredentials? {
        return BasicAWSCredentials(
            amazonAWSAccessKey, amazonAWSSecretKey)
    }
}