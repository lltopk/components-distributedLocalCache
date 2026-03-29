package org.lltopk.distributeRedisCache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.lltopk.distributeRedisCache.mapper")
public class DistributeRedisCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(DistributeRedisCacheApplication.class, args);
	}

}
