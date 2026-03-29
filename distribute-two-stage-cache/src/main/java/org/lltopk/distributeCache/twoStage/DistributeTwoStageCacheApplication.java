package org.lltopk.distributeCache.twoStage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("org.lltopk.distributeCache.twoStage.mapper")
@ComponentScan(value = "org.lltopk.distributeCache.**")
public class DistributeTwoStageCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(DistributeTwoStageCacheApplication.class, args);
	}

}
