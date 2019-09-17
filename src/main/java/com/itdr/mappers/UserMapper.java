package com.itdr.mappers;

import com.itdr.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    //根据用户名和密码查找用户
    User selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    //根据用户名或邮箱查询用户
    int selectByUserNameOrEmail(@Param("str") String str, @Param("type") String type);


    //根据邮箱查找是否存在
    int selectByEmailAndId(@Param("email")String email,@Param("id")Integer id);

    //根据用户名查找用户密码问题
    String selectByUserName(String username);

    //根据用户名和问题和答案查询数据是否存在
    int selectByUsernameAndQuestionAndAnswer(@Param("username")String username,
                                             @Param("question")String question,
                                             @Param("answer") String answer);

    //根据用户名更新密码
    int updateByUserNameAndPassword(@Param("username")String username, @Param("passwordNew")String passwordNew);

    //根据用户id查询密码是否正确
    int selectByIdAndPassword(@Param("id")Integer id, @Param("password")String password);
}