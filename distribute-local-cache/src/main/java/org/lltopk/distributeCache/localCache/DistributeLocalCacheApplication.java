package org.lltopk.distributeCache.localCache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("org.lltopk.distributeCache.localCache.mapper")
@ComponentScan(value = "org.lltopk.distributeCache.**")
public class DistributeLocalCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(DistributeLocalCacheApplication.class, args);
	}

}
