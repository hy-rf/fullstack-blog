package com.backend.repository;

import com.backend.controller.dto.PostSummary;
import com.backend.dao.PostRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PostRepositoryTest {

  @Mock
  private PostRepository postRepository;

  @BeforeEach
  public void setUp() {
    // Initialize mocks
    MockitoAnnotations.openMocks(this);
    System.out.println("OK");
  }

  @Test
  public void get() {
    Runtime runtime = Runtime.getRuntime();
    Long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
    List<PostSummary> postSummaries =
      postRepository.findAllByPostSummariesAndOffset(0, 1000000);
    System.out.println(postSummaries);
    Long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
    System.out.println(Long.toString(runtime.totalMemory()));
    System.out.println(Long.toString(memoryBefore));
    System.out.println(Long.toString(memoryAfter));
  }
}
