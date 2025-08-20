package com.backend.config;

import org.springframework.stereotype.Component;

import com.backend.service.migration.MigrationService;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DataConfig {

  @Autowired
  private MigrationService migrationService;

  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationReady() {
    String result = migrationService.checkAndAddDataIfUserExists();
    log.info("Startup migration result: {}", result);
  }
}