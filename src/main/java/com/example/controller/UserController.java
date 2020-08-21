package com.example.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@RestController
@Component
public class UserController {

    @RequestMapping(value = "/isLogin", method = RequestMethod.GET)
    @ResponseBody
    public boolean isLogin(){
        Subject subject = SecurityUtils.getSubject();
        boolean flag = subject.isAuthenticated();
        if (flag){
            return true;
        }
        return false;
    }

    @RequestMapping(value = "/toLogin", method = RequestMethod.GET)
    @ResponseBody
    public String toLogin(){
        if (isLogin()){
            return "已经登录了";
        }
        return "未登录";
    }

    @RequestMapping(value = "/noauth", method = RequestMethod.GET)
    @ResponseBody
    public String auth(){
       return "未授权";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public String subLogin(@RequestParam("username")String username, @RequestParam("password")String password) {
        // 获取主体，用于安全验证
        Subject subject = SecurityUtils.getSubject();
        // 将前台传来的 用户 信息，按照规则生成
        System.out.println(username + " " + password);
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        // 设置记住我状态，在此状态内，可以访问对应权限的资源，对应的权限为过滤器中的user权限
        token.setRememberMe(true);
        try {
            // 提交验证
            subject.login(token);
        } catch (UnknownAccountException e) {
            return "用户名不存在";
        } catch (IncorrectCredentialsException e){
            return "密码错误";
        }
        return "验证成功";
    }
    @RequestMapping(value = "/getSession", method = RequestMethod.GET)
    public String getSession(){
    Session session = SecurityUtils.getSubject().getSession(false);
        return  session.getId().toString() + " " + session.getTimeout() + " " + session.getLastAccessTime();
    }
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(){
        Session session = SecurityUtils.getSubject().getSession(false);
        Subject subject = SecurityUtils.getSubject();
        return  subject.isRemembered() + " " + session.getTimeout() + " " + session.getLastAccessTime();
    }

    @RequestMapping(value = "/testA", method = RequestMethod.GET)
    public String testA(){
        return  "哈哈哈哈，你有权限了";
    }
}
