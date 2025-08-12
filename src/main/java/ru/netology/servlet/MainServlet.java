package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class MainServlet extends HttpServlet {
    private final String GET = "GET";
    private final String POST = "POST";
    private final String DELETE = "DELETE";
    private final String PATH_POSTS = "/api/posts";
    private final String PATH_POST_ID = "/api/posts/\\d+";
    private PostController controller;

    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    private long idFromPath (String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            // primitive routing
            if (method.equals(GET) && path.equals(PATH_POSTS)) {
                controller.all(resp);
                return;
            }
            if (method.equals(GET) && path.matches(PATH_POST_ID)) {
                // easy way
                controller.getById(idFromPath(path), resp);
                return;
            }
            if (method.equals(POST) && path.equals(PATH_POSTS)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(DELETE) && path.matches(PATH_POST_ID)) {
                // easy way
                controller.removeById(idFromPath(path), resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

