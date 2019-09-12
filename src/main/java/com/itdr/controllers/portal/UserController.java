package com.itdr.controllers.portal;


import com.itdr.common.ServerResponse;
import com.itdr.pojo.User;
import com.itdr.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@ResponseBody
@RequestMapping("/user/")
public class UserController {

    @Autowired
    UserService userService;

    //用户登录
    @PostMapping("login.do")
    public ServerResponse<User> login(String username, String password, HttpSession session){
        ServerResponse<User> sr = userService.login(username,password);
        //当返回的是成功状态才执行
        if (sr.isSuccess()){
            User u = sr.getData();
            session.setAttribute("login_user",u);
            User u2 = new User();
            u2.setId(u.getId());
            u2.setUsername(u.getUsername());
            u2.setEmail(u.getEmail());
            u2.setPhone(u.getPhone());
            u2.setCreateTime(u.getCreateTime());
            u2.setUpdateTime(u.getUpdateTime());
            u.setPassword("");
            sr.setData(u2);
        }
        return sr;
    }

    //用户注册
    @PostMapping("register.do")
    public ServerResponse<User> register(User u){
        ServerResponse<User> sr = userService.register(u);
        return sr;
    }

    //检查用户名或者邮箱是否存在
    @PostMapping("check_valid.do")
    public ServerResponse<User> checkUserName(String str,String type){
        ServerResponse<User> sr = userService.checkUserName(str,type);
        return sr;
    }

    //获取登陆用户信息
    @PostMapping("get_user_info.do")
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute("login_user");
        if (user == null){
            return ServerResponse.defeatedRS("用户未登录，无法获取登录信息");
        }else {
            return ServerResponse.successRS(user);
        }
    }

    //退出登录
    @PostMapping("logout.do")
    public ServerResponse<User> logout(HttpSession session){
        session.removeAttribute("login_user");
        return ServerResponse.successRS("退出成功");
    }

    //获取当前登陆用户详细信息
    @PostMapping("get_inforamtion.do")
    public ServerResponse<User> getInforamtion(HttpSession session){
        User user = (User)session.getAttribute("login_user");
        if (user == null){
            return ServerResponse.defeatedRS("用户未登录，无法获取登录信息");
        }else {
            ServerResponse sr = userService.getInforamtion(user);
            return sr;
        }
    }

    //登陆状态更新个人信息
    @PostMapping("update_information.do")
    public ServerResponse<User> update_information(User u,HttpSession session){
        User user = (User)session.getAttribute("login_user");
        if (user == null){
            return ServerResponse.defeatedRS("用户未登录，无法获取登录信息");
        }else {
            u.setId(user.getId());
            u.setUsername(user.getUsername());
            ServerResponse sr = userService.update_information(u);
            session.setAttribute("login_user",u);
            return sr;
        }
    }


    //忘记密码
    @PostMapping("forget_get_question.do")
    public ServerResponse<User> forgetGetQuestion(String username){
        return userService.forgetGetQuestion(username);
    }

    //提交问题答案
    @PostMapping("forget_check_answer.do")
    public ServerResponse<User> forgetCheckAnswer(String username,String question,String answer){
        return userService.forgetCheckAnswer(username,question,answer);
    }

    //忘记密码的重设密码
    @PostMapping("forget_reset_password.do")
    public ServerResponse<User> forgetResetPassword(String username,String passwordNew,String forgetToken){
        return userService.forgetResetPassword(username,passwordNew,forgetToken);
    }

    //登陆状态重置密码
    @PostMapping("reset_password.do")
    public ServerResponse<User> resetPassword(String passwordOId,String passwordNew,HttpSession session){
        User user = (User) session.getAttribute("login_user");
        if (user == null){
            return ServerResponse.defeatedRS("用户未登录");
        }else {
            return userService.resetPassword(user,passwordOId,passwordNew);
        }
    }
}
