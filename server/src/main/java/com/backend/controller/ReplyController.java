package com.backend.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReplyController {
  private String sql = """
            SELECT r.id,
            r.content,
            r.post_id,
            r.reply_id,
            r.created_at,
            r.updated_at,
            u.id as author_id,
            u.username,
            STRING_AGG(rs.name, ', ') AS user_role_name_list
            FROM reply r
      LEFT JOIN users u ON r.author_id = u.id
      LEFT JOIN user_roles ur ON u.id = ur.user_id
                      LEFT JOIN roles rs ON ur.role_id = rs.id
                      WHERE r.post_id=1
                      GROUP BY r.id, u.id;
            """;
}
