package com.wynnteo.ordermgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients(basePackages = "com.wynnteo.ordermgmt.feignclient")
//@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class OrdermgmtApplication {

  public static void main(String[] args) {
    SpringApplication.run(OrdermgmtApplication.class, args);
  }
}
