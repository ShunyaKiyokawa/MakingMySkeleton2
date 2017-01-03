//RestControllerから飛んできた処理を行うサービス

package newgroup.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import newgroup.entity.MyDataEntity;
import newgroup.repository.MyDataRepository;


//サービス登録アノテーション
@Service
public class MyDataService {
	private static final int PAGE_SIZE = 3; // １ページあたりのエンティティ数

	//EntityManagerのBeanを自動的に割り当てるpersistenceContextアノテーション
	@PersistenceContext
	private EntityManager entityManager;

	public List<MyDataEntity> getAll() {
		return (List<MyDataEntity>) entityManager.createQuery("from MyDataEntity").getResultList();
	}

	//idでエンティティを取得
	public MyDataEntity getId(int num) {
		return (MyDataEntity)entityManager
				.createQuery("from MyDataEntity where id = " + num)
				.getSingleResult();
	}

	//usernameフィールドのテキスト検索でエンティティ取得
	public List<MyDataEntity> findUsername(String fstr) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MyDataEntity> query = builder.createQuery(MyDataEntity.class);
		Root<MyDataEntity> root = query.from(MyDataEntity.class);
		query.select(root).where(builder.equal(root.get("username"), fstr));
		List<MyDataEntity> list = null;
		list = (List<MyDataEntity>) entityManager.createQuery(query).getResultList();
		return list;
	}

//PageNation
	@Autowired
	MyDataRepository repository;

	public Page<MyDataEntity> getMyDataInPage(Integer pageNumber) {
		PageRequest pageRequest = new PageRequest(pageNumber - 1, PAGE_SIZE);
		//PageRequestというクラスのインスタンスの作成
		//ページ番号とサイズ（1ページあたりのエンティティ数）を引数に指定しnewする
		return repository.findAll(pageRequest);
		//引数にはPageRequestインスタンスを指定する。ページ分けしたエンティティを取り出す
		//返り値はListではなく「Page」というクラスのインスタンスになる
	}

}
