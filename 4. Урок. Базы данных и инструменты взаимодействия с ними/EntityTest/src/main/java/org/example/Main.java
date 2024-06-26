package org.example;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.entity.Post;
import org.example.entity.PostComment;
import org.example.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");

        try (SessionFactory sessionFactory = cfg.buildSessionFactory()) {

            // РАСКОММЕНТРИРОВАТЬ ДЛЯ ПРОВЕРКИ ВЫПОЛНЕНИЯ ДОП. ЗАДАНИЙ
            System.out.println();
            System.out.println("Получаем комментарии публикации =============================================================== ");
            loadAllCommentsByPost(sessionFactory, 1001L);
            loadAllCommentsByPost(sessionFactory, 1091L);

            System.out.println();
            System.out.println("Получаем публикации пользователя =============================================================== ");
            loadAllPostByUser(sessionFactory, 1L);

            System.out.println();
            System.out.println("Получаем комментарии пользователя =============================================================== ");
            loadAllCommentByUser(sessionFactory, 11L);

            System.out.println();
            System.out.println("Получаем всех пользователей, под которыми, указанный пользователь оставлял комментарии ========== ");
            loadAllUserByUser(sessionFactory, 11L);


            // РАСКОММЕНТРИРОВАТЬ ДЛЯ СОЗДАНЯИ ЗАПИСЕЙ В БД
//            List<Post> posts = new ArrayList<>();
//            List<User> users = EntityFactory.generatedUsers(10);
//
//            for (User user : users) {
//                CRUD.insert(user, sessionFactory);
//                posts = EntityFactory.generatedPosts(10, user);
//
//                for (Post post : posts) {
//                    CRUD.insert(post, sessionFactory);
//                }
//            }
//
//            users = EntityFactory.generatedUsers(10);
//            for (User user : users) {
//                CRUD.insert(user, sessionFactory);
//
//                for (int i = 0; i < posts.size(); i++) {
//                    int index = new Random().nextInt(1000, posts.size() * users.size() + 1000);
//
//                    Post post = CRUD.load(Post.class, index, sessionFactory);
//                    if (post == null) continue;
//
//                    List<PostComment> comments = EntityFactory.generatedComments(10, post, user);
//
//                    for (PostComment comment : comments) {
//                        CRUD.insert(comment, sessionFactory);
//                    }
//                }
//            }
        }
    }


    /**
     * 3.1 Метод получения всех комментариев публикации
     * @param sessionFactory интерфейс подключения
     * @param postId идентификатор публикации
     */
    private static void loadAllCommentsByPost(SessionFactory sessionFactory, long postId) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.find(Post.class, postId);
            System.out.println(post);

            for (PostComment comment : post.getComments()) {
                System.out.println(comment);
            }
        }
    }

    /**
     * 3.2 Метод получения всех публикаций пользователя
     * @param sessionFactory интерфейс подключения
     * @param userId идентификатор публикации
     */
    private static void loadAllPostByUser(SessionFactory sessionFactory, long userId) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.find(User.class, userId);
            System.out.println(user);

            for (Post post : user.getPosts()) {
                System.out.println(post);
            }
        }
    }

    /**
     * 3.3 Метод получения всех комментариев пользователя
     * @param sessionFactory интерфейс подключения
     * @param userId идентификатор публикации
     */
    private static void loadAllCommentByUser(SessionFactory sessionFactory, long userId) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.find(User.class, userId);
            System.out.println(user);

            for (PostComment comment : user.getComments()) {
                System.out.println(comment);
            }
        }
    }

    /**
     * 3.4 Метод получения всех пользователей под которыми указанный пользователь оставлял комментарии
     * @param sessionFactory интерфейс подключения
     * @param userId идентификатор публикации
     */
    private static void loadAllUserByUser(SessionFactory sessionFactory, long userId) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> rootEntry = cq.from(User.class);
            CriteriaQuery<User> all = cq.select(rootEntry);

            TypedQuery<User> allQuery = session.createQuery(all);
            List<User> users = allQuery.getResultList();
            List<User> result = new ArrayList<>();

            for (User user : users) {
                boolean need_break = false;
                for (Post post : user.getPosts()) {
                    for (PostComment comment : post.getComments()) {
                        if (comment.getUser().getId() == userId) {
                            result.add(user);
                            need_break = true;
                        }
                        if (need_break) {
                            break;
                        }
                    }
                    if (need_break) {
                        break;
                    }
                }
            }

            if (!result.isEmpty()) {
                User user = session.find(User.class, userId);
                System.out.println("Пользователь " + user.getName() + " комментировал(а) публикации пользователей: ");
                for (User u : result) {
                    System.out.println(u);
                }
            } else {
                System.out.println("Пользователи не найдены");
            }





//            System.out.println(allQuery.getResultList());
//
//            User user = session.find(User.class, userId);
//            System.out.println(user);
//
//            List<User> users = new ArrayList<>();
//
//            for (PostComment comment : user.getComments()) {
//                Post post = comment.getPost();
//                boolean addAccess = true;
//                for (User u: users) {
//                    User userFind = post.getUser();
//                    if (userFind.equals(u)) {
//                        addAccess = false;
//                        break;
//                    }
//                }
//
//                if (addAccess) {
//                    users.add(comment.getPost().getUser());
//                }
//            }
//
//            if (!users.isEmpty()) {
//                System.out.println("Пользователь " + user.getName() + " комментировал(а) публикации пользователей: ");
//                for (User u : users) {
//                    System.out.println(u);
//                }
//            } else {
//                System.out.println("Пользователи не найдены");
//            }
        }
    }
}