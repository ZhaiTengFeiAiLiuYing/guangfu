package com.pb;
import com.pb.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tk.mybatis.spring.annotation.MapperScan;
@SpringBootApplication
@MapperScan("com.pb.mapper")
@EnableScheduling
public class GuangfuApplication {
	public static void main(String[] args) {
		SpringApplication.run(GuangfuApplication.class);

	}
	/**
	 * 雪花算法用于生成id
	 * @return IdWorker
	 */
	@Bean
	public IdWorker idworker() {
		return new IdWorker();
	}

	/**
	 * 把springsecurity的加密组件声明为bean交给spring管理
	 * @return BCryptPasswordEncoder
	 */
	@Bean
	public BCryptPasswordEncoder bcryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
