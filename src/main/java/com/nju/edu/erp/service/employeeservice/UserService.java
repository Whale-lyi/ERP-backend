package com.nju.edu.erp.service.employeeservice;

import com.nju.edu.erp.model.po.user.User;
import com.nju.edu.erp.model.vo.user.UserVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserService {

    /**
     * 用户登录
     * @param userVO
     * @return
     */
    Map<String, String> login(UserVO userVO);

    /**
     * 用户注册
     * @param userVO
     */
    void register(UserVO userVO);

    /**
     * 用户认证
     * @param token
     */
    UserVO auth(String token);

    /**
     * 获取所有用户
     */
    List<User> getAllUser();

    User getUserById(Integer id);

    /**
     * 获取用户
     * @return
     */
    User getUserByName(String name);
}
