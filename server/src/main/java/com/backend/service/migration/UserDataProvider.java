package com.backend.service.migration;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserDataProvider implements DataProvider<UserData> {

  @Value("${admin.username}")
  private String ADMIN_USERNAME;

  @Value("${admin.password}")
  private String ADMIN_PASSWORD;

  @Override
  public List<UserData> provide() {
    List<UserData> ret = new ArrayList<>();
    ret.add(new UserData(ADMIN_USERNAME, ADMIN_PASSWORD));
    return ret;
  }

  public UserData provideAdminUserData() {
    return new UserData(ADMIN_USERNAME, ADMIN_PASSWORD);
  }
}
