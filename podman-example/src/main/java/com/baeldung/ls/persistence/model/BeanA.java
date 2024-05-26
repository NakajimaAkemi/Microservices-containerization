package com.baeldung.ls.persistence.model;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;



public class BeanA {
    private static Logger log = LoggerFactory.getLogger(BeanA.class);

    @PostConstruct
    public void post() {
        log.info("@PostConstruct method is called.");
    }
}