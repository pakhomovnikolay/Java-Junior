public class Person {

    /** Имя сотрудника */
    private String name;

    /** Возраст сотрудника */
    private int age;

    /** Ставка */
    private double salary;

    /** Департамент */
    private Department depart;

    /**
     * Метод возвращает имя сотрудника
     * @return Имя
     */
    public String getName() {
        return name;
    }

    /**
     * Метод возвращает возраст сотрудника
     * @return Возраст
     */
    public int getAge() {
        return age;
    }

    /**
     * Метод возвращает ставку сотрудника
     * @return Ставка
     */
    public double getSalary() {
        return salary;
    }

    /**
     * Метод возвращает данные департамента, в котором работет сотрудник
     * @return данные департамента
     */
    public Department getDepart() {
        return depart;
    }

    /**
     * Конструктор класса
     * @param name Имя
     * @param age Возраст
     * @param salary Ставка
     * @param depart Департамент
     */
    public Person(String name, int age, double salary, Department depart) {
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.depart = depart;
    }

    @Override
    public String toString() {
        String result =
                "Имя: " + name + "; " +
                "Возраст: " + age + "; " +
                "Ставка: " + salary + "; " +
                "Департамент - " + depart.getName() + "\n";
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person person) {
            return person.name == this.name && person.age == this.age && person.salary == this.salary;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return name.hashCode() + Integer.hashCode(age) + Double.hashCode(salary);
    }
}
