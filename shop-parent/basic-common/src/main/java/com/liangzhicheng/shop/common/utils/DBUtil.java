package com.liangzhicheng.shop.common.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

	public static Connection getConnection() throws Exception{
		String url = "jdbc:mysql://localhost:3306/shop_member?serverTimezone=GMT%2B8";
		String username = "root";
		String password = "admin";
		String driver = "com.mysql.jdbc.Driver";
		Class.forName(driver);
		return DriverManager.getConnection(url, username, password);
	}

}
