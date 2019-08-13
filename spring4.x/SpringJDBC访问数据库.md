# 使用Spring JDBC访问数据库

* 在Dao中使用JdbcTemplate
  * 在Dao类中使用JDBCTemplate

  ```java
  //声明一个DAO
  @Repository
  public class ForumDao {

  private JdbcTemplate jdbcTemplate;

  //注入JdbcTemplate实例
  @Autowired
  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void initDb() {
    String sql = "create table t_user(user_id int primary key,user_name varchar(60))";
    jdbcTemplate.execute(sql);
  }
  ```

  * 在XML配置好后Dao

  ```xml
  <!-- 扫描包以注册注解声明的Bean -->
  <context:component-scan base-package="com.smart"/>
  
  <!-- 配置数据源 -->
  <context:property-placeholder location="classpath:jdbc.properties" />

  <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource"
    destroy-method="close"
    p:driverClassName="${jdbc.driverClassName}"
    p:url="${jdbc.url}"
    p:username="${jdbc.username}"
    p:password="${jdbc.password}" />
  
  <!-- 声明Jdbc Template的Bean -->
  <bean id="jdbcTemplate"
    class="org.springframework.jdbc.core.JdbcTemplate"
    p:dataSource-ref="dataSource"/>
  ```

  * 在Spring配置DAO一般分为4个步骤
    * 定义DataSource
    * 定义JdbcTemplate
    * 声明一个抽象的Bean，以便所有的DAO复用配置JdbcTemplate属性的配置
    * 配置具体的DAO

* 基本的数据操作
  * 更改数据
    * 使用JdbcTemplate的update()方法

    ```java
    public void addForum(final Forum forum) {

        final String sql = "INSERT INTO t_forum(forum_name,forum_desc) VALUES(?,?)";
        Object[] params = new Object[] { forum.getForumName(),
        forum.getForumDesc() };

        // 方式1,不显式指定对应字段的数据类型
        jdbcTemplate.update(sql, params);

        // 方式2，显式指定对应的字段类型
        jdbcTemplate.update(sql, params,new
        int[]{Types.VARCHAR,Types.VARCHAR});
    }
    ```

  * 批量更改数据

  * 查询数据
    * 流化处理结果集：RowCallbackHandler
    * 批量化处理结果集：RowMapper<T>
    ```java
    ### 多条数据结果集处理
    public List<Forum> getForums(final int fromId, final int toId) {
        String sql = "SELECT forum_id,forum_name,forum_desc FROM t_forum WHERE forum_id between ? and ?";

        // 方法1：使用RowCallbackHandler接口
        final List<Forum> forums = new ArrayList<Forum>();
        jdbcTemplate.query(sql,new Object[]{fromId,toId},new
        RowCallbackHandler(){ public void processRow(ResultSet rs) throws
        SQLException { Forum forum = new Forum();
        forum.setForumId(rs.getInt("forum_id"));
        forum.setForumName(rs.getString("forum_name"));
        forum.setForumDesc(rs.getString("forum_desc")); forums.add(forum);
        }}); return forums;

        //方法2：使用RowMapper<T>
        return jdbcTemplate.query(sql, new Object[] { fromId, toId },
            new RowMapper<Forum>() {
                public Forum mapRow(ResultSet rs, int index)
                    throws SQLException {
                        Forum forum = new Forum();
                        forum.setForumId(rs.getInt("forum_id"));
                        forum.setForumName(rs.getString("forum_name"));
                        forum.setForumDesc(rs.getString("forum_desc"));
                    return forum;
                }
            });
    ```

  * 查询单值数据
    * int类型：int queryForInt(String sql)
    * long类型:long queryForLong(String sql)
    * 其他类型

  * 自增键和行集
    * 