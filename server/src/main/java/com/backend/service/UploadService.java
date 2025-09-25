package com.backend.service;

import com.backend.model.Avatar;
import com.backend.model.Post;
import com.backend.model.PostImage;
import com.backend.repository.AvatarRepository;
import com.backend.repository.PostImageRepository;
import com.backend.repository.PostRepository;
import jakarta.annotation.PostConstruct;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
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
  private final PostRepository postRepository;

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

    String safeFilename = type.equals("post_image")
      ? id.toString() + "_" + originalFilename
      : originalFilename;

    Path targetPath = this.rootPath.resolve(type)
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
      avatar.setUrl(type + "/" + safeFilename);
      avatarRepository.save(avatar);
    }

    if (type.equals("post_image")) {
      Post post = postRepository.findById(id).get();
      PostImage postImage = new PostImage();
      postImage.setPost(post);
      postImage.setUrl(type + "/" + safeFilename);
      postImageRepository.save(postImage);
    }

    log.info("Multipart file saved to: {}", targetPath);
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
    String originalFilename =
      type + "_" + id.toString() + LocalDateTime.now().toString();

    String safeFilename = originalFilename + "." + extension;

    Path targetPath = this.rootPath.resolve(type)
      .resolve(safeFilename)
      .toAbsolutePath()
      .normalize();

    if (!targetPath.startsWith(rootPath)) {
      throw new RuntimeException("Invalid destination path: " + targetPath);
    }

    try {
      FileOutputStream fos = new FileOutputStream(targetPath.toString());
      fos.write(file);
      fos.close();
    } catch (IOException e) {
      log.error(safeFilename, e);
      return;
    }

    if (type.equals("avatar")) {
      Avatar avatar = new Avatar();
      avatar.setUserId(id);
      avatar.setUrl(type + "/" + safeFilename);
      avatarRepository.save(avatar);
    }

    if (type.equals("post_image")) {
      Post post = postRepository.findById(id).get();
      PostImage postImage = new PostImage();
      postImage.setPost(post);
      postImage.setUrl(type + "/" + safeFilename);
      postImageRepository.save(postImage);
    }

    log.info("Multipart file saved to: {}", targetPath);
  }
}
