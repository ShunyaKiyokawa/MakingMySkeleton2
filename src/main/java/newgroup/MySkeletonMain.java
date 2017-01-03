package newgroup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * 下記3つをSpringBootApplicationアノテーションでまとめている
@Configuration
@EnableAutoConfiguration
@ComponentScan
 */

@SpringBootApplication
public class MySkeletonMain {

	public static void main(String[] args) {
		//SpringApplication.run(MySkeletonMain.class, args);
		SpringApplication.run(MySkeletonMain.class, new String[]{"100"});
		//argsを書き換えたのはMySampleBeanコンポーネントに引き渡すため。カウンターmax100
	}
}
