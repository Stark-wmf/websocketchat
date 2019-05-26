package com.websocket.service;

import com.websocket.entity.Response;
import com.websocket.dao.UserMapper;
import com.websocket.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Transactional
    public Map<Integer, String> insertUser(User user) {
        int insert = userMapper.register(user);
        Map<Integer, String> mapStatus = new HashMap<>();
        if (insert != 0) {
            mapStatus.put(0, "注册成功");
        } else {
            mapStatus.put(1, "注册失败");
        }
        return mapStatus;
    }
    @Transactional
    public Object login(String username,String pwd) throws Exception {
        User user = userMapper.getPassword(username);
        return new Response(0,"登录成功",null);
    }
    @Transactional
    public User queryUserByUsername(String username) {
        User user = null;
            user = userMapper.getPassword(username);
        return user;
    }

    @Transactional
    public void like(String username,String tousername) {
        userMapper.likeyou(username,tousername);

    }

    @Transactional
    public void dislike(String username,String tousername) {
        userMapper.hateyou(username,tousername);

    }

    @Transactional
    public boolean dolike(String username,String tousername) {
        if (userMapper.IfRelation(username,tousername)==1&&userMapper.IfStatus(username,tousername)==1){
            return true;
        }
            return false;
    }
}
