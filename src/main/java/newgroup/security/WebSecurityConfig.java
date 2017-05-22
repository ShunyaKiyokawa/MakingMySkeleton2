// controllerやmainより下の階層、または同じ階層におくこと。
// loginページが表示されず、認証のダイアログが出てくる
// http://docs.spring.io/spring-security/site/docs/3.0.x/reference/el-access.html
// hasAnyRoleとかもある

package newgroup.security;

// ここで認証認可の設定をする

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.Getter;
import lombok.Setter;
import newgroup.services.UserAuthService;

// USER Role
@Configuration
@EnableWebSecurity
// @Order(1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	@Getter
	@Setter
	private UserAuthService userAuthService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//
		// http://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#el-access
		http.authorizeRequests().antMatchers("/nosecurity").permitAll() //
				// URLがマッチしたら誰でも見れる
				// .antMatchers("/IPAddress").hasIpAddress("169.254.188.224")//192.168.0/24
				.antMatchers("/admin/**").hasRole("ADMIN")
				// .hasRole("USER") // USER 権限のみアクセス可 認可
				// .permitAll()は全ユーザーアクセス可
				//
				// なお、html側でrole情報を送信するときはROLE_を付ける必要がある。このページでROLE_ADMINと指定すると、ROLE_が自動でつけられるので邪魔というエラーが出る
				.anyRequest().authenticated() // その他リクエストあるとここにリダイレクト
				.and().formLogin().loginPage("/login") // 認証ページ
				.permitAll() // 全員アクセスできないと認証ページの意味がない
				.defaultSuccessUrl("/", true) // 認証成功時の遷移先
				.and().rememberMe().tokenValiditySeconds(86400) // 1ヶ月（秒）
				.and().logout()
				// ログアウト後の遷移先
				.logoutSuccessUrl("/login")
				// セッションを破棄する
				.invalidateHttpSession(true)
				// ログアウト時に削除するクッキー名
				.deleteCookies("JSESSIONID", "remember-me").permitAll();
	}

	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// configureGlobalにしたら、Bad Credentialsエラーが発生したのでGlobalを外す
		// Service認証
		auth.inMemoryAuthentication().withUser("admin").password("password").roles("ADMIN");
		auth.userDetailsService(userAuthService)
			.passwordEncoder(passwordEncoder()); // サービス認証
		// DBからっぽの時を考えてインメモリでadmin認証を1つ用意。
		/*
		 * JDBC認証。ハッシュ化が難しい。 auth .jdbcAuthentication() .dataSource(dataSource)
		 * .authoritiesByUsernameQuery("select username as username, role as
		 * authority // from mydata where username = ?" )
		 * .usersByUsernameQuery("select username as username, password as
		 * password, // true as enabled from mydata where username = ?" )
		 * //JDBCでの認証。nameからusernameに名前をかえてから繋がるかは確認していない ;
		 * application.propertiesで指定されたdataSourceのデータベースにアクセスした後、
		 * mydataテーブルのnameを参照し、
		 * パスワードを比較している。asで置き換えてるのはspringSecurityが求める形に合わせるため
		 */
		/*
		 * インメモリ認証 auth .inMemoryAuthentication()
		 * .withUser("admin").password("password").roles("ADMIN") auth
		 * .inMemoryAuthentication()
		 * .withUser("user").password("password").roles("USER"); auth
		 * .inMemoryAuthentication()
		 * .withUser("admin").password("password").roles("ADMIN") ;
		 * //.withUser("admin").password("password").roles("ADMIN",
		 * "CREATEUSER); //複数できそう？ //scope globalでmemory保存
		 */
		
		
	}
	@Bean
	BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
}
