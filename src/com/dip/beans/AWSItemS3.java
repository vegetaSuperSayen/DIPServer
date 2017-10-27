package com.dip.beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.util.logging.Logger;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.budgets.model.TimeUnit;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.sun.corba.se.impl.util.Utility;

public class AWSItemS3 {
	
	private static String bucketName     = null;
	private static String keyName        = null;
	private static String uploadFileName = null; //Users//maratmonnie//OneDrive//workspace//eclipseworkspace//openclassroom//WebContent//photos
	private  AmazonS3 s3Client = null;
	
		public AWSItemS3() {
			this.s3Client = new AmazonS3Client();
			this.bucketName     = "vegeta-bucket-test";
			this.keyName        = "sample1";
			this.uploadFileName = null;
		}
		
		public URL uploadfile(){
			System.out.println("Test w/ println");
			try {
	            System.out.println("Uploading a new object to S3 from a file\n");
	            File file = new File(uploadFileName);
	            s3Client.putObject(new PutObjectRequest(bucketName, keyName, file));
	
	         } catch (AmazonServiceException ase) {
	            System.out.println("Caught an AmazonServiceException, which " +
	            		"means your request made it " +
	                    "to Amazon S3, but was rejected with an error response" +
	                    " for some reason.");
	            System.out.println("Error Message:    " + ase.getMessage());
	            System.out.println("HTTP Status Code: " + ase.getStatusCode());
	            System.out.println("AWS Error Code:   " + ase.getErrorCode());
	            System.out.println("Error Type:       " + ase.getErrorType());
	            System.out.println("Request ID:       " + ase.getRequestId());
	        } catch (AmazonClientException ace) {
	            System.out.println("Caught an AmazonClientException, which " +
	            		"means the client encountered " +
	                    "an internal error while trying to " +
	                    "communicate with S3, " +
	                    "such as not being able to access the network.");
	            System.out.println("Error Message: " + ace.getMessage());
	        }

			Date expiration = new Date(System.currentTimeMillis() + 6*24*60*60*1000);
			URL generatePresignedUrl;
			try {
				generatePresignedUrl = new URL(null);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return generatePresignedUrl = s3Client.generatePresignedUrl(bucketName, keyName, expiration);
			
			
		}
		
		public void deletefile()
		{
			 try {
				 s3Client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
		        } catch (AmazonServiceException ase) {
		            System.out.println("Caught an AmazonServiceException.");
		            System.out.println("Error Message:    " + ase.getMessage());
		            System.out.println("HTTP Status Code: " + ase.getStatusCode());
		            System.out.println("AWS Error Code:   " + ase.getErrorCode());
		            System.out.println("Error Type:       " + ase.getErrorType());
		            System.out.println("Request ID:       " + ase.getRequestId());
		        } catch (AmazonClientException ace) {
		            System.out.println("Caught an AmazonClientException.");
		            System.out.println("Error Message: " + ace.getMessage());
		        }
		    }

		public void downloadfile() throws IOException
		{
			try {
	            System.out.println("Downloading an object");
	            S3Object s3object = s3Client.getObject(new GetObjectRequest(
	            		bucketName, keyName));
	            System.out.println("Content-Type: "  + 
	            s3object.getObjectMetadata().getContentType());	 
	            InputStream input = s3object.getObjectContent();
	            
	           BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	           while (true) {
	               String line = reader.readLine();
	               if (line == null) break;

	               System.out.println("    " + line);
	           }
	           System.out.println();   
	        } catch (AmazonServiceException ase) {
	            System.out.println("Caught an AmazonServiceException, which" +
	            		" means your request made it " +
	                    "to Amazon S3, but was rejected with an error response" +
	                    " for some reason.");
	            System.out.println("Error Message:    " + ase.getMessage());
	            System.out.println("HTTP Status Code: " + ase.getStatusCode());
	            System.out.println("AWS Error Code:   " + ase.getErrorCode());
	            System.out.println("Error Type:       " + ase.getErrorType());
	            System.out.println("Request ID:       " + ase.getRequestId());
	        } catch (AmazonClientException ace) {
	            System.out.println("Caught an AmazonClientException, which means"+
	            		" the client encountered " +
	                    "an internal error while trying to " +
	                    "communicate with S3, " +
	                    "such as not being able to access the network.");
	            System.out.println("Error Message: " + ace.getMessage());
	        }
	    }

		public static String getBucketName() {
			return bucketName;
		}

		public static void setBucketName(String bucketName) {
			AWSItemS3.bucketName = bucketName;
		}

		public static String getKeyName() {
			return keyName;
		}

		public static void setKeyName(String keyName) {
			AWSItemS3.keyName = keyName;
		}

		public static String getUploadFileName() {
			return uploadFileName;
		}

		public static void setUploadFileName(String uploadFileName) {
			AWSItemS3.uploadFileName = uploadFileName;
		}

}

