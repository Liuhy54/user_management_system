package com.userms.usermsbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.userms.usermsbackend.model.domain.User;
import com.userms.usermsbackend.model.request.UserLoginRequeset;
import com.userms.usermsbackend.model.request.UserRequestRequest;
import com.userms.usermsbackend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.userms.usermsbackend.contant.UserConstant.ADMIN_ROLE;
import static com.userms.usermsbackend.contant.UserConstant.USER_LOGIN_STATE;


/**
 * 用户接口
 *
 * @author haiy
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody  UserRequestRequest userRequestRequest) {
        if (userRequestRequest == null) {
            return null;
        }
        String userAccount = userRequestRequest.getUserAccount();
        String userPassword = userRequestRequest.getUserPassword();
        String checkPassword = userRequestRequest.getCheckPassword();
        if (StringUtils.isAllBlank(userAccount, userPassword, checkPassword)){
            return null;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequeset userLoginRequeset, HttpServletRequest request) {
        if (userLoginRequeset == null) {
            return null;
        }
        String userAccount = userLoginRequeset.getUserAccount();
        String userPassword = userLoginRequeset.getUserPassword();
        if (StringUtils.isAllBlank(userAccount, userPassword)){
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    @GetMapping("/serch")
    public List<User> serchUsers(String username, HttpServletRequest request){
        if (!isAdmin(request)){
            return new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }
        List<User> userList = userService.list(queryWrapper);
        return userList.stream().map(user -> {
            user.setUserPassword(null);
            return userService.getSafetyUser(user);
        }).collect(Collectors.toList());
    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id, HttpServletRequest request){
        if (!isAdmin(request)){
            return false;
        }
        if(id <= 0){
            return false;
        }
        return userService.removeById(id);
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request){
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

}
