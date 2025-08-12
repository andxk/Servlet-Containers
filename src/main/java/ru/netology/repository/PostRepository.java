package ru.netology.repository;

import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {
    private AtomicLong postID = new AtomicLong(0);
    private Map<Long, Post> repo = new ConcurrentHashMap<>();

    public List<Post> all() {
        return new ArrayList<>(repo.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(repo.get(id));
    }

    public Post save(Post post) {
        long id = post.getId();
        if (!repo.containsKey(id)) { // заводим новую запись, если id не существует, включая id==0
            id = postID.incrementAndGet(); // нумеруем с 1
            post.setId(id);
        }
        repo.put(id, post);
        return post;
    }

    public void removeById(long id) {
        repo.remove(id);
    }
}
