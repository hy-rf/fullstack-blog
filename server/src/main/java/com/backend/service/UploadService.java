package com.backend.service;

import com.backend.dao.Avatar;
import com.backend.dao.AvatarRepository;
import com.backend.dao.PostImage;
import com.backend.dao.PostImageRepository;
import com.backend.model.Post;
import com.backend.repository.JpaPostRepository;
import jakarta.annotation.PostConstruct;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadService {

  private final AvatarRepository avatarRepository;
  private final PostImageRepository postImageRepository;
  private final JpaPostRepository postRepository;

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

  /**
   * Standardized upload service
   * @param file
   * @param type
   * @param id
   * @param extension
   * @throws IOException
   */
  public void save(byte[] file, String type, Integer id, String extension)
    throws IOException {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
      "yyyyMMdd_HHmmss"
    );
    String originalFilename =
      type +
      "_" +
      id.toString() +
      "_" +
      LocalDateTime.now().format(dateTimeFormatter);
    String safeFilename = originalFilename + "." + extension;

    Path targetPath = this.rootPath.resolve(safeFilename)
      .toAbsolutePath()
      .normalize();

    if (!targetPath.startsWith(rootPath)) {
      throw new RuntimeException("Invalid destination path: " + targetPath);
    }

    FileOutputStream fos = new FileOutputStream(targetPath.toString());
    try {
      fos.write(file);
    } catch (IOException e) {
      log.error(safeFilename, e);
      return;
    } finally {
      fos.close();
    }

    if (type.equals("avatar")) {
      Avatar avatar = new Avatar();
      avatar.setUserId(id);
      avatar.setUrl(safeFilename);
      avatarRepository.save(avatar);
    }

    if (type.equals("post_image")) {
      PostImage postImage = new PostImage();
      postImage.setPostId(id);
      postImage.setUrl(safeFilename);
      postImageRepository.save(postImage);
    }

    log.info("Multipart file saved to: {}", targetPath);
  }
}
