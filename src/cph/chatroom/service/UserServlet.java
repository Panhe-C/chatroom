package cph.chatroom.service;

import cph.chatroom.dao.UserDao;
import cph.chatroom.dao.UserDaoImple;
import cph.chatroom.vo.User;

public class UserServlet {

    public User login(User user){
        UserDaoImple userDao = new UserDaoImple();
        return userDao.login(user);
    }

}
