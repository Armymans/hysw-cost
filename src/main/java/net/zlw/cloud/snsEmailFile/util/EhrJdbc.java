package net.zlw.cloud.snsEmailFile.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EhrJdbc {
	private static String url = "jdbc:sqlserver://172.16.11.131:1433;Databasename=eHR";
	private static String userName = "OrgPer";
	private static String password = "Hy123456";

	public ResultSet getEmployeeInfoSimplify() throws Exception {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection conn = (Connection) DriverManager.getConnection(url, userName, password);
		String sql = "SELECT * FROM v_EmployeeInfoSimplify";
		PreparedStatement statement = conn.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();
		return resultSet;
	}

	public ResultSet getOrgInfo() throws Exception {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection conn = (Connection) DriverManager.getConnection(url, userName, password);
		String sql = "SELECT * FROM v_OrgInfo";
		PreparedStatement statement = conn.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();
		return resultSet;
	}
}
