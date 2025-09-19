package com.backend.repository;

import com.backend.repository.dto.SaveFileResult;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository {
  public SaveFileResult saveFile();
}
