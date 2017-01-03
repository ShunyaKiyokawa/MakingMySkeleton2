//IDからデータを取得し、テーブルのタグの形にまとめ直して取り出す機能
//@componentも@serviceでもないPOJOなので、アプリケーション内からBeanとしてこのまま使えない
//MySkeltonConfigでBeanとしてアプリケーションに登録している

package newgroup.bean;

import org.springframework.beans.factory.annotation.Autowired;

import newgroup.entity.MyDataEntity;
import newgroup.repository.MyDataRepository;

public class MyDataBean {

	@Autowired
	MyDataRepository repository;

	public String getTableTagById(Long id){
		MyDataEntity data = repository.findOne(id);
		//findOneはJpaRepositoryクラスにあるメソッドで
		//引数に指定したIDのエンティティ1つだけ返す
		String result = "<tr><td>" + data.getUsername()
				+ "</td><td>" + data.getMail() +
				"</td><td>" + data.getAge() +
				"</td><td>" + data.getMemo() +
				"</td></tr>";
		return result;
	}

}