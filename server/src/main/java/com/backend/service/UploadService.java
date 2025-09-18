package com.backend.service;

import com.backend.model.Avatar;
import com.backend.model.PostImage;
import com.backend.repository.AvatarRepository;
import com.backend.repository.PostImageRepository;
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
  private final PostImageRepository postImageRepository;

  /**
   * Base raw path of root directory where files being stored
   */
  @Value("${file.upload.path}")
  private String rawPath;

  private Path rootPath;

  @PostConstruct
  public void init() {
    rootPath = Path.of(rawPath).toAbsolutePath().normalize();
    log.info(String.format("Initialized rootPath: %s", rawPath.toString()));
  }

  public void save(MultipartFile file, String type, Integer id)
    throws IOException {
    String originalFilename = file.getOriginalFilename();

    Path filenamePath = Path.of(originalFilename).getFileName();
    String safeFilename = type.equals("post_image")
      ? id.toString() + "_" + filenamePath.toString()
      : filenamePath.toString();

    // this make target dir rootpath/avatar or rootpath/post_image...
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
      log.error(safeFilename, e);
      return;
    }

    if (type.equals("avatar")) {
      Avatar avatar = new Avatar();
      avatar.setUserId(id);
      avatar.setUrl(type + "/" + filenamePath.toString());
      avatarRepository.save(avatar);
    }

    if (type.equals("post_image")) {
      PostImage postImage = new PostImage();
      postImage.setPostId(id);
      postImage.setUrl(type + "/" + safeFilename);
      postImageRepository.save(postImage);
    }

    log.info("Multipart file saved to: {}", targetPath);
  }
}
