package com.nju.edu.erp.dao.employeedao;


import com.nju.edu.erp.model.po.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserDao {

    User findByUsernameAndPassword(String username, String password);

    int createUser(User user);

    User findByUsername(String username);

    List<User> getAllUser();

    User findByUserId(Integer id);
}
