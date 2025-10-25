package com.backend.repository;

import com.backend.model.Avatar;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAvatarRepository extends BaseRepository<Avatar, Integer> {}
