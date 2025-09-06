package com.backend.service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class UploadService {

  @Value("${file.upload.path}")
  private String rawPath;

  private Path rootPath;

  @PostConstruct
  public void init() {
    rootPath = Path.of(rawPath).toAbsolutePath().normalize();
  }

  public void save(MultipartFile file, String type, Integer id)
    throws IOException {
    if (file == null || file.isEmpty()) {
      throw new RuntimeException("Uploaded file is empty or null.");
    }

    String originalFilename = file.getOriginalFilename();
    if (originalFilename == null || originalFilename.isBlank()) {
      throw new RuntimeException("Uploaded file has no original filename.");
    }

    Path filenamePath = Path.of(originalFilename).getFileName();
    String safeFilename = filenamePath.toString();

    safeFilename = id != null ? id + "-" + safeFilename : safeFilename;

    Path targetDir = this.rootPath.resolve(type);
    Files.createDirectories(targetDir);

    Path targetPath = targetDir
      .resolve(safeFilename)
      .toAbsolutePath()
      .normalize();

    if (!targetPath.startsWith(rootPath)) {
      throw new RuntimeException("Invalid destination path: " + targetPath);
    }

    try (var in = file.getInputStream()) {
      Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
    }

    log.info("Multipart file saved to: {}", targetPath);
  }
}
