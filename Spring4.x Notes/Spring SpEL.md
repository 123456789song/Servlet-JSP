* **Spring SpEL**
    * 核心接口
        * org.springframework.expression包及其子包，以及spel.support中
        * ExpressionParser接口用来解析表达式字符串
        * Expression接口用来计算表达式的字符串的值
    * SpEL基础表达式

    * 在Spring中使用SpEL
        * 语法：`#{<expression string>}`
        * 基于XML的配置
        ```
            ## 可以使用SpEL为Bean属性或构造函数入参注入动态值
            <bean id="numberGuess" class="org..."
                p:randomNumber="#{T(java.lang.Math).random()*100.0}" />

            ## 可以在Bean配置时引用任何一个在Spring容器中已经定义的Bean属性
                语法：Bean名称.属性名
            <bean id="bean1" class="org...."
                p:randomNumber="#{T(java.lang.Math).random()*100.0}" />
            <bean id="bean2" class="org...."
                p:randomNumber2="#{bean1.randomNumber}"
        ``` 
        * 基于注解的配置
        ```
            ## @Value注解可以标注在类的属性，方法及构造器函数上，用于从配置文件中加载一个参数值  
            @Component
            public class MyDataSource {
                @Value("${driverClassName}")
                private String driverClassName;
    
                @Value("${url}")
                private String url;
            }

            ## 使用上述引用，需要在spring配置文件中引入util工具命名空间和“property-placeholder”
            
            <util:properties id="properties" location="classpath:jdbc.properties" />
            <context:property-placeholder properties-ref="properties" />

        ```
