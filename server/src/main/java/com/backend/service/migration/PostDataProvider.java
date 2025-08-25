package com.backend.service.migration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class PostDataProvider implements DataProvider<PostData> {

  @Override
  public List<PostData> provide() {
    List<PostData> ret = new ArrayList<>();
    ret.add(new PostData(1L, "title", "content"));
    return ret;
  }
}