# 基于@AspectJ和Schema的AOP

* **使用AspectJ必须保证所使用的java是5.0以上**  

* **通过spring配置文件使用@AspectJ切面**  

    ```java
    //需要引入aop命名空间

    //自动为匹配了@AspectJ切面的Bean创建代理
    <aop:aspectj-autoproxy/>  

    <bean id="waiter" class="com.smart.NaiveWaiter"/>
    <bean class="com.smart.aspectj.basic.EnableSellerAspect"/>
    ```

* **@AspectJ语法基础**  
  * 切点表达函数：execution(* greetTo(..))，其中execution称为函数  

    * **增强类型**  
      * @Before         前置
      * @AfterReturning 后置
      * @Around         环绕
      * @AfterThrowing  抛出
      * @After          final增强
      * @DeclareParents 引介增强

* **切点函数详解**  
  * @annotation() :表示标注了某个注解的所有方法  

    ```java
    @AfterReturning("@annotation(com.smart.anno.NeedTest)")

    ## 该后置增强应用到标注了NeedTest的目标方法中
    ```
  
  * @execution() :表示满足某一匹配模式的所有目标类方法连接点  
    语法如下：
    ```java
    execution(<返回类型模式> <方法名模式> (<参数模式>))

    ```

  * 通过方法签名定义切点
    ```java
    * execution(public * *(..)) :匹配所有的目标类的public方法
    * execution(* *To(..)) :匹配目标类所有以To为后缀的方法
    ```  

  * 通过类定义切点
    ```jva
    * execution(* com.smart.Waiter.*(..)):匹配Waiter接口的所有方法
    * execution(* com.smart.Waiter+.*(..)):匹配Waiter接口及所有实现类的方法  
    ```
  * 通过类包定义切点  
    ```java
    * 在类名模式串中，“.* ”代表包下所有的类，“..*”表示包和子孙包的所有类
    * execution(* com.smart.*(..)):匹配com.smart包下所有类的所有方法
    * execution(* com.smart..*(..)):匹配smar包和子孙包所有类的所有方法
    ```
  * 通过方法入参定义切点
    ```java
    ## “*”表示任意类型的参数，“..”任意类型的参数且参数个数不限

    * execution(* joke(String,*)):匹配第一入参为String，第二入参类型不限
    * execution(* joke(String,..)):匹配第一入参为String，后面可以有任意入参且类型任意
    ```

    * args()和@args()
      * args()
      ```java
      * 该函数接受一个类名，表示目标类入参对象是指定类(包含子类)，匹配切点
      * args(com.smart.Waiter)等价于execution(* *(xom.smart.Waiter+))
      ```

      * @args()
      ```java
      * 该函数就接受一个注解类的类名
      ```
    * @within()和@target()
    ```java
    * @target(M):匹配任意标注了@M的目标类
    * @within(M):匹配标注了@M的类及子孙类

    ## 注意：如果标注@M著注解的是一个接口，则所有实现该接口的类并不匹配@within()  
    && 原因：@target()，@within(),@annotation()函数都是针对目标类的，并非针对运行时的引用类型  
    ```
        * target()
        ```java
        * target(M):表示目标类按类型匹配于M，则目标类的所有方法都匹配切点
        * target(com.smart.Waiter)：NaiveWaiter，NaughtyWaiter的所有方法都匹配切点，包括那些未在Waiter接口定义的方法
        ```

* **@AspectJ进阶**
  * 切点的复合运算
    * 命名切点
    ```java
    * @Pointcut("execution(* greetTo(..))")
    * private void inPackge(){}

    ## 方法的修饰符(private,protected,public,)控制切点的可引用性
    ```
    * 访问连接点的信息
      * org.aspectj.lang.JoinPoint接口表示目标类的连接点对象
      * org.aspectj.lang.ProceedingJoinPoint表示**环绕增强**的连接点对象
      ```java
      @Around("execution(* greetTo(..)) && target(com.smart.NaiveWaiter)")

      public void joinPointAccess(ProceedingJoinPoint pjp) throws Throwable{
        //访问连接点的信息
        System.out.println("args[0]:"+pjp.getArgs()[0]);
        System.out.println("signature:"+pjp.getTarget().getClass());

        //执行目标对象的方法
        pjp.proceed();
      }
      ```
