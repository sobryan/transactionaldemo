package com.example.transactionaldemo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class NotifyingService {

  private final static Logger logger = LoggerFactory.getLogger(NotifyingService.class);

  private final JdbcTemplate jdbcTemplate;

  public NotifyingService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Transactional
  public void notify(String... persons) {
    for (String person : persons) {
      logger.info("Notifying " + person  + " of a booking...");
      jdbcTemplate.update("insert into NOTIFICATIONS(FIRST_NAME) values (?)", person);
    }
  }

  public void deleteAll() {
	  logger.info("Deleting all books");
	  jdbcTemplate.execute("delete from NOTIFICATIONS");
  }

  
  public List<String> findAllNotifications() {
    return jdbcTemplate.query("select FIRST_NAME from NOTIFICATIONS",
        (rs, rowNum) -> rs.getString("FIRST_NAME"));
  }

}