JDBC编程步骤概述  
1. 注册驱动/加载驱动  
2. 获取连接  
3. 创建Statement对象  
4. 执行SQL语句  
5. 处理结果集  
6. 关闭资源  

步骤详解
1.注册驱动/加载驱动，使用类加载器

    static {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }        
2.获取连接

    需要的参数：
    url = jdbc：mysql://localhost:3306/database_name
    username = root
    password = 1234

    ##其他的参数
    useUnicode=true     如果characterEncoding为gb2312或gbk，必须设为true
    characterEncoding=utf-8  指定字符编码，可设置为gb2312或gbk
    useSSL=false        与服务器进行通信时使用SSL（真／假）
    serverTimezone=GMT  覆盖时区的检测/映射，当服务器时区为映射到Java时区时使用

开始建立连接，通过DriverManager类创建数据库连接对象Connection

    try{
        Connection con = DriverManager.getConection(url,usernsme,password)
    }catch(SQLException e){
        e.printStackTrace();
    }  
3.创建Statement对象，用于执行静态SQL语句  

Statement接口提供两种执行SQL语句的方法：executeQuery 、executeUpdate,  
**executeUpdate**一般用于添加、删除、修改数据  
**executeQuery**一般用于查询数据  

    //创建一个Statemen对象来将SQL语句发送到数据库
    Statement statement = connection.createStatement();

    //添加数据
    String sql = "insert into test(id,username)values('15','张三')"; 
    int rows = statement.executeUpdate(sql);
    **返回受影响的行数

    //查询数据
    String sql = "select cid,cname from class_info";
    ResultSet resultSet = statement.executeQuery(sql);
    ** ResultSet表示数据库结果集,  

    ##执行动态SQL时用PreparedStatement
**预备语句PreparedSatement: 当查询涉及到变量时使用**   
每个宿主变量都用"?"表示

    //SQL语句
    String sql = "INSERT INTO student_info (sname,ssex,sage,cid,sbalance)VALUES(?,?,?,?,?)";

    //创建执行对象
    preparedStatement = connection.prepareStatement(sql);  
执行预备语句前，用set()方法将变量绑定到实际值上  

    //给预处理对象
    preparedStatement.setString(1,bean.getSname());
    preparedStatement.setString(2,bean.getSsex());
    preparedStatement.setInt(3,bean.getSage());
    preparedStatement.setInt(4,bean.getCid());
    preparedStatement.setBigDecimal(5,BigDecimal.ZERO);   
    ** 第一个参数是宿主变量位置，第二个参数表示赋予宿主变量的值     

4.处理查询结果集  
boolean next() 将光标移至下一行   
getxxx(): 获取结果集中的每列的值

    while(resultSet.next()){
        int cid =resultSet.getInt("cid");
        String cname=resultSet.getString("cname");
    }
    ** ResultSet初始化时被定为在第一行之前，必须先调用next()方法移到第一行
多结果集  
1. 使用execute方法执行SQL语句
2. 获取第一个结果集或更新计数
3. 重复调用getMoreResults方法移到下一个结果集
4. 当不存在更多结果集或更新计数时，完成操作

    boolean isResult = stat.execute(command)；
    boolean done = false;
    while(!done){
        if(isResult){
            ResultSet result = state.getResultSet();
            ...处理结果...
        }
        else{
            int updateCount = state.getUpdateCount();
            if(updateCount>=0)
                ...处理更新计数...
            else
                done = true;
        }
        if(!done)
            isResult = state.getMoreResults();
    }
    ** 如果多结果集下一项是结果集，execute和getMoreResults方法返回true  
    ** 如果下一项不是更新计数，getUpdateCount方法返回-1  


