package com.backend.service.migration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class PostDataFactory {
  public List<PostData> posts() {
    List<PostData> ret = new ArrayList<>();
    PostData newPost1 = new PostData(1L, "title", "content");
    ret.add(newPost1);
    return ret;
  }
}
