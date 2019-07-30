package cph.chatroom.dao;

import cph.chatroom.utils.JDBCUtils;
import cph.chatroom.vo.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

public interface UserDao {

    public User login(User user);
}
