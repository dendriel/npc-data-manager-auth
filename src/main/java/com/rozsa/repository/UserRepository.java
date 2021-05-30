package com.rozsa.repository;

import com.rozsa.repository.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByLogin(String login);

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);

    @Query(value = "SELECT u.id, u.email, u.login, u.name, u.active FROM User u WHERE u.active=true AND u.id=?1 ORDER BY u.id")
    Object[] find(Long id);

    @Query(value = "SELECT u.id, u.email, u.login, u.name, u.active FROM User u WHERE u.active=true ORDER BY u.id")
    Page<Object[]> find(Pageable pageable);
}
