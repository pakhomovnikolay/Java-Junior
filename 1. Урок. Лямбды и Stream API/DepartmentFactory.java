import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DepartmentFactory {

    private static Random random = new Random();

    private static Person generatePerson(){
        String[] names = new String[] { "Анатолий", "Глеб", "Клим", "Мартин", "Лазарь", "Владлен", "Клим", "Панкратий", "Рубен", "Герман" };

        String[] nameDepartament = new String[] { "ДМТО", "ДРПО", "ДПКР", "ДУП", "ДОТиПБ" };
        String[] descriptionDepartament = new String[] { "Департамент метериально технического обеспечения",
                                                        "Департамент разработки программного обеспечения",
                                                        "Департамент проектных и конструкторских работ",
                                                        "Департамент управления проектами",
                                                        "Департамент отхраны труда и пожарной безопасности" };

        int salary = random.nextInt(60000, 120000);
        int age = random.nextInt(21, 65);

        int randomIndex = random.nextInt(nameDepartament.length);

        return new Person(names[random.nextInt(names.length)], age, salary,
                    new Department(nameDepartament[randomIndex], descriptionDepartament[randomIndex]));
    }

    public static List<Person> generatePerson(int count) {
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            persons.add(generatePerson());
        }
        return persons;
    }
}
