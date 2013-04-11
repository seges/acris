package sk.seges.sesam.remote.jmx;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldExport {
    public static final int DEFAULT_INDEX = 99;
    String description() default "";
    int index() default DEFAULT_INDEX;
    /**
     * Don't use {@link Throwable} as type
     * @return
     */
    Class<?> type() default Throwable.class;
    /**
     * Safe conversion to string (calling toString with null check).
     */
    boolean convertToString() default false;
}
