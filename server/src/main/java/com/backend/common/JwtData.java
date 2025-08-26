package com.backend.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtData {
    private Long userId;
    private String userName;
    private List<Long> roleIds;
    private List<String> roleNames;
}
