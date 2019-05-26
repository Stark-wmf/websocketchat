package com.websocket.dao;


import com.websocket.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert(" INSERT INTO entity SET username = #{username},password=#{password}")
    int register(User user);

    @Select("Select * from entity where username = #{username}")
    User getPassword(String username);

    @Select("Select username from entity where user_id = #{0}")
    String getUsername(int user_id);

    @Insert("Insert into relation SET username =#{username} ,tousername=#{tousername},status =1")
    String likeyou(String username,String tousername);

    @Update("Update relation SET status =0 Where username =#{username} ,tousername=#{tousername}")
    String hateyou(String username,String tousername);

    @Select("SELECT COUNT(id) FROM relation WHERE username =#{username} AND  tousername =#{tousername} ")
    int IfRelation(String username,String tousername);


    @Select("SELECT status FROM relation WHERE username =#{username} AND  tousername =#{tousername} ")
    int IfStatus(String username,String tousername);
}