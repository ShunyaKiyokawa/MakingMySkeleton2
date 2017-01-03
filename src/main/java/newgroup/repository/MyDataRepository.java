//dataの処理は、controller空呼ばれたこのripositoryが処理をする

package newgroup.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import newgroup.entity.MyDataEntity;

@Repository
public interface MyDataRepository  extends JpaRepository<MyDataEntity, Long> {

	@Query("SELECT d FROM MyDataEntity d ORDER BY d.username")
	List<MyDataEntity> findAllOrderByUsername();
	//クエリーの実行が、Listとして返却される
	//「d」はMyDataEntityのエイリアス（別名）. MySQLでいう「*」
	//毎回MyDataEentityと書くのが面倒なので省略している
	@Query("from MyDataEntity where age > :min and age < :max")
	public List<MyDataEntity> findByAge(@Param("min") int min, @Param("max") int max);
	//コントローラーからdaoに対するアクセスではなくリポジトリに対するアクセスをしたときはこちらにアクセスされる

	public MyDataEntity findByUsername(String username); //
	public MyDataEntity findById(long username);
	public List<MyDataEntity> findByUsernameLike(String username);
	public List<MyDataEntity> findByIdIsNotNullOrderByIdDesc();
	public List<MyDataEntity> findByAgeGreaterThan(Integer age);
	public List<MyDataEntity> findByAgeBetween(Integer age1, Integer age2);

}
