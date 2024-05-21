package com.baeldung.ls;

import com.baeldung.ls.persistence.model.Project;
import com.baeldung.ls.persistence.repository.IProjectRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDate;
import java.util.random.RandomGenerator;

@SpringBootApplication
@ComponentScan(basePackages = "com.baeldung")
public class LsApp implements ApplicationRunner {
    IProjectRepository projectRepository;

    public LsApp(IProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public static void main(final String... args) {
        ConfigurableApplicationContext context = SpringApplication.run(LsApp.class, args);
        //abbiamo preso un bean
        //BeanA car = context.getBean(BeanA.class);

        //context.close();
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        /*projectRepository.save(new Project("P1", LocalDate.now()));
        projectRepository.save(new Project("P2", LocalDate.now()));
        projectRepository.save(new Project("P3", LocalDate.now()));*/
    }
}