import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Lesson_2 {

    
    public static void main(String[] args) {
        Person rndPerson = ObjectCreator.createObj(Person.class);
        System.out.println("long_date = " + rndPerson.long_date);
        System.out.println("date = " + rndPerson.date);
        System.out.println("instant = " + rndPerson.instant);
        System.out.println("localDate = " + rndPerson.localDate);
        System.out.println("localDateTime = " + rndPerson.localDateTime);
        System.out.println("localDateTimeOffsetUTC = " + rndPerson.localDateTimeOffsetUTC);
        System.out.println("localDateTimeOffsetMsc = " + rndPerson.localDateTimeOffsetMsc);
    }

    public static class Person {

        @RandomDate()
        private long long_date;

        @RandomDate()
        private Date date;

        @RandomDate()
        private Instant instant;

        @RandomDate()
        private LocalDate localDate;

        @RandomDate()
        private LocalDateTime localDateTime;

        // Обычное время: Fri, 15 Mar 2024 13:45:58 GMT ( +3 => 16:45:58)
        // min - 1710510358000L => ПТ, 15 Марта 2024 13:45:58
        // min - 1710510358000L => ПТ, 15 Марта 2024 13:45:59
        // result UTC - ПТ, 15 Марта 2024 13:45:58
        // result Moscow - ПТ, 15 Марта 2024 16:45:58
        @RandomDate(min = 1710510358000L, max = 1710510359000L, zone = 0)
        private LocalDateTime localDateTimeOffsetUTC;

        @RandomDate(min = 1710510358000L, max = 1710510359000L, zone = 3)
        private LocalDateTime localDateTimeOffsetMsc;
      }
    
}

/**
   * В существующий класс ObjectCreator добавить поддержку аннотации RandomDate (по аналогии с Random):
   * 1. Аннотация должна обрабатываться только над полями типа java.util.Date
   * 2. Проверить, что min < max
   * 3. В поле, помеченной аннотацией, нужно вставлять рандомную дату,
   * UNIX-время которой находится в диапазоне [min, max)
   *
   * 4. *** Добавить поддержку для типов Instant, ...
   * 5. *** Добавить атрибут Zone и поддержку для типов LocalDate, LocalDateTime
   */

  /**
   * Примечание:
   * Unix-время - количество милисекунд, прошедших с 1 января 1970 года по UTC-0.
   */