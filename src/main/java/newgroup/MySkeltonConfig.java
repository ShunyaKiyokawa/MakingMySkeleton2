package newgroup;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import newgroup.bean.MyDataBean;

//@Configurationはアプリ起動時設定クラスとしてインスタンス化され
//記述されているBeanなどをアプリケーションに登録する

@Configuration
public class MySkeltonConfig {

	//Beanとして登録するインスタンスを返す
	@Bean
	MyDataBean myDataBean(){
		return new MyDataBean();
	}
}
