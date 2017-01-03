//ページネーションのユーティリティオブジェクト
//ユーティリティオブジェクトは出力するテキストをreturnするもの
//352プロジェクト時点では動かない

package newgroup.UtilityAndDialect;

public class MyTLUtility {
	//ユーティリティオブジェクトを利用する際のサンプル
	public String hello(String name) {
		return "Hello, <b>" + name + "!</b>";
	}
	//前のページ
	public String prevUrl(int num) {
		return "/page/" + (num > 1 ? num - 1 : 1);
	}
	//次のページ
	public String nextUrl(int num) {
		return "/page/" + (num + 1);
	}
}
