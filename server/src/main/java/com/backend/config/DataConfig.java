package com.backend.config;

import org.springframework.stereotype.Component;

import com.backend.service.migration.MigrationService;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DataConfig {

  private final MigrationService migrationService;

  public DataConfig(MigrationService migrationService) {
    this.migrationService = migrationService;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationReady() {
    String result = migrationService.checkAndAddDataIfUserExists();
    log.info("Startup migration result: {}", result);
  }
}