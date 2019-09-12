package com.itdr.services;

import com.itdr.common.ServerResponse;
import com.itdr.pojo.User;

public interface UserService {
    //用户登录
    ServerResponse<User> login(String username, String password);

    //用户注册
    ServerResponse<User> register(User u);

    //检查用户名或者邮箱是否存在
    ServerResponse<User> checkUserName(String str, String type);


    //获取当前登陆用户详细信息
    ServerResponse getInforamtion(User user);


    //登陆状态更新个人信息
    ServerResponse update_information(User u);


    //忘记密码
    ServerResponse<User> forgetGetQuestion(String username);

    //提交问题答案
    ServerResponse<User> forgetCheckAnswer(String username, String question, String answer);

    //忘记密码的重设密码
    ServerResponse<User> forgetResetPassword(String username, String passwordNew, String forgetToken);

    //登录状态修改密码
    ServerResponse<User> resetPassword(User user, String passwordOId, String passwordNew);
}
