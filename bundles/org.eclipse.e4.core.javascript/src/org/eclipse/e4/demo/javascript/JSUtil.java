package org.eclipse.e4.demo.javascript;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.IdScriptableObject;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;

public class JSUtil {

	private Context jsContext;
	private ImporterTopLevel jsScope;

	public JSUtil() {
		jsContext = Context.enter();
		jsScope = new ImporterTopLevel(jsContext);
	}

	public Object eval(String js) {
		Object result = jsContext.evaluateString(jsScope, "[" + js + "][0]", "eval", 0, null); //$NON-NLS-1$
		return result;
	}
	
	public Object get(Object jsObject, String attribute) {
		if (jsObject instanceof Scriptable) {
			return ((Scriptable) jsObject).get(attribute,
					(IdScriptableObject) jsObject);
		}
		return null;
	}

	public Object get(Object jsArray, int index) {
		return ((IdScriptableObject) jsArray).get(index,
				(IdScriptableObject) jsArray);
	}
	
	public int length(Object jsArray) {
		Number number = (Number) get(jsArray, "length");
		return number == null ? 0 : number.intValue();
	}

	public Object call(Object jsFunction, Object jsThis, Object[] args) {
		return ((BaseFunction) jsFunction).call(jsContext, jsScope,
				(Scriptable) jsThis, args);
	}

	private static class Foo {
		public int incremented(int i) {
			return i + 1;
		}
	}

	public static void main(String[] a) {
		JSUtil js = new JSUtil();
		Object result = js
				.eval("{args:['foo','bar'], f:function(foo, x){return foo.incremented(x);}}");
		System.out.println(result);
		Object args = js.get(result, "args");
		System.out.println(args);
		System.out.println("length: " + js.length(args));
		Object function = js.get(result, "f");
		System.out.println(function);
		System.out.println(js.get(args, 0));
		System.out.println(js.get(args, 1));
		Object fResult = js.call(function, null, new Object[] { new Foo(), 6 });
		System.out.println(fResult);
	}
}
