package com.example.myapplication.net;

import com.example.myapplication.context;
import com.example.myapplication.pwd;

import java.net.URL;

public interface ClentUrl {
//本地连接URL
//    http://localhost:8080/context/
    final String LOCAL = "http://192.168.0.100:9990/bdqnBack";
//    数据库连接URL远程
//    http://fnnso710945.java.cdnjsp.wang
     final String FREE = "http://fnnso710945.java.cdnjsp.wang";
     final static String URL = LOCAL;
//    注册的URL
    public static String SENDINSERTUSER = URL+"/UserServlet?op=insert";
//    登入的URL
    public static String LOGINURL = URL+"/UserServlet?op=login";


//    查询预留一个模糊查询
//    定义参数
    public static String USERINFOMESSAGE = URL+"/MessageServlet?op=sel";


    public static String USERCONN = URL+"/UserServlet?op=pwdforget";

    public static String UPDATEPWD = URL+"/UserServlet?op=updatepwd";

//    图片img
    public static String TUPIANIMG = URL+"/img/";


//    根据密码和名称查询id编号
    public static String BYID = URL+"/UserServlet?op=byId";
    public static String INSERTMESSAGES = URL+"/MessageServlet";

//    加载用户信息的url
    public static String LOADUSERINFO = URL+"/MessageServlet?op=userinfo";

//    删除帖子
    public static String DELMESSAGE = URL+"/MessageServlet?op=delMessage";
    public static String USERFREEBACK = URL+"/FeedbackServlet?op=insert";
//&title=%E6%B5%8B%E8%AF%95&userId=7&message=%E4%BD%A0%E8%A7%89%E5%BE%97%E5%91%A2&userQQ=247974414


//    收藏帖子
//    http://localhost:8080/context/CollectServlet?op=add&
    public static String SC = URL+"/CollectServlet?op=add";

//    显示收藏的信息
//    http://localhost:8080/context/CollectServlet?op=collectSel&id=7
    public static String SCUSER = URL+"/CollectServlet?op=collectSel";
//    判断是否收藏还是取消收藏
//    http://localhost:8080/context/CollectServlet?op=assicPanQ&messageId=81&userId=7
    public static String SCASSICE = URL+"/CollectServlet?op=assicPanQ";

//    取消收藏
    public static String SCQX = URL+"/CollectServlet?op=delSc";

//    设置分类
    public static String TYPE = URL+"/TypeServlet?op=sel";


    public final static String ISUPDATA = URL+"/AppInfoServlet?op=isUpdate";
}