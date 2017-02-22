package com.swiftpot.timetable;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteConcern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.WriteResultChecking;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         22-Feb-17 @ 3:22 PM
 */
@EnableAutoConfiguration
@Configuration
public class AppConfig {

    @Autowired
    MongoProperties mongoProperties;

    public
    @Bean
    Mongo mongo() throws Exception {
        String mongoClientUriFromProperties = mongoProperties.getUri();
        System.out.println("mongo client Uri from properties =" + mongoClientUriFromProperties);
        return new MongoClient(new MongoClientURI(mongoClientUriFromProperties));
    }

    public
    @Bean
    MongoTemplate mongoTemplate() throws Exception {
        String databaseName = mongoProperties.getDatabase();
        MongoTemplate mongoTemplate = new MongoTemplate(mongo(), databaseName);
        mongoTemplate.setWriteResultChecking(WriteResultChecking.EXCEPTION);
        mongoTemplate.setWriteConcernResolver(action -> WriteConcern.ACKNOWLEDGED);
        //set write result checking and writeconcern resolver.
        System.out.println("WriteResultChecking.EXCEPTION and WriteConcern.ACKNOWLEDGED set successfully");
        return mongoTemplate;
    }
}
