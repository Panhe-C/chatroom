<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="safe.jsp"%>

<html>
<head>
<title>聊天室</title>
<link href="CSS/style.css" rel="stylesheet">
    <script type="text/javascript" src="${ pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
    <script type="text/javascript">


        var sysBBS = "<span style='font-size:14px; line-height:30px;'>欢迎光临心之语聊天室，请遵守聊天室规则，不要使用不文明用语。</span><br><span style='line-height:22px;'>";var sysBBS = "<span style='font-size:14px; line-height:30px;'>欢迎光临心之语聊天室，请遵守聊天室规则，不要使用不文明用语。</span><br><span style='line-height:22px;'>";

        window.setInterval("showContent()",1000);//一秒刷新显示聊天内容
        window.setInterval("showOnLine();",10000);//十秒间隔显示在线列表人员
        window.setInterval("check();",1000);//检测用户的session是否过期

        //Jquery
        //相当于window.onload
        $(function () {
            showOnLine();
            showContent();
            check();
        });


        //检测用户session是否过期
        function check() {
            $.post("${pageContext.request.contextPath}/UserServlet?method=check",function (data) {
                if(data==1){
                    alert("您已被踢下线！");
                    //回到登录页面
                    window.location = "index.jsp";
                }
            })
            
        }
        // 显示在线人员列表
        function showOnLine(){
            // 异步发送请求 获取在线人员列表
            // Jquery发送异步请求
            $.post("${pageContext.request.contextPath}/online.jsp?"+new Date().getTime(),function(data){
                // $("#online") == document.getElementById("online");
                $("#online").html(data);
            });
        }

        function send(){//发送消息
            if(form1.to.value==""){
                alert("请选择聊天对象！");
                return false;
            }
            if(form1.content.value==""){//发送框里的内容
                alert("发送消息不能为空！");
                form1.content.focus();
                return false;
            }
            //$("#form1").serialize():让表单中所有的元素都提交.
            //jquery提交数据.{id:1,name:aa,age:25}
            $.post("${pageContext.request.contextPath}/UserServlet?" + new Date().getTime(),$("#form1").serialize(),function (data) {
                $("#content").html(sysBBS+data+"</span>");
            });


        }
        
        //显示聊天内容
        function showContent() {
            $.post("${pageContext.request.contextPath}/UserServlet?"+new Date().getTime(),{'method':'getMessage'},function (data) {
                //调用userServlet中的getMessage方法，得到返回内容写到标签为content的位置处
                $("#content").html(sysBBS+data);
            })//加时间戳防止缓存
            
        }


        function set(selectPerson){//自动添加聊天对象，聊天对象不能是自己
            if(selectPerson != "${existUser.username}"){//列表的用户与登录用户不一样
                form1.to.value = selectPerson;//下方表单里的id为to的值设为选择聊天的用户名
            }
            else{
                alert("请重新选择聊天对象");
            }
        }

        //退出聊天室
        function exit() {
            alert("欢迎下次登录！");

            //页面路径指向UserServlet
            window.location.href = "${pageContext.request.contextPath}/UserServlet?method=exit";

        }


    </script>


