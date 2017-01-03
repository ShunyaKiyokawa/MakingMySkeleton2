//ユーザー認証で使用している。WebSecurityConfig.javaから飛んでくる

package newgroup.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import newgroup.entity.MyDataEntity;
import newgroup.repository.MyDataRepository;

@Service

public class UserAuthService implements UserDetailsService{
	@Autowired
	@Getter
	@Setter
	private MyDataRepository mydatarepository;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    if (username == null || "".equals(username)) {
	      throw new UsernameNotFoundException("ユーザー名を入力してください");
	    }

	    MyDataEntity usernameInfo = mydatarepository.findByUsername(username);
	    if (usernameInfo == null) {
	      throw new UsernameNotFoundException("ユーザー名が登録されていません" + username);
	    }

	    return usernameInfo;
	  }
}
