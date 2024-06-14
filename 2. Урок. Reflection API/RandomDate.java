import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RandomDate {

    long min() default 1704067200000L;  // 1 января 2024 года UTC0
    long max() default 1735689600000L;  // 1 января 2025 года UTC0
    int zone() default 0;               // Часовой пояс
}
