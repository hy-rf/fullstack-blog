package com.backend.service;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UploadService {

  @Value("${file.upload.path}")
  private String rawPath;

  private Path rootPath;

  @PostConstruct
  public void init() {
    try {
      log.info("Initializing upload path with rawPath: {}", rawPath);
      if (rawPath == null || rawPath.isBlank()) {
        throw new RuntimeException(
          "The 'file.upload.path' property is not set or is empty."
        );
      }
      // Use Path.of directly for file paths
      rootPath = Path.of(rawPath).toAbsolutePath().normalize();
      Files.createDirectories(rootPath); // Ensure the directory exists
      log.info("Upload path initialized to: {}", rootPath);
    } catch (Exception e) {
      throw new RuntimeException("Failed to initialize upload path", e);
    }
  }

  public void save(File file, String type, Integer id) throws IOException {
    if (!file.exists()) {
      throw new RuntimeException(
        "The file does not exist: " + file.getAbsolutePath()
      );
    }

    Path targetDir = this.rootPath.resolve(type);
    Files.createDirectories(targetDir); // Ensure the subdirectory exists

    Path targetPath = targetDir.resolve(file.getName());
    Files.copy(file.toPath(), targetPath); // Copy the file to the target directory

    log.info("File saved to: {}", targetPath);
  }
}
