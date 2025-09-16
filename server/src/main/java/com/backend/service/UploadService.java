package com.backend.service;

import com.backend.model.Avatar;
import com.backend.repository.AvatarRepository;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadService {

  private final AvatarRepository avatarRepository;

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

    Path targetDir = this.rootPath.resolve(type);
    Files.createDirectories(targetDir);

    Path targetPath = targetDir
      .resolve(safeFilename)
      .toAbsolutePath()
      .normalize();

    if (!targetPath.startsWith(rootPath)) {
      throw new RuntimeException("Invalid destination path: " + targetPath);
    }

    try {
      var in = file.getInputStream();
      Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      log.error(safeFilename, e.getMessage());
    }

    Avatar avatar = new Avatar();
    avatar.setUserId(id);
    avatar.setUrl(type + "/" + filenamePath.toString());
    avatarRepository.save(avatar);

    log.info("Multipart file saved to: {}", targetPath);
  }
}
