package com.dip.beans;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.Part;
import com.dip.beans.DBConnection;

public class DataBase {

	
	public DataBase() {}

	public void useTable() {
		try {
			System.out.println("query va etre executer");
			String sql = "use "+DBConnection.getDatabase()+";";
			System.out.println(sql);
			PreparedStatement statement = DBConnection.getInstance()
					.prepareStatement(sql);
			statement.executeQuery();
			System.out.println("query executer");

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public void createTable() {
		try {
			System.out.println("query2 va etre executer");
			String sql ="CREATE TABLE `records`("
					+ "`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
					+ "`email` VARCHAR(32)," + "`phone` VARCHAR(32),"
					+ "`s3-raw-url` VARCHAR(32),"
					+ "`s3-finished-url` VARCHAR(32)," 
					+ "`photo` mediumblob,"
					+ "status INT(1)," + "receipt BIGINT"
					+ ")ENGINE=InnoDB DEFAULT CHARSET=latin1;";
			System.out.println(sql);
			PreparedStatement statement = DBConnection.getInstance()
					.prepareStatement(sql);
			statement.executeQuery();
			System.out.println("query2 executer");

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

}
