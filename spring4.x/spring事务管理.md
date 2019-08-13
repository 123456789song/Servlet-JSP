* **Spring对DAO的支持**
    * 数据源(DBCP,C3P0)
        * DBCP数据源
        ```
        ** MYSQL经典的8小时问题

        MYSQL默认的情况下如果发现一个连接的空闲时间超过8小时，
        则会在数据库端自动关闭这个连接，而数据源并不知道这个连接
        已经被数据库关闭了，当它将这个无用的连接返回给某个DAO时，
        DAO就会报无法获取Connection的异常

        ## 如果使用DBCP的默认配置，由于testOnBorrow属性默认为true，
            会事先检测连接是否完好，所以并不会出现“8小时问题”
        ```
        
        * C3P0数据源  
        `C3P0比DBCP拥有更丰富的配置`
        
        * 使用属性文件  
        ```
        数据源的配置信息(如数据库用户名和密码)独立到到一个属性文件中

        ## 通过<content:property-placeholder>引入属性文件
        ## 以${xxx}的方式引用属性 
        ```  

* **Spring的事务管理**
    * 数据库的事务  
    `满足ACID，即原子性，一致性，隔离性和持久性`
    * 数据库的并发问题
    ```
    ## 脏读(dirty read) :A事务读取B事务尚未提交的更改数据，并在这个
    数据的基础上进行操作如果B事务发生回滚，那么A事务读到的数据是脏数据

    ## 不可重复读 ：A事务读取了B事务已经提交的“更改数据”，可添加行级锁

    ## 幻象读 ：A事务读取B事务提交的“新增数据”，可添加表级锁

    ## 第一类丢失更新：A事务撤销时，把已经提交的B事务的更新数据覆盖了

    ## 第二类丢失更新：A事务覆盖B事务已经提交的数据，造成B事务所做的操作丢失

    ```
    * JDBC对事务的支持
        * Connection默认时自动提交的，即每条执行的SQL语句都对应一个事务
        * setAotuCommit(false)阻止自动提交Connection
        * setTransactionIsolation设置事务隔离级别
        * commit()提交事务
        * rollback()回滚事务
        
    * Spring对事务管理的支持
        * 接口(org.springframework.transaction包)
        ```
        ## TransactionDefinition：定义了Spring兼容的事务属性，对事务管理控制进行配置
            # 事务隔离
            # 事务传播
            # 事务超时
            # 只读状态

        ## TransactionStatus：代表一个事务的具体运行状态

        ## PlatformTransationManager:事务管理
            commit(TransactionStatus status),rollback(TransactionStatus status)

        ```
    * 使用XML配置声明性事务
        * 一般使用基于aop/tx命名空间的配置
    
    * 使用注解配置声明事务
        * 使用@transactional注解
        ```
        # 在需要事务管理的业务类中添加@transaction注解，就完成业务类事务属性的配置

        # 注解只提供元素据，并不能完成事务切面织入，故需要在spring配置中增强配置
        <bean id="txManager"
          class="org.springframework.jdbc.datasource.DataSourceTransactionManager"
          p:dataSource-ref="dataSource"/>

          //对标注@tansactional注解的Bean进行加工，以织入事务管理切面
          <tx:annotation-driven transaction-manager="txManager" proxy-target-class="true"/>
        ```
        

        *                 