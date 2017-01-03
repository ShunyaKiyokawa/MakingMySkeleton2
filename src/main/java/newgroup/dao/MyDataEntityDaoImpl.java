package newgroup.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import newgroup.entity.MyDataEntity;

@Repository
public class MyDataEntityDaoImpl implements MyDataEntityDaoIF<MyDataEntity> {
	//MyDataEntityに対するDB操作
	private static final long serialVersionUID = 1L;

	private EntityManager entityManager;
	//Entitymanagerクラスを補完するためのフィールドを用意している

	public MyDataEntityDaoImpl(){
		super();
	}
	public MyDataEntityDaoImpl(EntityManager manager){
		entityManager = manager;
	}
	//EntityManagerはSpringDataJPAでDBアクセスを行う機能を提供する


	@Override
	public List<MyDataEntity> getAll() {
		Query query = entityManager.createQuery("from MyDataEntity");
		//SQLのクエリーに相当する機能をもつクラス
		//from MyDataEntityは、select * from MyDataEntityに相当する
		List<MyDataEntity> list = query.getResultList();
		//クエリーの結果をlistインスタンスとして取得する
		entityManager.close();
		return list;

	}
	@Override
	public MyDataEntity findById(long id) {
		return (MyDataEntity)entityManager.createQuery("from MyDataEntity where id = "
			+ id).getSingleResult();
	}

	@Override
	public List<MyDataEntity> findByName(String username) {
		return (List<MyDataEntity>)entityManager.createQuery("from MyDataEntity where username = "
			+ username).getResultList();
	}


	@Override
	public List<MyDataEntity> find(String fstr){
		List<MyDataEntity> list = null;
		Long fid = 0L;
		//0LのLはlongを意味する。つけないとintとして扱われ、longで宣言したidで矛盾が生じる
		try {
			fid = Long.parseLong(fstr);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
		}
		Query query = entityManager
				.createNamedQuery("findWithName")
				.setParameter("fusername", "%" + fstr + "%");
		//CreateNamedQueryメソッドで、MyDataEntityで定義されたクエリーコードを読みこみQueryインスタンスを作成する
		list = query.getResultList();
		return list;
	}
	/*
	@Override
	public List<MyDataEntity> find(String fstr){
		List<MyDataEntity> list = null;
		String qstr = "from MyDataEntity where id = :fid or username like :fusername or mail like :fmail";
		//idが:fidと一致する場合、またはusernameが:fusernameに似ている場合、またはmailが:fmailに似ている場合
		Long fid = 0L;
		//0LのLはlongを意味する。つけないとintとして扱われ、longで宣言したidで矛盾が生じる
		try {
			fid = Long.parseLong(fstr);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
		}
		Query query = entityManager.createQuery(qstr)
				.setParameter("fid", fid)
				.setParameter("fusername", "%" + fstr + "%")
				.setParameter("fmail", fstr + "@%");
		list = query.getResultList();
		return list;
	}
*/
	@Override
	public List<MyDataEntity> findByAge(int min, int max) {
		return (List<MyDataEntity>)entityManager
				.createNamedQuery("findByAge")
				.setParameter("min", min)
				.setParameter("max", max)
				.getResultList();
	}
}