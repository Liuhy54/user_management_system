package com.userms.usermsbackend.service;

import com.userms.usermsbackend.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * 用户服务
 *
 * @author lhynb54
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2024-09-23 19:12:14
 */
public interface UserService extends IService<User> {


    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword ,HttpServletRequest request);

    /**
     * 获取安全的用户信息(脱敏)
     *
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);
}
