package net.zlw.cloud.snsEmailFile.util;

import java.sql.*;

/**
 * @Author Armyman
 * @Description //获取采购平台数据
 * @Date 16:01 2020/11/16
 **/
public class CaiGouJdbc {
	private static String url = "jdbc:sqlserver://10.61.97.20:1433;Databasename=yunbidding";
	private static String userName = "sa";
	private static String password = "hywater@2018";

	public ResultSet getBidInfoSimplify() throws Exception {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection conn = (Connection) DriverManager.getConnection(url, userName, password);
		String sql = "SELECT * FROM plat_archive_project where status = 1";
		PreparedStatement statement = conn.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();
		return resultSet;
	}

	public ResultSet getSectionInfoSimplify(String projectCode) throws Exception {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection conn = (Connection) DriverManager.getConnection(url, userName, password);
		String sql = "SELECT * FROM plat_bid_section where tender_project_code = '"+projectCode+"'";
		PreparedStatement statement = conn.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();
		return resultSet;
	}

	public ResultSet getSectionInfoSimplify2(String projectCode) throws Exception {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection conn = (Connection) DriverManager.getConnection(url, userName, password);
		String sql = "SELECT * FROM plat_bid_section where bid_section_code = '"+projectCode+"'";
		PreparedStatement statement = conn.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();
		return resultSet;
	}

	public ResultSet getWinInfoSimplify(String projectCode) throws Exception {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection conn = (Connection) DriverManager.getConnection(url, userName, password);
		String sql = "SELECT * FROM plat_win_bid where bid_section_code = '"+projectCode+"'";
		PreparedStatement statement = conn.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();
		return resultSet;
	}

	public ResultSet getCompanySimplify(String companyId) throws Exception {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection conn = (Connection) DriverManager.getConnection(url, userName, password);
		String sql = "SELECT * FROM plat_sys_company where id = '"+companyId+"'";
		PreparedStatement statement = conn.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();
		return resultSet;
	}

	public ResultSet getUserInfoSimplify(String userId) throws Exception {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection conn = (Connection) DriverManager.getConnection(url, userName, password);
		String sql = "SELECT * FROM plat_sys_user where id = '"+userId+"' and user_type = '1'";
		PreparedStatement statement = conn.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();
		return resultSet;
	}

	public ResultSet getUserInfo2Simplify(String userId) throws Exception {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection conn = (Connection) DriverManager.getConnection(url, userName, password);
		String sql = "SELECT * FROM plat_sys_user where id = '"+userId+"'";
		PreparedStatement statement = conn.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();
		return resultSet;
	}
}
