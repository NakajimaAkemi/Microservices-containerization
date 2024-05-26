package com.baeldung.ls.config;

import com.baeldung.ls.persistence.repository.IProjectRepository;
import com.baeldung.ls.persistence.repository.impl.ProjectRepositoryImpl;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baeldung.ls.persistence.model.BeanA;
import com.baeldung.ls.persistence.model.BeanB;
import com.baeldung.ls.persistence.model.BeanC;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;

@Configuration
public class AppConfig {
    private static final Logger LOG = LoggerFactory.getLogger(AppConfig.class);
    @Autowired
    private Environment environment;

    @Bean
    public BeanA benA() {
        return new BeanA();
    }
    @PostConstruct
    private void postConstruct(){
        LOG.info("project prefix: {}", environment.getProperty("project.prefix"));
        LOG.info("project suffix: {}", environment.getProperty("project.suffix"));
    }
}