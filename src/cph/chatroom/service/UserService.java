package cph.chatroom.service;

import cph.chatroom.dao.UserDaoImple;
import cph.chatroom.vo.User;

public class UserService {
    UserDaoImple userDaoImple = new UserDaoImple();
    public User login(User user){
        return userDaoImple.login(user);
    }
}
