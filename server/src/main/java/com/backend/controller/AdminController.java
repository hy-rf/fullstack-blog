package com.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.service.migration.MigrationService;

@RestController
@RequestMapping("/admin")
public class AdminController {

  private final MigrationService migrationService;

  public AdminController(MigrationService migrationService) {
    this.migrationService = migrationService;
  }


  /**
   * Not using
   * 
   * @return
   */
  @GetMapping("/seed-posts")
  public String seedPosts() {
    return migrationService.seedPosts();
  }

}
