package cph.chatroom.action;

import cph.chatroom.service.UserService;
import cph.chatroom.utils.BaseServlet;
import cph.chatroom.vo.User;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;

@WebServlet(name = "UserServlet",urlPatterns = {"/UserServlet"})
public class UserServlet extends BaseServlet {


    //退出
    public String exit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获得session对象

        HttpSession session = request.getSession();
        //将session销毁
        session.invalidate();
        //页面转向
        response.sendRedirect(request.getContextPath()+"/index.jsp");

        return null;

    }


    //发送聊天内容
    public String sendMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //接受表单数据
        System.out.println("sendMessage invoke....");
        String from = request.getParameter("from"); // 发言人
        String face = request.getParameter("face"); // 表情
        String to = request.getParameter("to"); // 接收者
        String color = request.getParameter("color"); // 字体颜色
        String content = request.getParameter("content"); // 发言内容
        //记录发言时间
//        String sendTime = new Date().simpleDateFormat();
        long sendTime = new Date().getTime();
        //获得servletContext对象
        ServletContext application = getServletContext();
        //从servletContext获取消息
        String sourceMessage = (String) application.getAttribute("message");
        //拼接发言的内容
        sourceMessage += "<font color='blue'><strong>" + from
                + "</strong></font><font color='#CC0000'>" + face
                + "</font>对<font color='green'>[" + to + "]</font>说："
                + "<font color='" + color + "'>" + content + "</font>（"
                + sendTime + "）<br>";
        application.setAttribute("message",sourceMessage);

        return getMessage(request,response);
    }


    //获取消息的方法
    public String getMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String message = (String) getServletContext().getAttribute("message");
        if(message != null){
            response.getWriter().print(message);
        }

        return null;
    }

    public String kick(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1.接受用户id参数
        int id = Integer.parseInt(request.getParameter("id"));
        //2.获取map
        Map<User,HttpSession> userMap = (Map<User, HttpSession>) getServletContext().getAttribute("userHttpSessionMap");

        //3.获取要踢下线的用户的session，然后销毁

        //获得该用户的session，传递过来了id，得到id然后去数据库查询
        //重写了user的equals和hashcode方法，那么只有用户id相同就认为是同一个用户
        User user = new User();
        user.setId(id);
        //从map中获得用户对应的session
        HttpSession session = userMap.get(user);
        session.invalidate();

        //重定向到页面
        response.sendRedirect(request.getContextPath()+"/main.jsp");
        return null;


    }

    //检测用户的session是否过期，即是否下线
    public String check(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //从session中获取用户信息
        User existUser = (User) request.getSession().getAttribute("existUser");
        //判断session中用户是否过期，即是否有用户信息
        if(existUser==null){
            response.getWriter().print("1");
        }else
            response.getWriter().print("2");

        return null;
    }

    //用户登录
    public String login(HttpServletRequest request, HttpServletResponse response){
        //接受数据
        Map<String,String[]> map = request.getParameterMap();
        User user = new User();

        //封装数据
        try {
            BeanUtils.populate(user,map);
            //调用service处理数据
            UserService userService = new UserService();
            User existUser = userService.login(user);

            //判断登录结果
            if(existUser == null){
                request.setAttribute("msg","用户名或密码错误");
                return "/index.jsp";
            }else{
                //用户登录成功，保存信息

                /*第一个bug：同一浏览器不同用户登录。session相同，user不同*/
                //第二个用户登录之后要将之前的session销毁
                request.getSession().invalidate();

                /*第二个bug:不同浏览器登录同一个用户，第一个登录的用户的session不在map中，但没有被销毁，不能再踢下线*/
                //解决：判断用户是否以及再map集合中，若存在，则销毁之前的session.
                //a.获取ServletContext中存的map集合
                Map<User, HttpSession> userMap = (Map<User, HttpSession>) getServletContext().getAttribute("userHttpSessionMap");
                //判断此时登陆的用户是否已经存在在map集合中
                if(userMap.containsKey(existUser)){
                    //调用map中的这个用户
                    HttpSession session = userMap.get(existUser);
                    //将这个session销毁
                    session.invalidate();
                }

                //使用监听器：HttpSessionBandingListener,作用在javabean上

                request.getSession().setAttribute("existUser",existUser);
                //信息可被别人看见
                ServletContext application = getServletContext();

                String sourceMessage = "";
                if(null!=application.getAttribute("message")){
                    sourceMessage = application.getAttribute("message").toString();
                }
                //否则为第一次登录，提示登录信息
                sourceMessage += "系统公告：<font color='gray'>"+existUser.getUsername()+"进入了聊天室！</font><br/>";
                application.setAttribute("message",sourceMessage);
                response.sendRedirect(request.getContextPath()+"/main.jsp");
                return null;//不转发
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

}
