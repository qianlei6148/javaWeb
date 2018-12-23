package io.github.qianlei.chapter2.helper;

import io.github.qianlei.chapter2.util.JdbcUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * 数据库操作助手类.
 */
public final class DatabaseHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);

    private static final QueryRunner QUERY_RUNNER = new QueryRunner();
//本地线程池， 把Connection放入
    private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<Connection>();


    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORED;

    static {
        Properties conf = JdbcUtil.loadProps("jdbc.properties");
        DRIVER = conf.getProperty("jdbc.driver");
        URL = conf.getProperty("jdbc.url");
        USERNAME = conf.getProperty("jdbc.username");
        PASSWORED = conf.getProperty("jdbc.password");

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.error("can not load jdbc driver", e);
        }
    }

    public static Connection getConnection() {
        Connection conn = CONNECTION_HOLDER.get();
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORED);
        } catch (SQLException e) {
            LOGGER.error("get connection failure", e);
        } finally {
            CONNECTION_HOLDER.set(conn);//添加进线程池
        }
        return conn;
    }

    public static void closeConnection() {
        Connection conn = CONNECTION_HOLDER.get(); //添加，所以参数中就不需要了
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.error("close connection failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();//从线程池中移除
            }
        }
    }

    /**
     * 查询实体列表.
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList;
        try {
            Connection conn = getConnection();
            entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass));
        } catch (SQLException e) {
            LOGGER.error("query entity list failure", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return entityList;
    }
}
