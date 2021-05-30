package com.rozsa.business.api;

import com.rozsa.repository.model.User;

import java.util.List;


public interface UserBusiness {
    User findByLogin(String login);

    void create(User user);

    void update(User user);

    void delete(Long id);

    User get(Long id);

    List<User> getAll(int start, int limit);
}
