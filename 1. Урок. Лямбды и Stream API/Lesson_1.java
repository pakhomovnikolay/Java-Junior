import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Lesson_1 {

    public static void main(String[] args) {

        List<Person> persons = DepartmentFactory.generatePerson(10);
        System.out.println(" =========================================== Сгенерированная коллекция департаментов =========================================== ");
        System.out.println(persons);

        // System.out.println();
        // System.out.println(" =========================================== Самый молодной сорудник =========================================== ");
        // System.out.println(findMostYoungestPerson(persons));

        // System.out.println();
        // System.out.println(" ======================== Департамент, в котором работает сотрудник с самой большой зарплатой ======================== ");
        // System.out.println(findMostExpensiveDepartment(persons));

        // System.out.println();
        // System.out.println(" ===================================== Сгруппированные сотрудники по департаментам ===================================== ");
        // System.out.println(groupByDepartment(persons));

        // System.out.println();
        // System.out.println(" ============================== Сгруппированные сотрудники по наименованию департаментам ============================== ");
        // System.out.println(groupByDepartmentName(persons));

        // System.out.println();
        // System.out.println(" ============================== Самый старший сотрудник в каждом департаменте ============================== ");
        // System.out.println(getDepartmentOldestPerson(persons));

        System.out.println();
        System.out.println(" ============================== Сотрудники с минимальными зарплатами в своем отделе ============================== ");
        System.out.println(cheapPersonsInDepartment(persons));
    }
    
    /**
     * Найти самого молодого сотрудника
     */
    public static Optional<Person> findMostYoungestPerson(List<Person> persons) {
        return persons.stream()
            .max((p1, p2) -> Integer.compare(p2.getAge(), p1.getAge()))
            ;
    }

    /**
     * Найти департамент, в котором работает сотрудник с самой большой зарплатой
     */
    public static Optional<Department> findMostExpensiveDepartment(List<Person> persons) {
        return persons.stream()
            .max((p1, p2) -> Double.compare(p1.getSalary(), p2.getSalary()))
            .flatMap(p -> Optional.of(p.getDepart()))
            ;
    }

    /**
     * Сгруппировать сотрудников по департаментам
     */
    public static Map<Department, List<Person>> groupByDepartment(List<Person> persons) {
        return persons.stream()
            .collect(Collectors.groupingBy(Person::getDepart));
    }

    /**
     * Сгруппировать сотрудников по названиям департаментов
     */
    public static Map<String, List<Person>> groupByDepartmentName(List<Person> persons) {
        return persons.stream()
            .collect(Collectors.groupingBy(p -> p.getDepart().getName()));
    }

    /**
     * В каждом департаменте найти самого старшего сотрудника
     */
    public static Map<String, Person> getDepartmentOldestPerson(List<Person> persons) {
        
        return persons.stream()
            .collect(Collectors.toMap(Person::getName, Function.identity(),
                (p1, p2) -> {
                    if (p1.getAge() > p2.getAge()) {
                        return p1;
                    }
                    return p2;
                }
            ));
    }

    /**
     * *Найти сотрудников с минимальными зарплатами в своем отделе
     * (прим. можно реализовать в два запроса)
     */
    public static List<Person> cheapPersonsInDepartment(List<Person> persons) {

        return groupByDepartment(persons).entrySet().stream()
            .map(t -> t.getValue().stream()
            .min((p1, p2) -> Double.compare(p1.getSalary(), p2.getSalary())))
            .flatMap(p -> p.stream()).toList();
    }

}
