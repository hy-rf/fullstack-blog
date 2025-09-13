package com.backend.mapper;

import com.backend.controller.dto.user.UserBasicDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
  UserBasicDto selectBasicById(Integer id);
  UserBasicDto selectAll(Integer offset, Integer limit);
}
