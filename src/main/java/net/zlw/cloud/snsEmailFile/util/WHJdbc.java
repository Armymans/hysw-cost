package net.zlw.cloud.snsEmailFile.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class WHJdbc {

    // 正式环境
    private static String url = "jdbc:oracle:thin:@10.61.97.26:1521:orcl";
    // 测试环境
    //private static String url = "jdbc:oracle:thin:@10.61.95.29:1521:orcl";
    private static String userName = "ZJPT";
    private static String password = "ZJPTMM";

    public static ResultSet getMaterialInfo() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection conn = (Connection) DriverManager.getConnection(url, userName, password);
        String sql = "SELECT * FROM V_TOZJPT_INVBASDOC";
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static ResultSet getSaleOrderInfo() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection conn = (Connection) DriverManager.getConnection(url, userName, password);
        String sql = "SELECT * FROM V_TOZJPT_SALEORDER";
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    // 获取芜湖物料存货视图
    public static ResultSet getWhMaterialInfo() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection conn = (Connection) DriverManager.getConnection(url, userName, password);
        String sql = "SELECT * FROM V_APITOZJPT_INVBASDOC";
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    // 获取芜湖物料订单视图
    public static ResultSet getWhSaleOrderInfo() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection conn = (Connection) DriverManager.getConnection(url, userName, password);
        String sql = "SELECT * FROM V_APITOZJPT_SALEORDER";
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

}
