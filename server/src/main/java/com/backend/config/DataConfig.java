package com.backend.config;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan("com.backend.mapper")
public class DataConfig {

  @Bean
  public SqlSessionFactory sqlSessionFactory(DataSource dataSource)
    throws Exception {
    SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    sessionFactory.setDataSource(dataSource);

    PathMatchingResourcePatternResolver resolver =
      new PathMatchingResourcePatternResolver();
    sessionFactory.setMapperLocations(
      resolver.getResources("classpath:mappers/*.xml")
    );

    return sessionFactory.getObject();
  }
}
