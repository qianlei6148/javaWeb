package io.github.qianlei.framework;

/**
 * 提供相关配置项常量.
 */
public interface ConfigConstant {
    String CONFIG_FILE = "smart.properties"; //jdbc配置文件

    //
    String JDBC_DRIVER = "smart.framework.jdbc.driver";
    String JDBC_URL = "smart.framework.jdbc.url";
    String JDBC_USERNAME = "smart.framework.jdbc.username";
    String JDBC_PASSWORD = "smart.framework.jdbc.password";

    String APP_BASE_PACKAGE = "smart.framework.app.base_package";//项目基础包名
    String APP_JSP_PATH = "smart.framework.app.jsp_path";//JSP路径
    String APP_ASSET_PATH = "smart.framework.app.asset_path";//表示静态次元文件的基础路径
}
