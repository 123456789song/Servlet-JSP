#Sping高级容器  
-------------
**使用外部属性文件**  
1.新建配置文件  

    ## jdbc.properties
    dbName=sampledb
    driverClassName=com.mysql.jdbc.Driver
    url=jdbc:mysql://localhost:3306/${dbName}
    userName=root
    password=1234
2.在XML中引入属性文件  

    ** 第一种 需要定义一些高级功能时用到，如属性加密等
    <bean class="org.springframework.beans.factory.config.PropertyPlaceholder.Configurer"
    p:location="classpath:com.smart.jdbc.properties"
    p:fileEncoding="utf-8" />

    ** 第二种
    <context:property-placeholder
         location="classpath:com/smart/placeholder/jdbc.properties"/>
3.引用属性  

    <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource"
        destroy-method="close" 
        p:driverClassName="${driverClassName}" 
        p:url="${url}"
        p:username="${userName}" 
        p:password="${password}" />
4.基于注解及基于Java类的配置文件中引用属性  

    @Component
    public class MyDataSource {
        @Value("${driverClassName}")
        private String driverClassName;
    
        @Value("${url}")
        private String url;
    
        public String toString(){
             return ToStringBuilder.reflectionToString(this);
        }   
    }
    ** 在配置文件，即注解@Configuration中也是一样
5.引用Bean的属性值  

    在XML中：
    #{sysConfig.value}
    在基于注解和基于Java类配置的Bean中：
    @Value("#{beanName.propName}")


