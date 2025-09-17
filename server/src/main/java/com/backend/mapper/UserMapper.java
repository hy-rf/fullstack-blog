package com.backend.mapper;

import com.backend.controller.dto.user.UserBasicDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
  UserBasicDto selectBasicById(Integer id);
  List<UserBasicDto> selectAll(Integer offset, Integer limit);
}
