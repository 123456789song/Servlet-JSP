# ORM框架

## 1. ORM框架是什么

> 对象关系映射，目前数据库大部分是关系型数据库，ORM主要是把数据库中的关系数据映射称为程序中的对象。

## 2. 使用Spring所提供的整合框架的好处

* 方便基础设施的搭建
* 异常封装
* 统一的事务管理
* 允许混合使用多个ORM框架
* 方便单元测试

## 3. 在Spring中使用Hibernate

* 配置SessionFactory
  * 零过渡障碍的配置方式
    * 首先对象关系的映射文件xxx.hbm.xml
    * 然后通过hibernate的配置文件hibernate.cfg.xml将映射文件组装起来
    * 最后通过两行经典代码得到SessionFactory的实例
      ```java
      Configuration cfg = new Configuration().configure(hibernate.cfg.xml)
      SessionFactory sessionFactory = cfg.buildSessionFactory();
      ```

    * hibernate.cfg.xml配置文件拥有创建基础设施所需的配置信息
      ```xml
      <!-- 一个最简单的hibernate配置文件 -->

      <?xml version='1.0' encoding='utf-8'?>
      <!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">

      <hibernate-configuration>
      <session-factory>
        <!-- 配置数据源 -->
        <property name="connection.driver_class">
            com.mysql.jdbc.Driver</property>
        <property name="connection.url">
            jdbc:mysql://localhost:3306/sampledb</property>
        <property name="connection.username">root</property>
        <property name="connection.password">1234</property>

        <!-- 控制属性 -->
        <property name="dialect">
            org.hibernate.dialect.MySQLDialect</property>
        <property name="show_sql">true</property>
        <property name="format_sql">true</property>
        <property name="current_session_context_class">thread</property>

        <!-- 映射文件 -->
        <mapping resource="com/smart/domain/Forum.hbm.xml" />
      </session-factory>
      </hibernate-configuration>
      ```
    * 在Spring中指定一个hibernate配置文件创建一个SessionFactory实例
      ```xml
      <bean id="SessionFactory"
            class="org.springframework.orm.hibernate5.LocalSessionFactoryBean"
            p:configLocation="classpath:hibernate.cfg.xml">
      ```
  * Spring风格的Hibernate配置
    * LocalSessionFactoryBean中集成的数据源，映射文件和控制文件，完全替代的hibernate.cfg.xml
    ```xml
      <!-- 用Spring中的数据源 -->
      <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource"
          destroy-method="close"
          p:driverClassName="${jdbc.driverClassName}"
          p:url="${jdbc.url}"
          p:username="${jdbc.username}"
          p:password="${jdbc.password}"/>

      <!-- 配置信息 -->
      <bean id="sessionFactory"
          class="org.springframework.orm.hibernate4.LocalSessionFactoryBean"
          p:dataSource-ref="dataSource"
          p:mappingDirectoryLocations="classpath:/com/smart/domain">

        <!-- 指定Hibernate的配置属性 -->
        <property name="hibernateProperties">
            <props>
                <prop key="hibernate.dialect">
                    org.hibernate.dialect.MySQLDialect
                </prop>
                <prop key="hibernate.show_sql">
                    true
                </prop>
            </props>
        </property>
      </bean>
    ```
* 使用HibernateTemplate
* 处理LOB类型数据
* 添加Hibernate事件监听
* 使用注解配置,支持JPA注解
  * 在领域对象进行ORM配置
    ```java
      @Entity
      @Table(name="T_FORUM")
      public class Forum implements Serializable{
        @Id
        @Column(name = "FORUM_ID")
        private int forumId;

        @Column(name = "FORUM_NAME")
        private String forumName;

        @Column(name = "FORUM_DESC")
        private String forumDesc;
      }
    ```
  * 创建基于JPA注解的SessionFactory,使用Spring提供的AnnotationSessionFactoryBean
* 事务处理

## 4. 在Spring中使用MyBatis

