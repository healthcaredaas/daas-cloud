package cn.healthcaredaas.data.cloud;

import cn.healthcaredaas.data.cloud.web.rest.annotation.EnableGlobalUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**

 * @ClassName： FoundationSvcApplication.java
 * @Description:
 * @Author： chenpan
 * @Date：2024/4/11 11:14
 * @Modify：
 */
@EnableGlobalUser
@SpringBootApplication
public class FoundationSvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoundationSvcApplication.class, args);
    }
}
