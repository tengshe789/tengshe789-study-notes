package tech.tengshe789.ConfigurationCenter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @program: xbrl-root
 * @description: 中心微服务配置入口
 * @doc：https://springcloud.cc/spring-cloud-config.html
 * @author: tEngSHe789
 * @create: 2018-11-26 17:07
 **/
@EnableConfigServer
@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class ConfigServerApplication implements CommandLineRunner{
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }

	@Override
	public void run(String... args) {
		log.info(">>>>>>>>>>>>>>> xbrl 程序 配置中心功能 启动完成<<<<<<<<<<<<<");
	}
}

