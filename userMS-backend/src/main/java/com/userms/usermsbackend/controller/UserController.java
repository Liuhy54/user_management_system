package com.userms.usermsbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.userms.usermsbackend.common.BaseResponse;
import com.userms.usermsbackend.common.ErrorCode;
import com.userms.usermsbackend.common.ResultUils;
import com.userms.usermsbackend.exception.BusinessException;
import com.userms.usermsbackend.model.domain.User;
import com.userms.usermsbackend.model.request.UserLoginRequeset;
import com.userms.usermsbackend.model.request.UserRequestRequest;
import com.userms.usermsbackend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
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
    public BaseResponse<Long> userRegister(@RequestBody UserRequestRequest userRequestRequest) {
        if (userRequestRequest == null) {
            return ResultUils.error(ErrorCode.PRAMS_ERROR);
        }
        String userAccount = userRequestRequest.getUserAccount();
        String userPassword = userRequestRequest.getUserPassword();
        String checkPassword = userRequestRequest.getCheckPassword();
        String planetCode = userRequestRequest.getPlanetCode();
        if (StringUtils.isAllBlank(userAccount, userPassword, checkPassword, planetCode)) {
            return null;
        }
        long register = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUils.success(register);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequeset userLoginRequeset, HttpServletRequest request) {
        if (userLoginRequeset == null) {
//            return ResultUils.error(ErrorCode.PRAMS_ERROR);
            throw new BusinessException(ErrorCode.PRAMS_ERROR);
        }
        String userAccount = userLoginRequeset.getUserAccount();
        String userPassword = userLoginRequeset.getUserPassword();
        if (StringUtils.isAllBlank(userAccount, userPassword)) {
            return ResultUils.error(ErrorCode.PRAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currnetUser = (User) userObj;
        if (currnetUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currnetUser.getId();
        // todo 校验用户是否存在
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUils.success(safetyUser);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> serchUsers(String username, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUils.success(list);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR);
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR);
        }
        boolean b = userService.removeById(id);
        return ResultUils.success(b);
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

}
