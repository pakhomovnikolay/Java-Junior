package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class PersonService {

    public void createTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    create table person (
                      id bigint,
                      name varchar(256),
                      age integer,
                      active boolean,
                      department_id bigint,
                      primary key (id),
                      foreign key (department_id) references department(id)
                    )
                    """);
        } catch (SQLException e) {
            System.err.println("Во время создания таблицы произошла ошибка: " + e.getMessage());
            throw e;
        }
    }

    public void addForeignKeyTable(Connection connection, String ForeignKey, String PrimaryKey) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("alter table person add foreign key (" + ForeignKey + ") references " + PrimaryKey);
        } catch (SQLException e) {
            System.err.println("Во время создания таблицы произошла ошибка: " + e.getMessage());
            throw e;
        }
    }

    public void insertData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            StringBuilder insertQuery = new StringBuilder(
                    "insert into person(id, name, age, active, department_id) values\n");
            for (int i = 1; i <= 10; i++) {
                int age = ThreadLocalRandom.current().nextInt(20, 60);
                int department_id = ThreadLocalRandom.current().nextInt(1, 5);
                boolean active = ThreadLocalRandom.current().nextBoolean();
                insertQuery
                        .append(String.format("(%s, '%s', %s, %s, %s)", i + 100, "Person #" + i, age, active,
                                department_id));

                if (i != 10) {
                    insertQuery.append(",\n");
                }
            }

            int insertCount = statement.executeUpdate(insertQuery.toString());
            System.out.println("Вставлено строк: " + insertCount);
        }
    }

    public void updateData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            int updateCount = statement.executeUpdate("update person set active = true where id > 5");
            System.out.println("Обновлено строк: " + updateCount);
        }
    }

    public List<String> selectNamesByAge(Connection connection, String age) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("select name from person where age = ?")) {
            statement.setInt(1, Integer.parseInt(age));
            ResultSet resultSet = statement.executeQuery();

            List<String> names = new ArrayList<>();
            while (resultSet.next()) {
                names.add(resultSet.getString("name"));
            }
            return names;
        }
    }

    public void selectData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("""
                    select id, name, age, department_id
                    from person
                    where active is true""");

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                long department_id = resultSet.getLong("department_id");
                System.out.println("Найдена строка: [id = " + id + ", name = " + name + ", age = " + age
                        + ", department_id = " + department_id + "]");
            }
        }
    }

    /** Пункт 4 v.1 */
    public String getPersonDepartmentName(Connection connection, long personId) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sqlCommand = String.format("select department.id, department.department_name"
                    + " from department"
                    + " join person"
                    + " on department.id = person.department_id"
                    + " where person.id = %d", personId);

            ResultSet resultSet = statement.executeQuery(sqlCommand);
            while (resultSet.next())
                return resultSet.getString("department_name");

            return "";
        }
    }

    /** Пункт 4 v.2 */
    public Department getEntityPersonDepartmentName(Connection connection, long personId) throws SQLException {
        try (Statement statement = connection.createStatement()) {

            String sqlCommand = String.format("select department.id, department.department_name"
                    + " from department"
                    + " join person"
                    + " on department.id = person.department_id"
                    + " where person.id = %d", personId);

            ResultSet resultSet = statement.executeQuery(sqlCommand);
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("department_name");
                Department department = new Department(id, name);
                return department;
            }
            return new Department(0, "department_unnamed");
        }
    }

    /** Пункт 5 v.1 */
    public Map<String, String> getPersonDepartments(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            Map<String, String> result = new HashMap<>();
            ResultSet resultSet = statement.executeQuery("select id, name from person");

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String department_name = getPersonDepartmentName(connection, id);
                result.put(name, department_name);
            }
            return result;
        }
    }

    /** Пункт 5 v.2 */
    public Map<Person, Department> getEntityPersonDepartments(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            Map<Person, Department> result = new HashMap<>();
            ResultSet resultSet = statement.executeQuery("select id, name, age, active from person");

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                int age = resultSet.getInt("age");
                String name = resultSet.getString("name");
                boolean active = resultSet.getBoolean("active");
                Person person = new Person(id, age, name, active, getEntityPersonDepartmentName(connection, id));
                result.put(person, person.getDepartment());
            }
            return result;
        }
    }

    /** Пункт 6 v.1 */
    public Map<String, List<String>> getDepartmentPersons(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            Map<String, List<String>> result = new HashMap<>();
            ResultSet resultSet = statement.executeQuery("select id, name from person");

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String department_name = getPersonDepartmentName(connection, id);

                if (result.containsKey(department_name)) {
                    result.get(department_name).add(name);
                } else {
                    result.put(department_name, new ArrayList<String>());
                    result.get(department_name).add(name);
                }
            }
            return result;
        }
    }

    /** Пункт 6 v.2 */
    public Map<Department, List<Person>> getEntityDepartmentPersons(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            Map<Department, List<Person>> result = new HashMap<>();
            ResultSet resultSet = statement.executeQuery("select id, name, age, active from person");

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                int age = resultSet.getInt("age");
                String name = resultSet.getString("name");
                boolean active = resultSet.getBoolean("active");
                Person person = new Person(id, age, name, active, getEntityPersonDepartmentName(connection, id));
                Department department = getEntityPersonDepartmentName(connection, id);

                if (result.containsKey(department)) {
                    result.get(department).add(person);
                } else {
                    result.put(department, new ArrayList<Person>());
                    result.get(department).add(person);
                }
            }
            return result;
        }
    }
}