</head>
<body>

	<table width="778" height="150" border="0" align="center"
		cellpadding="0" cellspacing="0" background="images/top.jpg">
		<tr>
			<td>&nbsp;</td>
		</tr>
	</table>
	<table width="778" height="276" border="0" align="center"
		cellpadding="0" cellspacing="0">
		<tr>
			<td width="165" valign="top" bgcolor="#f6fded" id="online" style="padding:5px">在线人员列表</td>
			<td width="613" height="200px" valign="top"
				background="images/main_bj.jpg" bgcolor="#FFFFFF"
				style="padding:5px; ">
				<div style="height:290px; overflow:hidden" id="content">聊天内容</div></td>

		</tr>
	</table>
	<table width="778" height="95" border="0" align="center"
		cellpadding="0" cellspacing="0" bordercolor="#D6D3CE"
		background="images/bottom.jpg">

		<form action="" id="form1" name="form1" method="post">
			<input type="hidden" name="method" value="sendMessage"/>
			<tr>
				<td height="30" align="left">&nbsp;</td>
				<td height="37" align="left">
				<input name="from" type="hidden" value="${existUser.username}">[${existUser.username} ]对 
				<input name="to" type="text" value="" size="35" readonly="readonly"> 表情 
				<select name="face" class="wenbenkuang">
						<option value="无表情的">无表情的</option>
						<option value="微笑着" selected>微笑着</option>
						<option value="笑呵呵地">笑呵呵地</option>
						<option value="热情的">热情的</option>
						<option value="温柔的">温柔的</option>
						<option value="红着脸">红着脸</option>
						<option value="幸福的">幸福的</option>
						<option value="嘟着嘴">嘟着嘴</option>
						<option value="热泪盈眶的">热泪盈眶的</option>
						<option value="依依不舍的">依依不舍的</option>
						<option value="得意的">得意的</option>
						<option value="神秘兮兮的">神秘兮兮的</option>
						<option value="恶狠狠的">恶狠狠的</option>
						<option value="大声的">大声的</option>
						<option value="生气的">生气的</option>
						<option value="幸灾乐祸的">幸灾乐祸的</option>
						<option value="同情的">同情的</option>
						<option value="遗憾的">遗憾的</option>
						<option value="正义凛然的">正义凛然的</option>
						<option value="严肃的">严肃的</option>
						<option value="慢条斯理的">慢条斯理的</option>
						<option value="无精打采的">无精打采的</option>
				</select> 说：</td>
				<td width="189" align="left">&nbsp;&nbsp;字体颜色： <select
					name="color" size="1" class="wenbenkuang" id="select">
						<option selected>默认颜色</option>
						<option style="color:#FF0000" value="FF0000">红色热情</option>
						<option style="color:#0000FF" value="0000ff">蓝色开朗</option>
						<option style="color:#ff00ff" value="ff00ff">桃色浪漫</option>
						<option style="color:#009900" value="009900">绿色青春</option>
						<option style="color:#009999" value="009999">青色清爽</option>
						<option style="color:#990099" value="990099">紫色拘谨</option>
						<option style="color:#990000" value="990000">暗夜兴奋</option>
						<option style="color:#000099" value="000099">深蓝忧郁</option>
						<option style="color:#999900" value="999900">卡其制服</option>
						<option style="color:#ff9900" value="ff9900">镏金岁月</option>
						<option style="color:#0099ff" value="0099ff">湖波荡漾</option>
						<option style="color:#9900ff" value="9900ff">发亮蓝紫</option>
						<option style="color:#ff0099" value="ff0099">爱的暗示</option>
						<option style="color:#006600" value="006600">墨绿深沉</option>
						<option style="color:#999999" value="999999">烟雨蒙蒙</option>
				</select>
				</td>
				<td width="19" align="left"><input name="scrollScreen"
					type="checkbox" class="noborder" id="scrollScreen"
					onClick="checkScrollScreen()" value="1" checked>
				</td>
			</tr>
			<tr>
				<td width="21" height="30" align="left">&nbsp;</td>
				<td width="549" align="left">
				<input name="content" type="text" size="70"
					onKeyDown="if(event.keyCode == 13 && event.ctrlKey){send();}">
					<input name="Submit2" type="button" class="btn_grey" value="发送"
					onClick="send()">
				</td>
				<td align="right"><input name="button_exit" type="button"
					class="btn_grey" value="退出聊天室" onClick="exit()">
				</td>
				<td align="center">&nbsp;</td>
			</tr>
			<tr>
				<td height="30" align="left">&nbsp;</td>
				<td colspan="2" align="center" class="word_dark">&nbsp;All
					CopyRights reserved 2014 北京传智播客教育科技有限公司</td>
				<td align="center">&nbsp;</td>
			</tr>
		</form>
	</table>
</body>
</html>
