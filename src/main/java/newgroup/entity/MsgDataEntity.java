//MsgDataEntityはユーザーのメッセージを管理するクラス

package newgroup.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotEmpty;

//Xmlで書き出すアノテーション。
@XmlRootElement
@Entity
@Table(name = "msgdata")
public class MsgDataEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	@NotNull
	private long id;

	@Column
	private String title;

	@Column(nullable = false)
	@NotEmpty
	private String message;

	@ManyToOne
	private MyDataEntity mydata;
	//MsgDataEntityは1人のメンバーがいくつでもメッセージを投稿できるため
	//1つのmydataに対して複数のMsgDataEntityを持つということ


	public MsgDataEntity() {
		super();
		mydata = new MyDataEntity();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MyDataEntity getMydata() {
		return mydata;
	}

	public void setMydata(MyDataEntity mydata) {
		this.mydata = mydata;
	}


}