package org.example;

import org.example.entity.Post;
import org.example.entity.PostComment;
import org.example.entity.User;

import java.util.*;

public class EntityFactory {
    private static int countUser = 0;
    private static int countPost = 1000;
    private static int countComment = 5000;
    private static Random createRandom() {
        return new Random();
    }

    public void resetCountUser() {
        countUser = 0;
    }

    public void resetCountPost() {
        countPost = 1000;
    }

    public void resetCountComment() {
        countPost = 5000;
    }

    // Саздаем публикацию ==================================================================================================
    private static Post createPost(User user) {
        Random rnd = createRandom();
        String[] titles = {"Польза зеленого чая для здоровья", "Топ 5 самых популярных мобильных приложений на рынке",
                "Как правильно выбрать подарок на день рождения", "Плюсы и минусы удаленной работы",
                "10 фактов о космосе, которые вас удивят", "История возникновения караоке и его популярность в мире",
                "Как сохранить здоровье глаз при работе за компьютером", "Популярные тренды в мире моды на этот сезон",
                "Лучшие способы расслабиться после долгого рабочего дня",
                "Забавные факты о животных, которые вы не знали"
        };
        long[] dates = {1705558307000L, 1593288368000L, 1538661011000L, 1759133016000L, 1251402961000L,
                1810580403000L, 1657785238000L, 1303962801000L, 1623734487000L, 1142692175000L
        };
        long id = ++countPost;
        String title = titles[rnd.nextInt(titles.length)];
        Date date = new Date(dates[rnd.nextInt(dates.length)]);
        Post post = new Post(id, title, date);
        post.setComments(new ArrayList<>());
        post.setUser(user);

        return post;
    }

    public static List<Post> generatedPosts(int count, User user) {
        int resultCount = Math.min(count, 10);
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < resultCount; i++) {
            posts.add(createPost(user));
        }
        return posts;
    }

    // Саздаем комментарии ==================================================================================================
    private static PostComment createPostComments(Post post, User user) {
        Random rnd = createRandom();
        String[] comments = {"Прекрасно написано!", "Это действительно вдохновляет.",
                "Впечатляюще!", "Безупречный стиль.",
                "Просто великолепно!", "Захватывает дух.",
                "Очень ясно и понятно", "Искренние слова.",
                "Эмоционально и зажигательно.", "Слова проникают в самое сердце."
        };

        long[] dates = {1705558307000L, 1593288368000L, 1538661011000L, 1759133016000L, 1251402961000L,
                1810580403000L, 1657785238000L, 1303962801000L, 1623734487000L, 1142692175000L
        };
        long id = ++countComment;
        String title = comments[rnd.nextInt(comments.length)];
        Date date = new Date(dates[rnd.nextInt(dates.length)]);
        PostComment comment = new PostComment(id, title, date);
        comment.setPost(post);
        comment.setUser(user);

        return comment;
    }

    public static List<PostComment> generatedComments(int count, Post post, User user) {
        int resultCount = Math.min(count, 10);
        List<PostComment> comments = new ArrayList<>();
        for (int i = 0; i < resultCount; i++) {
            comments.add(createPostComments(post, user));
        }
        return comments;
    }

    // Саздаем пользователя ==================================================================================================
    private static User createUser() {
        Random rnd = createRandom();
        String[] names = {"Артём", "Серафим", "Амира", "Вероника", "Эмилия",
                "Виктория", "Аврора", "Серафима", "Мия", "Артемий",
                "Роман", "Николай", "Ксения", "Тихон", "Софья",
                "Дмитрий", "Максим", "Милана", "Марьям", "Варвара",
                "Михаил", "Вячеслав", "Полина", "Матвей", "Пётр",
                "София", "Диана", "Макар", "Амина", "Лилия",
                "Марина", "Марсель", "Иван", "Александр", "Мария",
                "Элина", "Лидия", "Злата", "Василиса", "Елисей",
                "Яна", "Екатерина", "Ульяна", "Дамир", "Александра",
                "Платон", "Анна", "Анастасия", "Лука", "Лев"
        };
        long id = ++countUser;
        String name = names[rnd.nextInt(names.length)];
        User user = new User(id, name);
        user.setComments(new ArrayList<>());
        user.setPosts(new ArrayList<>());

        return user;
    }

    public static List<User> generatedUsers(int count) {
        int resultCount = Math.min(count, 50);
        List<User> users = new ArrayList<>();
        for (int i = 0; i < resultCount; i++) {
            users.add(createUser());
        }
        return users;
    }
}
