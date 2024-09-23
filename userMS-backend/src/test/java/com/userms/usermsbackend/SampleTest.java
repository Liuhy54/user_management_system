package com.userms.usermsbackend;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.userms.usermsbackend.mapper.UserMapper;
import com.userms.usermsbackend.model.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = UserMsBackendApplication.class)
public class SampleTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assert.isTrue(5 == userList.size(), "");
        userList.forEach(System.out::println);
    }

}