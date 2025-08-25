package com.backend.service.migration;

import java.util.List;

public interface DataProvider<T> {
  List<T> provide();
}