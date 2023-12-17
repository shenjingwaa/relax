package com.relax.relax;

import com.relax.relax.common.annotation.EnableRelax;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRelax
@SpringBootApplication
public class RelaxApplication {

    public static void main(String[] args) {
        SpringApplication.run(RelaxApplication.class, args);
    }

}
