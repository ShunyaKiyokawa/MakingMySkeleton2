package newgroup.dao;

import java.io.Serializable;
import java.util.List;

public interface MyDataEntityDaoIF<T> extends Serializable {
	public List<T> findByAge(int min, int max);
	//たぶん<T>でも動くだろうけど。呼び出すときはfindByAge(10, 20)みたいな感じ
	//コントローラーならIterable<MyData> list = dao.findByAge(10, 40);
	//DaoImplクラスからならcreateNamedQuery("findByAge")、setParameter("min", min), setParameter("max", max)
	public List<T> getAll(); //全エンティティの取得
	public T findById(long id); //ID番号を引数に指定しエンティティを検索し返す。エンティティ取得の基本
	public List<T> findByName(String username); //名前からエンティティを検索する
	public List<T> find(String fstr);
}