public class Department {

    /** Наименвоание департамента */
    private String name;

    /** Описание департамента */
    private String description;

    /**
     * Метод возвращает наименвоание департамента
     * @return Наименвоание
     */
    public String getName() {
        return name;
    }

    /**
     * Метод возвращает описание департамента
     * @return Описание
     */
    public String getDescription() {
        return description;
    }

    /**
     * Конструктор класса
     * @param name Наименвоание департамента
     * @param description Описание департамента
     */
    public Department(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        String result = "" +
                "Наименование: " + name + "; " +
                "Описание: " + description + "\n";
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Department department) {
            return department.name == this.name && department.description == this.description;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return name.hashCode() + description.hashCode();
    }
}
