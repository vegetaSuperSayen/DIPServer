package com.dip.servlets;

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

@WebServlet("/FileUploadServlet")
@MultipartConfig(maxFileSize = 16177215) // upload file's size up to 16MB
public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final int BUFFER_SIZE = 4096;

	public FileUploadServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	@SuppressWarnings("deprecation")
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		AWSItemS3 s3Client = new AWSItemS3();
		String message = null;
		URL url1 = null;
		URL url2 = null;

		//////////////////////////////////////////////////////////////////////
		// UPLOAD FROM THE FORM TO THE DATABASE
		//////////////////////////////////////////////////////////////////////

		// gets values of text fields
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");

		InputStream inputStream = null;

		// I) obtains the upload file part in this multipart request
		Part filePart = request.getPart("photo");
		if (filePart != null) {
			// prints out some information for debugging
			System.out.println(filePart.getName());
			System.out.println(filePart.getSize());
			System.out.println(filePart.getContentType());

			// II) obtains input stream of the upload file
			inputStream = filePart.getInputStream();
		}

		System.out.println("before try catch 1");

		try {
			// constructs SQL statement
			String sql = "INSERT INTO records (email, phone, photo) values (?, ?, ?)";
			System.out.println("getInstance 1");
			PreparedStatement statement = DBConnection.getInstance()
					.prepareStatement(sql);
			System.out.println("Test");
			statement.setString(1, email);
			statement.setString(2, phone);
			
			
			
			if (inputStream != null) {
				// III) fetches inputstream of the upload file for the blob
				// column
				statement.setBlob(3, inputStream);
			}

			// sends the statement to the database server
			int row = statement.executeUpdate();
			if (row > 0) {
				message = "File uploaded and saved into database.";
			}
		} catch (SQLException ex) {
			message = "ERROR: " + ex.getMessage();
			ex.printStackTrace();
		} finally {

			System.out.println("upload {setBlob} to DB finished");

			//////////////////////////////////////////////////////////////////////
			// DOWNLOAD FROM THE DATABASE
			//////////////////////////////////////////////////////////////////////

			String filePath = "digital_image_pre_processed.jpg";

			try {
				// Connection conn = DriverManager.getConnection(url, user,
				// password);
				String sql2 = "SELECT photo FROM records WHERE email=? AND phone=?";
				System.out.println("getInstance 2");
				PreparedStatement statement2 = DBConnection.getInstance()
						.prepareStatement(sql2);
				statement2.setString(1, email);
				statement2.setString(2, phone);

				ResultSet result = statement2.executeQuery();
				if (result.next()) {
					Blob blob = result.getBlob("photo");
					InputStream inputStream2 = blob.getBinaryStream();
					System.out.println("ready to create outputstream..");
					OutputStream outputStream = new FileOutputStream(filePath);
					System.out.println("finished to create outputstream..");
					int bytesRead = -1;
					byte[] buffer = new byte[BUFFER_SIZE];
					while ((bytesRead = inputStream2.read(buffer)) != -1) {
						outputStream.write(buffer, 0, bytesRead);
					}

					inputStream2.close();
					outputStream.close();
					System.out.println("File saved");
				}

			} catch (SQLException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			System.out.println("download {getBlob} from the database");

			//////////////////////////////////////////////////////////////////////
			// IMAGE UPLOAD TO S3 AND URL UPLOAD TO THE DATABASE
			//////////////////////////////////////////////////////////////////////

			try {
				s3Client.setUploadFileName("digital_image_pre_processed.jpg");
				s3Client.setKeyName(email + "_" + phone + "_"
						+ "digital_image_pre_processed.jpg");
				url1 = s3Client.uploadfile();
				System.out.println("url1: "+url1.toString());
				String sql3 = "INSERT INTO records (s3-raw-url) values (?)";
				System.out.println("getInstance 3");
				PreparedStatement statement3 = DBConnection.getInstance()
						.prepareStatement(sql3);
				statement3.setString(1, url1.toString());
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			//////////////////////////////////////////////////////////////////////
			// DIGITAL IMAGE PROCESSING
			//////////////////////////////////////////////////////////////////////

			System.out.println("step 1 new GrayScale");
			GrayScale grayImage = new GrayScale();
			// here it will be the path from where the original image is stored
			// on the S3 Bucket
			System.out.println("step 2 input and output");
			grayImage.setInputPath("digital_image_pre_processed.jpg");
			// here it will be the path from where the new grayscaled image will
			// be stored on the S3 Bucket
			grayImage.setOutputPath(email + "_" + phone + "_"
					+ "digital_image_processed_grayscale.jpg");
			/*
			 * grayImage.setOutputPath(
			 * "/Users/maratmonnie/OneDrive/workspace/eclipseworkspace/openclassroom/WebContent/WEB-INF/photos/"
			 * + email + "_" + phone + "_" + "grayscale.jpg");
			 */
			System.out.println("step 3 conversion");
			grayImage.grayscale();
			System.out.println("conversion done");

			//////////////////////////////////////////////////////////////////////
			// IMAGE PROCESSED UPLOAD TO S3 AND URL UPLOAD TO THE DATABASE
			//////////////////////////////////////////////////////////////////////

			try {
				s3Client.setUploadFileName(email + "_" + phone + "_"
						+ "digital_image_processed_grayscale.jpg");
				s3Client.setKeyName(email + "_" + phone + "_"
						+ "digital_image_processed_grayscale.jpg");
				s3Client.uploadfile();
				url2 = s3Client.uploadfile();
				System.out.println("url2: "+url2.toString());
				String sql4 = "INSERT INTO records (s3-finished-url) values (?)";
				PreparedStatement statement4;
				System.out.println("getInstance 4");
				statement4 = DBConnection.getInstance().prepareStatement(sql4);
				statement4.setString(1, url2.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}

			//////////////////////////////////////////////////////////////////////
			// ATTRIBUTE SEND TO THE VIEW
			//////////////////////////////////////////////////////////////////////

			// sets the message in request scope
			request.setAttribute("Message", message);
			request.setAttribute("url1", url1);
			request.setAttribute("url2", url2);

			// forwards to the message page
			this.getServletContext()
					.getRequestDispatcher("/WEB-INF/UploadMessage.jsp")
					.forward(request, response);
		}

	}

}
