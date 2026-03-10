package cn.healthcaredaas.data.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**

 * @ClassName： AuthSvcApplication.java
 * @Author： chenpan
 * @Date：2024/12/7 16:22
 * @Modify：
 */
@SpringBootApplication
@EnableFeignClients
public class AuthSvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthSvcApplication.class, args);
    }
}
