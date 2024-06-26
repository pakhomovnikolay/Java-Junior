package org.example;

import org.hibernate.*;

public class CRUD {

    /**
     * Метод создания записей, в БД
     * @param sessionFactory интерфейс подключения сессии
     */
    public static <T> void insert(T Entity, SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(Entity);
            tx.commit();
        }
    }

    /**
     * Метод получения записи из БД
     * @param sessionFactory интерфейс подключения сессии
     */
    public static <T> T load(Class<T> type, long id, SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(type, id);
        }
    }

    /**
     * Метод обновления записей в БД
     * @param sessionFactory интерфейс подключения сессии
     */
    public static <T> void update(T Entity, SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(Entity);
            tx.commit();
        }
    }

    /**
     * Метод удаления записи из БД
     * @param sessionFactory интерфейс подключения сессии
     */
    public static <T> void delete(T Entity, SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.remove(Entity);
            tx.commit();
        }
    }
}
