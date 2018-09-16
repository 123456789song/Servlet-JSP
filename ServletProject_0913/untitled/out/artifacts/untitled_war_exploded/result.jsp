<%--
  Created by IntelliJ IDEA.
  User: Skinjay
  Date: 2018/9/13
  Time: 15:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1 align="center">Beer Recommendations JSP</h1>
<p>

<%
        List styles = (List)request.getAttribute("styles");
        Iterator it = styles.iterator();
        while (it.hasNext()){
            out.print("<br>try" + it.next());
        }
%>
</body>
</html>
