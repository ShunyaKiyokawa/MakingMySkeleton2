//数字をカウントするだけの簡単なコンポーネント
//@RequestMapping("/count")

package newgroup.bean;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;


//コンポーネントとして認識させるためのアノテーション。インスタンスがBeanとして登録される
@Component
public class MySampleBean {
	private int counter = 0;
	private int max = 10;

	//コンストラクタにAutowiredをつけ、Bean登録時の値にする
	//コンポーネントにAutowiredがないと起動に失敗する
	@Autowired
	public MySampleBean(ApplicationArguments args) {
		//ApplicationArgumentsはアプリ（SpringApplication.run）が実行されたときに渡された引数
		List<String> files = args.getNonOptionArgs();
		//getNonOptionArgsはアプリケーション実行時の引数をlistとして取り出せる
		try {
			max = Integer.parseInt(files.get(0));
			//取り出した最初の要素をInteger.ParseIntで整数に変換し、maxフィールドに設定
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	public int count() {
		counter++;
		counter = counter > max ? 0 : counter;
		//counterがmaxより大きいなら0、小さないならcounterを返す
		return counter;
	}
}
