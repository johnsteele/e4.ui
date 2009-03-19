package org.eclipse.e4.demo.modifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
public @interface Inject {

	boolean optional() default false;

}
