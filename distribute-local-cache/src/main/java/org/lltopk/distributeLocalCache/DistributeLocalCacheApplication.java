package org.lltopk.distributeLocalCache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.lltopk.distributeLocalCache.mapper")
public class DistributeLocalCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(DistributeLocalCacheApplication.class, args);
	}

}
