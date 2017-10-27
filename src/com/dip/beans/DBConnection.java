package com.dip.beans;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.net.URL;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.dip.beans.GrayScale;
import com.dip.beans.DataBase;
import com.dip.beans.AWSItemS3;
import com.dip.beans.DBConnection;
import com.dip.beans.DataBase;
import com.amazonaws.AmazonWebServiceClient;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.AmazonRDSClient;
import com.amazonaws.services.rds.AmazonRDSClientBuilder;
import com.amazonaws.services.rds.model.DBInstance;
import com.amazonaws.services.rds.model.DescribeDBInstancesRequest;
import com.amazonaws.services.rds.model.DescribeDBInstancesResult;

@SuppressWarnings("deprecation")
public class DBConnection {
	private static Connection conn;
	private  String host = "db-rds-us-west-2.cxwfuof2khve.us-west-2.rds.amazonaws.com";
	private  int port = 3306;
	private static  String database = "DragonBaseRDS";
	private  String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
	private  String user = "vegetaDB";
	private  String pwd = "gokuadmin";

	public DBConnection() {
		try {
			//connects to the RDS DB + retrieves data for connection
			AmazonRDS rdsClient = new AmazonRDSClient();
			rdsClient.setEndpoint("rds.us-west-2.amazonaws.com");
			DescribeDBInstancesResult dbResult = rdsClient.describeDBInstances(
					new com.amazonaws.services.rds.model.DescribeDBInstancesRequest());
			for (DBInstance dbInstance : dbResult.getDBInstances()) {
				System.out.println("Instance Details:"
						+ dbInstance.getEndpoint().getAddress());
				this.host = dbInstance.getEndpoint().getAddress();
				this.port = dbInstance.getEndpoint().getPort();
				DBConnection.database = dbInstance.getDBName();
				this.user = dbInstance.getMasterUsername();
				
				url = "jdbc:mysql://" + host + ":" + port + "/" + database;
				System.out.println(url);
				// loads driver and set up connection
				DriverManager.registerDriver(new com.mysql.jdbc.Driver());
				DBConnection.conn = (Connection) DriverManager.getConnection(url, user, pwd);
				System.out.println("connection made");
				
				//we need to test if it exist already
				DataBase database = new DataBase();
				database.useTable();
				database.createTable();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*try {
			url = "jdbc:mysql://" + host + ":" + port + "/" + database;
			System.out.println(url);
			// loads driver and set up connection
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			DBConnection.conn = (Connection) DriverManager.getConnection(url, user, pwd);
			System.out.println("connection made");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}



	// returns the existing database connection, or creates it
	public static Connection getInstance() {
		if (conn == null) {
			new DBConnection();
		}
		return conn;
	}

	public static String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		DBConnection.database = database;
	}
	
	
}