* 配置SqlMapClient
  * SqlSessionFactory为核心对象，可以通过SqlSessionBuilder获得
  * 使用配置文件装配SQL映射文件，同时定义一些控制属性信息

  ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE configuration
    PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-config.dtd">

    <configuration>
    <!-- 提供可控制MyBatis运行行为属性信息 -->
    <settings>
        <setting name="lazyLoadingEnabled" value="false" />
    </settings>

    <!-- 定义全限定类名的别名，在映射文件中可以通过别名代替具体的类名 -->
    <typeAliases>
        <typeAlias alias="Forum" type="com.smart.domain.Forum" />
        <typeAlias alias="Topic" type="com.smart.domain.Topic" />
        <typeAlias alias="Post" type="com.smart.domain.Post" />
    </typeAliases>
    </configuration>
  ```

  * 映射文件

  ```xml
    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

    <!-- 映射所在的命名空间 -->
    <mapper namespace="com.smart.dao.mybatis.ForumMybatisDao">

      <!-- 定义语句映射，parameterType指定传入的参数对象 -->
      <select id="getForum" resultType="Forum" parameterType="int" >
        SELECT
         forum_id  forumId,
         forum_name  forumName,
         forum_desc  forumDesc
        FROM t_forum
        WHERE forum_id = #{forumId}
      </select>

      <insert id="addForum" parameterType="Forum">
        INSERT INTO t_forum(forum_id,forum_name,forum_desc)
        VALUES(#{forumId},#{forumName}, #{forumDesc})
      </insert>
      <update id="updateForum" parameterType="Forum">
        UPDATE t_forum f
        SET forum_name=#{forumName},forum_desc=#{forumDesc}
        WHERE f.forum_id = #{forumId}
      </update>
    </mapper>

  ```

* 在Spring中配置MyBatis
  * 使用mybatis-spring整合类包

  ```xml
    <?xml version="1.0" encoding="UTF-8" ?>
    <beans ...>
    <context:component-scan base-package="com.smart" resource-pattern="**/mybatis/*.class"/>

    <context:property-placeholder location="classpath:jdbc.properties"/>
    <bean id="dataSource"
          class="org.apache.commons.dbcp.BasicDataSource"
          destroy-method="close"
          p:driverClassName="com.mysql.jdbc.Driver"
          p:url="jdbc:mysql://localhost:3306/sampledb"
          p:username="root"
          p:password="123456"/>

    <!-- 需要注入数据源，并指定MyBatis的总装配配置文件，再按资源路径扫描SQL映射文件 -->
    <bean id="sqlSessionFactory"
          class="org.mybatis.spring.SqlSessionFactoryBean"
          p:dataSource-ref="dataSource"
          p:configLocation="classpath:myBatisConfig.xml"
          p:mapperLocations="classpath:com/smart/domain/mybatis/*.xml"/>
  ```

* 编写MyBatis的DAO
  * 使用SqlSessionTemplate
    * 首先在applicationContext-mybatis.xml配置好SqlSessionTemplate
    ```xml
      <bean class="org.mybatis.spring.SqlSessionTemplate">
        <constructor-arg ref="sqlSessionFactory" />
      </bean>
    ```  
    * 然后使用SqlSessionTemplate调用SQL映射项完成数据访问
    ```java
      package com.smart.dao.mybatis;

      import com.smart.domain.Forum;
      import org.mybatis.spring.SqlSessionTemplate;
      import org.springframework.beans.factory.annotation.Autowired;
      import org.springframework.stereotype.Repository;

      @Repository
      public class ForumMybatisTemplateDao {

        private SqlSessionTemplate sessionTemplate;

        @Autowired
        public void setSessionTemplate(SqlSessionTemplate sessionTemplate) {
            this.sessionTemplate = sessionTemplate;
        }

        <!-- selectOne方法调用命名空间ForumMyBatisDao的映射项id为getForum的SQL映射项，并传入参数 -->
        public Forum getForum(int forumId) {
            return (Forum) sessionTemplate.selectOne(
                "com.smart.dao.mybatis.ForumMybatisDao.getForum",
                forumId);
        }
    }
    ```
  * 使用映射接口

### 5. DAO层的设计

* DAO基类设计
* 查询接口方法的设计
* 分页查询接口设计