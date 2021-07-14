package com.rozsa.business;

import com.rozsa.business.api.UserBusiness;
import com.rozsa.exception.InvalidArgsException;
import com.rozsa.exception.ResourceNotFoundException;
import com.rozsa.repository.UserRepository;
import com.rozsa.repository.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@AllArgsConstructor
@Component
public class UserBusinessImpl implements UserBusiness {

    private final UserRepository userRepository;

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public List<User> getAll(int start, int limit) {
        Pageable page = PageRequest.of(start, limit, Sort.by("id"));
        Page<Object[]> users = userRepository.find(page);

        return parseToUsers(users);
    }

    // TODO: this should be inside a repository impl.
    private List<User> parseToUsers(Page<Object[]> users) {
        return users.stream()
                .map(this::fromPageData)
                .collect(Collectors.toList());
    }

    private User fromPageData(Object[] data) {
        User user = new User();
        user.setId((Long)data[0]);
        user.setEmail((String)data[1]);
        user.setLogin((String)data[2]);
        user.setName((String)data[3]);
        user.setActive(true);

        return user;
    }

    @Override
    public User get(Long id) {
        Object[] data = userRepository.find(id);
        if (data.length == 0) {
            return null;
        }

        return fromPageData((Object[])data[0]);
    }

    @Override
    public void delete(Long id) {
        Optional<User> optUser = userRepository.findById(id);
        if (optUser.isEmpty()) {
            return;
        }

        User user = optUser.get();
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void update(User user) {
        if (user.getId() == null) {
            throw new ResourceNotFoundException();
        }

        Optional<User> optUser = userRepository.findById(user.getId());
        if (optUser.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        if (ObjectUtils.isEmpty(user.getName()) || ObjectUtils.isEmpty(user.getPassword())) {
            throw new InvalidArgsException("Some user data is missing!");
        }

        User sourceUser = optUser.get();
        sourceUser.setName(user.getName());

        String pwdHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        sourceUser.setPassword(pwdHash);

        userRepository.save(sourceUser);

        log.info("User updated! " + user);
    }

    @Override
    public void create(User user) {
        assertNewUserInfo(user);
        String pwdHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(pwdHash);

        user.setActive(true);
        user.setService(false);
        userRepository.save(user);

        log.info("User created! " + user);
    }

    private void assertNewUserInfo(User user) {
        assertAllFieldsFilled(user);

        if (userRepository.existsByLogin(user.getLogin())) {
            throw new InvalidArgsException("User login already in use!");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new InvalidArgsException("User email already in use!");
        }
    }

    private void assertAllFieldsFilled(User user) {
        if (ObjectUtils.isEmpty(user.getName()) ||
            ObjectUtils.isEmpty(user.getLogin()) ||
            ObjectUtils.isEmpty(user.getPassword()) ||
            ObjectUtils.isEmpty(user.getEmail())
        ) {
            throw new InvalidArgsException("Some user data is missing!");
        }
    }
}
