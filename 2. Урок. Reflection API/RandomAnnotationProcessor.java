import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class RandomAnnotationProcessor {
    
    public static void processAnnotation(Object obj) {
        java.util.Random random = new java.util.Random();
        Class<?> objClass = obj.getClass();
        for (Field field : objClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(RandomDate.class)) {

                RandomDate annotation = field.getAnnotation(RandomDate.class);
                long min = annotation.min();
                long max = annotation.max();
                int zone = annotation.zone();
                long result = random.nextLong(min, max);

                if (min >= max) {
                    System.err.println(
                            "Не удалось вставить значение в поле: Минимальная дата должна быть больше максимальной");
                } else {
                    try {
                        field.setAccessible(true); // чтобы можно было изменять финальные поля
                        Date date = new Date(result);
                        if (field.getType().isAssignableFrom(Date.class))
                            field.set(obj, date);
                        else if (field.getType().isAssignableFrom(LocalDate.class))
                            field.set(obj, LocalDate.ofInstant(date.toInstant(), ZoneOffset.ofHours(zone)));
                        else if (field.getType().isAssignableFrom(LocalDateTime.class))
                            field.set(obj, LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.ofHours(zone)));
                        else if (field.getType().isAssignableFrom(Instant.class))
                            field.set(obj, date.toInstant());
                    } catch (IllegalAccessException e) {
                        System.err.println("Не удалось вставить значение в поле: " + e.getMessage());
                    }
                }
            }
        }
    }
}
