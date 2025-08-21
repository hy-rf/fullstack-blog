package com.backend.repository.cache;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCacheRepository<T, ID> implements CacheBaseRepository<T, ID> {
  private final Map<ID, T> store = new ConcurrentHashMap<>();

  @Override
  public Optional<T> findById(ID id) {
    T entry = store.get(id);
    return Optional.ofNullable(entry);
  }

  @Override
  public T save(ID id, T entity) {
    store.put(id, entity);
    return entity;
  }

  @Override
  public void deleteById(ID id) {
    store.remove(id);
  }
}
