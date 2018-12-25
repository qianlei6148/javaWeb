//package io.github.qianlei.chapter2.helper;
//
//import io.github.qianlei.chapter2.util.CollectionUtil;
//import io.github.qianlei.chapter2.util.JdbcUtil;
//import org.apache.commons.dbutils.QueryRunner;
//import org.apache.commons.dbutils.handlers.BeanHandler;
//import org.apache.commons.dbutils.handlers.BeanListHandler;
//import org.apache.commons.dbutils.handlers.MapListHandler;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//
///**
// * 数据库操作助手类.
// */
//public final class DatabaseHelperCopy {
//    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelperCopy.class);
//
//    private static final QueryRunner QUERY_RUNNER = new QueryRunner();
//    //本地线程池， 把Connection放入, 在get和close方法中进行(防止线程安全)
//    private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<Connection>();
//
//
//    private static final String DRIVER;
//    private static final String URL;
//    private static final String USERNAME;
//    private static final String PASSWORED;
//
//    static {
//        Properties conf = JdbcUtil.loadProps("jdbc.properties");
//        DRIVER = conf.getProperty("jdbc.driver");
//        URL = conf.getProperty("jdbc.url");
//        USERNAME = conf.getProperty("jdbc.username");
//        PASSWORED = conf.getProperty("jdbc.password");
//
//        try {
//            Class.forName(DRIVER);
//        } catch (ClassNotFoundException e) {
//            LOGGER.error("can not load jdbc driver", e);
//        }
//    }
//
//    public static Connection getConnection() {
//        Connection conn = CONNECTION_HOLDER.get();
//        try {
//            conn = DriverManager.getConnection(URL, USERNAME, PASSWORED);
//        } catch (SQLException e) {
//            LOGGER.error("get connection failure", e);
//        } finally {
//            CONNECTION_HOLDER.set(conn);//添加进线程池
//        }
//        return conn;
//    }
//
//    public static void closeConnection() {
//        Connection conn = CONNECTION_HOLDER.get(); //添加，所以参数中就不需要了
//        if (conn != null) {
//            try {
//                conn.close();
//            } catch (SQLException e) {
//                LOGGER.error("close connection failure", e);
//                throw new RuntimeException(e);
//            } finally {
//                CONNECTION_HOLDER.remove();//从线程池中移除
//            }
//        }
//    }
//
//    /**
//     * 查询实体列表.
//     */
//    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
//        List<T> entityList;
//        try {
//            Connection conn = getConnection();
//            entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass));
//        } catch (SQLException e) {
//            LOGGER.error("query entity list failure", e);
//            throw new RuntimeException(e);
//        } finally {
//            closeConnection();
//        }
//        return entityList;
//    }
//
//    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
//        T entity;
//        try {
//            Connection conn = getConnection();
//            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), params);
//        } catch (SQLException e) {
//            LOGGER.error("query entity failure", e);
//            throw new RuntimeException();
//        } finally {
//            closeConnection();
//        }
//        return entity;
//
//    }
//
//    /**
//     * 查询不一定来自单表，所以输入sql与动态参数，输入一个list, 其中Map表示列名与列值得映射关系.
//     */
//    public static List<Map<String, Object>> executeQuery(String sql, Object...params) {
//        List<Map<String, Object>> result;
//        try {
//            Connection conn = getConnection();
//            result = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
//        } catch (SQLException e) {
//            LOGGER.error("execute query failure", e);
//            throw new RuntimeException();
//        } finally {
//            closeConnection();
//        }
//        return result;
//    }
//
//    /**
//     * update insert delete
//     */
//    public static int executeUpdate(String sql, Object... params) {
//        int rows = 0;
//        try {
//            Connection conn = getConnection();
//            rows = QUERY_RUNNER.update(conn, sql, params);
//        } catch (SQLException e) {
//            LOGGER.error("execute update failure", e);
//            throw new RuntimeException();
//        } finally {
//            closeConnection();
//        }
//        return rows;
//    }
//
//    /**
//     * 插入、更新、删除实体
//     */
//    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap) {
//        if (CollectionUtil.isEmpty(fieldMap)) {
//            LOGGER.error("can not insert entity: fieldMap is empty!");
//            return false;
//        }
//        String sql = "insert into " + getTableName(entityClass) ;
//        StringBuilder columns = new StringBuilder("(");
//        StringBuilder values = new StringBuilder("(");
//        for (String fieldName : fieldMap.keySet()) {
//            columns.append(fieldName).append(", ");
//            values.append("?, ");
//        }
//        columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
//        values.replace(values.lastIndexOf(", "), values.length(), ")");
//        sql += columns + " values " + values;
//
//        Object[] params = fieldMap.values().toArray();
//        return executeUpdate(sql, params) == 1;
//
//    }
//    public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String, Object> fieldMap) {
//        if (CollectionUtil.isEmpty(fieldMap)) {
//            LOGGER.error("can not update entity: fieldMap is empty!");
//            return false;
//        }
//        String sql = "update " + getTableName(entityClass) + " set ";
//        StringBuilder columns = new StringBuilder();
//        for (String fieldName : fieldMap.keySet()) {
//            columns.append(fieldName).append("=?, ");
//        }
//        sql += columns.substring(0, columns.lastIndexOf(", ")) + " where id=?";
//
//        List<Object> paramList = new ArrayList<Object>();
//        paramList.addAll(fieldMap.values());
//        paramList.add(id);
//
//        Object[] params = paramList.toArray();
//        return executeUpdate(sql, params) == 1;
//
//    }
//
//    public static <T> boolean deleteEntity(Class<T> entityClass, long id) {
//        String sql = "delete from " + getTableName(entityClass) + " where id=?";
//        return executeUpdate(sql, id) == 1;
//    }
//
//    private static String getTableName(Class<?> entityClass) {
//        return entityClass.getSimpleName().toLowerCase();
//    }
//
//}