5.关闭资源

    if(connection != null){
        try {
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

        
**Web基础之Servlet+JDBC+JSP项目(1)**  
一. 需求  
结合JDBC和Servlet以及JSP技术，实现对新增班级信息的功能，并要求显示新增加的班级  

二.项目思路  
1. 数据库新建班级表class_info(字段：班级cid和班级名称cname)  
2. 新建一个JavaBean（ClassInfoBean）类,用来和数据库中班级表相对应  
3. 加载数据库驱动，连接数据库  
4. 前台界面编写(classInfo.jsp)  
5. 逻辑实现(ClassInfoDao 和 ClassInfoServlet)  

三.  项目整体架构  
![avatar](https://s1.ax1x.com/2018/09/17/iZ6fAI.png)  
四.源代码

ClassInfoBean.java  

    package com.skinjay.bean;

    /**
     * @Class: Servlet&JSP&JDBC
     * @Author: Song
     * @Description: 属性和数据库中的字段相对应
     * @Create: 2018-09-17 09:23
     */
    public class ClassInfoBean {
        private Integer cid;
        private String cname;
        
        public Integer getCid(){
            return cid;
        }

        public void setCid(Integer cid) {
            this.cid = cid;
        }

        public String getCname(){
            return cname;
        }

        public void setCname(String cname) {
            this.cname = cname;
        }
    }
  
  
ClassInfoDao.java    

    package com.skinjay.dao;

    import com.skinjay.bean.ClassInfoBean;
    import java.sql.*;
    import java.util.ArrayList;
    import java.util.List;

    /**
     * @Class: Servlet&JSP&JDBC
     * @Author: Song
     * @Description: 数据库驱动类加载以及定义操作类
     * @Create: 2018-09-17 09:28
     */
    public class ClassInfoDao {
    
    //加载数据库驱动
    static {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    //新增班级信息到数据库
    public void addClassInfo(ClassInfoBean bean){
        Connection connection = null;
        Statement statement = null;
        try{
            //获取连接
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?useUnicode=true&"   
            +"characterEncoding=utf-8&useSSL=false&serverTimezone=GMT","root","1234");

            //整理一条SQL语句
            String sql = "INSERT INTO class_info (cname) VALUE ('"+ bean.getCname() +"')";

            //创建SQL执行对象
            statement = connection.createStatement();

            //执行SQL语句
            int row = statement.executeUpdate(sql);
            if(row != 1){
                throw new RuntimeException("新增班级失败！");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(connection != null){
                try {
                    connection.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if(statement != null){
                try {
                    statement.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    //查询所有班级信息
    public List<ClassInfoBean> findAll(){
        Connection connection = null;
        Statement statement = null;
        List<ClassInfoBean> classInfoBeanList = new ArrayList<ClassInfoBean>();
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?useUnicode=true&"   
            +"characterEncoding=utf-8&useSSL=false&serverTimezone=GMT","root","1234");

            //SQL语句
            String sql = "select cid,cname from class_info";

            //创建执行sql的对象
            statement = connection.createStatement();

            //执行sql语句
            ResultSet resultSet = statement.executeQuery(sql);

            //遍历结果
            while(resultSet.next()){
                int cid =resultSet.getInt("cid");
                String cname=resultSet.getString("cname");
                
                //将查询结果插入List<classInfoBean>中
                ClassInfoBean bean = new ClassInfoBean();
                bean.setCid(cid);
                bean.setCname(cname);
                classInfoBeanList.add(bean);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return classInfoBeanList;
    }
    }
ClassInfoServlet.java  

    package com.skinjay.servlet;

    import javax.servlet.ServletException;
    import javax.servlet.annotation.WebServlet;
    import javax.servlet.http.HttpServlet;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import java.io.IOException;
    import java.util.List;

    import com.skinjay.bean.ClassInfoBean;
    import com.skinjay.dao.ClassInfoDao;

    @WebServlet(name = "ClassInfoServlet")
    public class ClassInfoServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;
        protected void doPost(HttpServletRequest request, HttpServletResponse response)   
        throws ServletException, IOException {
            request.setCharacterEncoding("UTF-8");

            //获取表单参数，并存入一个Classbean中
            String className = request.getParameter("className");
            ClassInfoBean bean = new ClassInfoBean();
            bean.setCname(className);

            //创建数据库操作对象
            ClassInfoDao dao = new ClassInfoDao();

            //新增班级信息到数据库
            dao.addClassInfo(bean);

            //查询所有班级信息
            List<ClassInfoBean> classInfoBeanList = dao.findAll();

            //保存查询的班级信息
            request.setAttribute("classInfo",classInfoBeanList);

            //转发
            request.getRequestDispatcher("/classInfo.jsp").forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)   
    throws ServletException, IOException {
        this.doPost(request,response);
    }
    }
calssInfo.jsp

    <%--
        Created by IntelliJ IDEA.
        User: Skinjay
        Date: 2018/9/17
        Time: 9:20
        To change this template use File | Settings | File Templates.
    --%>
    <%@ page contentType="text/html;charset=UTF-8" language="java"
        pageEncoding="utf-8" %>
    <%@ page import="com.skinjay.bean.ClassInfoBean" %>
    <%@ page import="java.util.List" %>
    <html>
        <head>
            <meta http-equiv="content-type" content="text/html;charset=utf-8">
            <title>班级信息界面</title>
            <style type="text/css">
                th{
                    text-align: center;
                }
                td{
                text-align: center;
                }
            </style>
        </head>

        <body>

        <div style="margin:0 auto;margin-top: 40px;">
            <form action="classInfoReq" method="POST">
                <center>
                    <table width="40%"; border="1"; bgcolor="#FFFFC6">
                        <caption style="margin-bottom: 20px">新增班级信息</caption>
                        <tr><td >班级名称：</td><td><input type="text" name="className"></td></tr>
                        <tr><td><input type="reset" value="重置"></td><td><input type="submit" value="确定"></td></tr>
                    </table>
                </center>
            </form>
        </div>

        <div style="margin:0 auto;margin-top: 40px;">
            <center>
                <table  width="40%"; border="1"; bgcolor="#FFFFC6">
                    <caption style="margin-bottom: 20px">班级信息列表</caption>
                    <tr><th>班级序号</th><th>班级名称</th></tr>
                    <% List<ClassInfoBean> classInfo=(List<ClassInfoBean>)request.getAttribute("classInfo");
                    if(classInfo!=null && !classInfo.isEmpty()){
                        for(ClassInfoBean classes : classInfo){
                    %>
                    <tr><td><%=classes.getCid() %></td><td><%=classes.getCname()%></td></tr>
                    <% }
                    }
                    %>
                </table>
            </center>
        </div>
        </body>
    </html>












