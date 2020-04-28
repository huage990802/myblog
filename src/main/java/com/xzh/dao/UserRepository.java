package com.xzh.dao;

import com.xzh.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional
    User findAllByUsernameAndPassword(String username,String password);

}

