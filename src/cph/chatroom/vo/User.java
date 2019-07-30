package cph.chatroom.vo;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.util.Map;
import java.util.Objects;

public class User implements HttpSessionBindingListener {
    private int id;
    private String username;
    private String password;
    private String type;

    @Override
    /*
    * 增加equal和hashcode的方法，增加id，用来识别是否为同一user
    * */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    //将JavaBean于session绑定
    //登录成功后将信息存入session，该方法就会被调用
    public void valueBound(HttpSessionBindingEvent httpSessionBindingEvent) {
        System.out.println("进入了...");
        //通过事件对象获取事件源对象
        HttpSession session = httpSessionBindingEvent.getSession();
        //从servletContext中获得人员列表的Map集合
        Map<User,HttpSession> userHttpSessionMap = (Map<User, HttpSession>) session.getServletContext().getAttribute("userHttpSessionMap");
        //将用户和对应的session存入map集合中
        userHttpSessionMap.put(this,session);


    }

    @Override
    //java对象和session解除绑定
    public void valueUnbound(HttpSessionBindingEvent httpSessionBindingEvent) {
        System.out.println("退出了...");
        //获取session对象
        HttpSession session = httpSessionBindingEvent.getSession();
        //获得人员的列表
        Map<User,HttpSession> userHttpSessionMap  = (Map<User, HttpSession>) session.getServletContext().getAttribute("userHttpSessionMap");

        //将用户移除map
        userHttpSessionMap.remove(this);

    }
}
