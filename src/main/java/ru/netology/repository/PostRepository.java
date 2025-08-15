package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
@Repository
public class PostRepository implements IPostRepository {
    private AtomicLong postID = new AtomicLong(0);
    private Map<Long, Post> repo = new ConcurrentHashMap<>();

    @Override
    public List<Post> all() {
        return new ArrayList<>(repo.values());
    }

    @Override
    public Optional<Post> getById(long id) {
        return Optional.ofNullable(repo.get(id));
    }

    @Override
    public Post save(Post post) {
        long id = post.getId();
        if (!repo.containsKey(id)) { // заводим новую запись, если id не существует, включая id==0
            id = postID.incrementAndGet(); // нумеруем с 1
            post.setId(id);
        }
        repo.put(id, post);
        return post;
    }

    @Override
    public String removeById(long id) {
        if (repo.containsKey(id)) {
            repo.remove(id);
            return String.format("OK: Post ID %d successfully deleted", id);
        }
        else {
            throw new NotFoundException("Такой 'post.id' не найден");
        }
    }
}
