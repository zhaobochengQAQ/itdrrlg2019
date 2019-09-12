package com.itdr.services.impl;

import com.itdr.common.ServerResponse;
import com.itdr.common.TokenCache;
import com.itdr.mappers.UserMapper;
import com.itdr.pojo.User;
import com.itdr.services.UserService;
import com.itdr.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    //用户登录
    @Override
    public ServerResponse<User> login(String username, String password) {
        if(username == null || username.equals("")){
            return ServerResponse.defeatedRS("用户名不能为空");
        }

        if (password == null || password.equals("")){
            return ServerResponse.defeatedRS("密码不能为空");
        }

        //根据用户名查找是否存在该用户
        int i = userMapper.selectByUserNameOrEmail(username,"username");
        if (i<=0){
            return ServerResponse.defeatedRS("用户不存在");
        }

//        String md5Password = MD5Utils.getMD5Code(password);
//        根据用户名和密码查询用户是否存在
        User u = userMapper.selectByUsernameAndPassword(username,password);

        if (u == null){
            return ServerResponse.defeatedRS("账号或密码错误");
        }


        //封装数据并返回
        ServerResponse sr = ServerResponse.successRS(u);
        return sr;
    }

    //用户注册
    @Override
    public ServerResponse<User> register(User u) {
        if (u.getUsername() == null || u.getUsername().equals("")){
            return ServerResponse.defeatedRS("账户名不能为空");
        }
        if (u.getPassword() == null || u.getPassword().equals("")){
            return ServerResponse.defeatedRS("密码不能为空");
        }

        //检查注册用户名是否存在
        int i2 = userMapper.selectByUserNameOrEmail(u.getUsername(),"username");
        if (i2>0){
            return ServerResponse.defeatedRS("要注册的用户已经存在");
        }

        String md5Password = MD5Utils.getMD5Code(u.getPassword());
        u.setPassword(md5Password);
        int i = userMapper.insert(u);
        if (i<=0){
            return ServerResponse.defeatedRS("用户注册失败");
        }
        return ServerResponse.successRS(200,null,"用户注册成功");
    }

    //检查用户名或者邮箱是否存在
    @Override
    public ServerResponse<User> checkUserName(String str, String type) {

        if (str == null || str.equals("")){
            return ServerResponse.defeatedRS("参数不能为空");
        }
        if (type == null || type.equals("")){
            return ServerResponse.defeatedRS("参数类型不能为空");
        }
        int i = userMapper.selectByUserNameOrEmail(str,type);
        if(i>0 && type.equals("username")){
            return ServerResponse.defeatedRS("用户名已存在");
        }
        if(i>0 && type.equals("email")){
            return ServerResponse.defeatedRS("邮箱已经存在");
        }
        return ServerResponse.successRS(200,null,"校验成功");
    }


    //获取当前登陆用户详细信息
    @Override
    public ServerResponse getInforamtion(User user) {
        User user1 = userMapper.selectByPrimaryKey(user.getId());
        if (user1 == null){
            return ServerResponse.defeatedRS("用户不存在");
        }
        user1.setPassword("");
        return ServerResponse.successRS(user1);
    }

    //登陆状态更新个人信息
    @Override
    public ServerResponse update_information(User u) {
        int i2 = userMapper.selectByEmailAndId(u.getEmail(),u.getId());
        if (i2 >0){
            return ServerResponse.defeatedRS("要更新的邮箱已经存在");
        }

        int i = userMapper.updateByPrimaryKeySelective(u);
        if (i <= 0){
            return ServerResponse.defeatedRS("更新失败");
        }
        return ServerResponse.successRS("更新个人信息成功");
    }

    //忘记密码
    @Override
    public ServerResponse<User> forgetGetQuestion(String username) {
        if(username == null || "".equals(username)){
            return ServerResponse.defeatedRS("参数不能为空");
        }
        int i = userMapper.selectByUserNameOrEmail(username, username);
        if (i<=0){
            return ServerResponse.defeatedRS("用户名不存在");
        }
        String question = userMapper.selectByUserName(username);
        if (question == null || "".equals(question)){
            return ServerResponse.defeatedRS("该用户未设置找回密码问题");
        }
        return ServerResponse.successRS(question);
    }

    //提交问题答案
    @Override
    public ServerResponse<User> forgetCheckAnswer(String username, String question, String answer) {
        //参数是否为空
        if (username == null || "".equals(username)){
            return ServerResponse.defeatedRS("用户名不能为空");
        }
        if (question == null || "".equals(question)){
            return ServerResponse.defeatedRS("问题不能为空");
        }
        if (answer == null || "".equals(answer)){
            return ServerResponse.defeatedRS("答案不能为空");
        }
        int i = userMapper.selectByUsernameAndQuestionAndAnswer(username,question,answer);
        if (i<=0){
            return ServerResponse.defeatedRS("问题答案不匹配");
        }
        //产生随机字符令牌
        String token = UUID.randomUUID().toString();
        //把令牌放入缓存中，这里使用的是Google的guava缓存，后期会使用redis替代
        TokenCache.set("token_"+username,token);


        return ServerResponse.successRS("成功");
    }

    //忘记密码的重设密码
    @Override
    public ServerResponse<User> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (username == null || "".equals(username)){
            return ServerResponse.defeatedRS("用户名不能为空");
        }
        if (passwordNew == null || "".equals(passwordNew)){
            return ServerResponse.defeatedRS("新密码不能为空");
        }
        if (forgetToken == null || "".equals(forgetToken)){
            return ServerResponse.defeatedRS("非法的令牌参数");
        }
        //判断缓存中的Token
        String token = TokenCache.get("token_"+username);
        if (token == null || "".equals(token)){
            return ServerResponse.defeatedRS("token过期了");
        }
        if (!token.equals(forgetToken)){
            return ServerResponse.defeatedRS("非法的token");
        }
        int i = userMapper.updateByUserNameAndPassword(username,passwordNew);
        if (i<=0){
            return ServerResponse.defeatedRS("修改密码失败");
        }
        return ServerResponse.successRS("修改密码成功");
    }


    //登录状态修改密码
    @Override
    public ServerResponse<User> resetPassword(User user, String passwordOId, String passwordNew) {
        if (passwordOId == null || "".equals(passwordOId)){
            return ServerResponse.defeatedRS("参数不能为空");
        }
        if (passwordNew == null || "".equals(passwordNew)){
            return ServerResponse.defeatedRS("参数不能为空");
        }
        int i = userMapper.selectByIdAndPassword(user.getId(),passwordOId);
        if (i<=0){
            return ServerResponse.defeatedRS("旧密码输入错误");
        }
        int i2 = userMapper.updateByUserNameAndPassword(user.getUsername(),passwordNew);
        if (i2 <=0){
            return ServerResponse.defeatedRS("修改密码失败");
        }
        return ServerResponse.successRS("修改密码成功");
    }
}












