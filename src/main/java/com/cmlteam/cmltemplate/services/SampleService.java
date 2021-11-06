package com.cmlteam.cmltemplate.services;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class SampleService {
  private final JdbcTemplate jdbcTemplate;
  private final MongoTemplate mongoTemplate;

  @Autowired
  public SampleService(JdbcTemplate jdbcTemplate, MongoTemplate mongoTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.mongoTemplate = mongoTemplate;
  }

  public String getDbVersion() {
    return jdbcTemplate.queryForObject("select version();", String.class);
  }
  public String getMongoVersion() {
    return mongoTemplate.executeCommand(new Document("buildInfo",1)).get("version", String.class);
  }

  @PostConstruct
  public void test() {
//    System.out.println(getDbVersion());
//    System.out.println("MONGO: " + getMongoVersion());
  }
}
