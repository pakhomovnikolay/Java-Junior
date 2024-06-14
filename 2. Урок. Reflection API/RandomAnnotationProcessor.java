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

                // Для удобства собираем битовую маску
                int validateField = 0;
                if (field.getType().isAssignableFrom(Date.class)) {
                    validateField += 1;
                }
                if (field.getType().isAssignableFrom(LocalDate.class)) {
                    validateField += 2;
                }
                if (field.getType().isAssignableFrom(LocalDateTime.class)) {
                    validateField += 4;
                }
                if (field.getType().isAssignableFrom(Instant.class)) {
                    validateField += 8;
                }

                // Обрабываем поля только с датой и временем
                if (validateField > 0) {
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
                            if ((validateField & 1) > 0) {
                                field.set(obj, date);
                            } else if ((validateField & 2) > 0) {
                                field.set(obj, LocalDate.ofInstant(date.toInstant(), ZoneOffset.ofHours(zone)));
                            } else if ((validateField & 4) > 0) {
                                field.set(obj, LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.ofHours(zone)));
                            } else if ((validateField & 8) > 0) {
                                field.set(obj, date.toInstant());
                            }
                        } catch (IllegalAccessException e) {
                            System.err.println("Не удалось вставить значение в поле: " + e.getMessage());
                        }
                    }
                }
            }
        }
    }
}
