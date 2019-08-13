# Springmvc

## 1. 配置DispatcherServlet

* 传统的web.xml方式配置

```xml
<!-- 指定业务层spring的配置文件(多个用逗号分隔) -->
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath:/applicationContext.xml</param-value>
</context-param>

<!-- 通过指定的配置文件启动业务层的Spring容器 -->
<listener>
    <listener-class>
        org.springframework.web.context.ContextLoaderListener
    </listener-class>
</listener>

<!-- 配置了名为smart的DispatcherServlet，默认自动加载
    /WEB-INF/smart(servlet-Name)-servlet.xml 的配置文件，启动Web层的Spring容器 -->
<servlet>
    <servlet-name>smart</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <load-on-startup>1</load-on-startup>
</servlet>

<!-- 指定DispatcherServlet处理所有以.html为后缀的HTTP请求 -->
<servlet-mapping>
    <servlet-name>smart</servlet-name>
    <url-pattern>*.html</url-pattern>
</servlet-mapping>
```

* 在Servlet3.0环境中，可以使用编程的方式来配置Servlet容器

    ```java
    public class SmartApplicationInitializer implements WebApplicationInitializer{
        @Override
        public void onStartup(ServletContext container){
            ...
        }
    }
    ```

## 2. 一个简单的springMvc

* 开发步骤
  * 配置web.xml，指定业务层的spring配置文件，定义DispatcherServlet
  * 编写处理请求的控制器
  * 编写视图对象(使用JSP)
  * 配置SpringMVC的配置文件，使控制器和视图解析器等生效
* 步骤二 ：编写处理请求的控制器
  * 通过@Controller注解将一个POJO转化为处理请求的控制器
  * 通过@RequestMapping为控制器指定处理哪些URL请求
  
    ```java
    @Controller
    @RequestMapping("/user")
    public class UserController{

        @RequestMapping("/register")
        public String register(){
            return "user/register";
        }

        <!-- 用于处理表单提交的请求 -->
        @RequestMapping(method = RequestMethod.POST)
        public ModelAndView createUser(User user) {
            userService.createUser(user);
            ModelAndView mav = new ModelAndView();
            mav.setViewName("user/createSuccess");
            mav.addObject("user", user);
            return mav;
        }
    }
    ```
* 步骤三 : 编写视图对象
    ```xml
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <html>
    <head>
    <title>新增用户</title>
    </head>
    <body>
        <!-- 将表单提交到/user.html控制器中 -->
        <form method="post" action="<c:url value="/user.html"/>">
            <tr>
           <td>用户名：</td>
           <td><input type="text" name="userName"  value="${user.userName}"/></td>
           </tr>
        </form>
    </body>
    </html>
    ```
* 步骤四 ：配置Spring的配置文件
    ```xml
    <context:component-scan base-package="com.smart.web"/>

    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"
          p:prefix="/WEB-INF/views/" p:suffix=".jsp"/>
    ```