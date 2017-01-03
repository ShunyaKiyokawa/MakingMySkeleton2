//ThymeleafのUtlityを使うのに必要なDialectクラスの実装
//ほとんど「この通り書けばOK」の決め打ちコード
//p352プロジェクト時点では動かない

package newgroup.UtilityAndDialect;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.thymeleaf.context.IProcessingContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionEnhancingDialect;

public class MyTLDialect extends AbstractDialect
		implements IExpressionEnhancingDialect {
	//IExpressionEnhancingDialectはgetAdditionalExpressionObjectsとgetPrefixの実装が必須

	private static final Map<String, Object> EXPRESSION_OBJECTS;

	//objects.put("myTLHelper", new MyTLUtility());のみ決め打ちコードではない
	static {
		Map<String, Object> objects = new HashMap<>();
		objects.put("myTLHelper", new MyTLUtility());
		EXPRESSION_OBJECTS = Collections.unmodifiableMap(objects);
	}

	public MyTLDialect() {
		super();
	}

	@Override
	public Map<String, Object> getAdditionalExpressionObjects
			(IProcessingContext processingContext) {
		return EXPRESSION_OBJECTS;
	}

	@Override
	public String getPrefix() {
		return null;
	}
}