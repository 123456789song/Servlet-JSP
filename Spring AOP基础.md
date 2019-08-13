#Spring AOP基础  
--------------
 AOP工具的设计就是把横切的问题(如性能监视，事务管理)模块化。

* **AOP术语**  
    * 连接点(Joinpoint)  
      `程序执行的某个特定的位置，spring仅支持方法的连接点，由两个信息确定：`  
      `一是用方法表示的程序执行点，二是用相对位置表示的方位`  

    * 切点(Pointcut)  
      `AOP通过切点定位特定的连接点，切点只定位到某个方法上，不定位到具体的方位`  

    * 增强(Advice)  
      `增强是织入目标类的一段代码，除了用于描述一段程序代码外，还拥有连接点的执行方位`  
      `只有结合切点和增强，才能确定特定的连接点并实施增强逻辑`  

    * 目标对象(Target)   
      `增强逻辑的织入目标类`  

    * 引介(Introduction)   
      `是一种特殊的增强，它为类添加一些属性和方法`  

    * 织入(Weaving)   
      `织入是将增强添加到目标类的具体连接点上的过程`  

    * 代理(Proxy)   
      `一个类被AOP织入增强后，它是一个融合了袁磊和增强逻辑的代理类`  

    * 切面(Aspect)   
      `由切点和增强组成，即包括横切逻辑定义，也包括连接点的定义`  

* **AOP底层实现**  
    * JDK的动态代理  
        * 主要涉及java.lang.reflect包中的两个类：Proxy 和 InvocationHandler  
        * 通过实现InvocationHandler接口定义横切逻辑  
        * 限制只能为接口创建代理实例  

    * CGLib动态代理  
        * 为一个类创建子类，在子类中采用方法拦截所有父类的方法调用，顺势织入横切逻辑  
        * 由于是动态创建子类的方式生成代理对象，故不能对final和private方法代理  

* **创建增强类**  
    *  前置增强  
        * 前置增强实现MethodBeforeAdvice接口，该接口仅定义唯一方法before()  
        
            ```
            //spring提供的代理工厂
            ProxyFactory pf = new ProxyFactory();

            //指定对接口代理，用jdk动态代理  
            pf.setInterfaces(target.getClass().getInterfaces());

            //启动优化代理方式
            pf.setOptimize(true);

            //设置代理目标
            pf.setTarget(target);

            //为代理目标添加增强
            pf.addAdvice(advice);

            //生成代理实例 
            Waiter proxy = (Waiter)pf.getProxy(); 
            proxy.greetTo("John");
            proxy.serveTo("Tom");           
            ```
    
    * 后置增强  
        * 需要实现AfterReturningAdvice接口，实现唯一方法afterReturning()  
    
    * 环绕增强  
        * 环绕增强允许在目标类方法调用前后织入横切逻辑，综合实现前置后置增强功能  
        * 实现MethodInterceptor接口，实现唯一方法Object invoke()  
    
    * 异常抛出增强  
        * 最适用于事务管理，事务发生异常回滚事务  
        * 实现ThrowsAdvice接口，定义增强方法的名字为afterThrowsing()  
    
    * 引介增强  
        * 为目标类创建新的方法和属性，故连接点是类级别的  

* **创建切面**  
    * .....

* **自动创建代理**  
    * 

