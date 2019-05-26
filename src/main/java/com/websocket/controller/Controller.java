package com.websocket.controller;

import com.websocket.service.UserService;
import com.websocket.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
public class Controller {
    @Autowired
    private UserService userService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/websocket/{username}")
    public String webSocket(@PathVariable String username, Model model) {
        try {
            logger.info("跳转到websocket的页面上");
            model.addAttribute("username", username);
            return "websocket";
        } catch (Exception e) {
            logger.info("跳转到websocket的页面上发生异常，异常信息是：" + e.getMessage());
            return "error";
        }


    }
    /**
     * 登陆注册接口
     *
     */
    @RequestMapping("/register")
    public Object register(String username,String password) throws Exception {
        User user =new User();

        user.setUsername(username);
        user.setPassword(password);
        try {
            userService.insertUser(user);
        }catch (Exception e){
            log.error("用户注册异常={}",e.getMessage(),e);
        }
        return userService.insertUser(user);
    }

    @RequestMapping("/login")
    public Object login(String username, String pwd) throws Exception {
        try {
            userService.login(username,pwd);
        }catch (Exception e){
            log.error("用户登录异常={}",e.getMessage(),e);
        }
        return userService.login(username,pwd);
    }

    @RequestMapping("/websocket/like")
    public String like(HttpSession session,String tousername) {
        try {
        String username = (String) session.getAttribute("username");
        userService.like(username,tousername);
            return "websocket";
        } catch (Exception e) {
            logger.info("添加好友发生异常，异常信息是：" + e.getMessage());
            return "error";
        }
    }

    @RequestMapping("/websocket/dislike")
    public String dislike(HttpSession session,String tousername) {
        try {
            String username = (String) session.getAttribute("username");
            userService.dislike(username,tousername);
            return "websocket";
        } catch (Exception e) {
            logger.info("删除好友发生异常，异常信息是：" + e.getMessage());
            return "error";
        }
    }

    /**
     * 功能没完善好，测试创建仅仅一个在线聊天室
     * @param session
     * @param groupname
     * @return
     */
    @RequestMapping("/websocket/{username}/{groupname}")
    public String creategroup(HttpSession session,String groupname) {
            String username = (String) session.getAttribute("username");
            session.setAttribute("username",username);
            session.setAttribute("groupname",groupname);
             log.info("用户" + username + "创建了" + groupname);
             return "success";
    }


}



