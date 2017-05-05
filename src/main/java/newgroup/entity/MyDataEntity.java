//entitypackageはcontrollerより下の階層に配置すること。上の階層に置くとJuniterror当たりが出てきて動かない。
//データベースのエンティティを格納

package newgroup.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import newgroup.phoneVaridation.Phone;

//JPAの管理化にエンティティクラスとしておかれる

@NamedQueries({ @NamedQuery(name = "findWithName", query = "from MyDataEntity where username like :fusername"),
		@NamedQuery(name = "findByAge", query = "from MyDataEntity where age > :min and age < :max"),
		@NamedQuery(name = "getAll", query = "from MyDataEntity"), })

// XmlRootElementはXMLで返すためにつけている。ただ、機能していない。原因不明。似た機能があると動かないらしい。
// jackson.dataformatをpomから消すと表示変わるからここが原因？
// 公式ではJAXB（JDK）を使うことを推奨しているがプラグイン必要かも
// @XmlRootElement
@Data
@Entity
@Table(name = "mydata") // mysql等のDBのテーブル名がこれになる（勝手に作られる）
public class MyDataEntity implements UserDetails {
	// public enum Authority {ROLE_USER, ROLE_ADMIN};

	// MsgDataEntityとの紐づけ。MyDataEntity（ユーザー）ごとにメッセージは複数。
	// @OneToMany(cascade=CascadeType.ALL)roleがない、みたいなエラーが出たのでonetomanyとfetchを追加

	// MsgDataとのリレーションDBにするときは下記コメントアウトをはずす。
	@OneToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)

	@Column(nullable = true)
	private List<MsgDataEntity> msgdatas;

	public List<MsgDataEntity> getMsgdatas() {
		return msgdatas;
	}

	public void setMsgdatas(List<MsgDataEntity> msgdatas) {
		this.msgdatas = msgdatas;
	}

	/*
	 * @Bean public Filter characterEncodingFilter() { CharacterEncodingFilter
	 * filter = new CharacterEncodingFilter(); filter.setEncoding("UTF-8");
	 * filter.setForceEncoding(true); return filter; //
	 * //文字コード指定。データベースはshiftjisで受け取っているが。。。 }
	 */

	@Id // プライマリキー
	@GeneratedValue(strategy = GenerationType.AUTO) // 値は自動生成
	@Column // フィールドになる箇所はすべて必ずgetterとsetterを下部に書くこと
	@NotNull
	// この辺でバリデーション。引っかかったらtemplatesの下のValidationMessages.propertiesが呼ばれる
	private long id;

	@Column(length = 50, nullable = false)
	@NotEmpty
	private String username; // ちなみにUserDetailsがusernameを要求するので、この値は変えられない

	@JsonIgnore // passwordみたいな出したくないのにつけろ
	@Column(nullable = false)
	@Size(min = 5, max = 16) // 文字数5文字以上最大16文字
	private String password;

	// @Column(nullable = true)
	// // @Pattern(regexp = "男|女|その他")
	// private String gender;

	@Column(length = 200, nullable = true)
	@Email
	private String mail;

	@Column(nullable = true)
	@Min(value = 0)
	@Max(value = 200)
	private Integer age;

	@Column(nullable = true)
	@Phone(onlyNumber = true)
	private String memo;

	@Column(nullable = false)
	@NotEmpty
	private String role;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	// public String getGender() {
	// return gender;
	// }
	//
	// public void setGender(String gender) {
	// this.gender = gender;
	// }

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(role.toString()));
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO 自動生成されたメソッド・スタブ
		// defaultがfalseだがユーザーがロックされているエラーが出たのでひとまずtrueにする
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO 自動生成されたメソッド・スタブ
		// defaultがfalseだがユーザーがロックされているエラーが出たのでひとまずtrueにする
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO 自動生成されたメソッド・スタブ
		// defaultがfalseだがユーザーがロックされているエラーが出たのでひとまずtrueにする
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO 自動生成されたメソッド・スタブ
		// defaultがfalseだがユーザーがロックされているエラーが出たのでひとまずtrueにする
		return true;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}