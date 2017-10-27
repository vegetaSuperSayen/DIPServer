package com.dip.beans;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class GrayScale { 
   BufferedImage  image;
   int width;
   int height;
   private String inputPath=null; // "/Users/maratmonnie/OneDrive/workspace/eclipseworkspace/photosONserver/tom.jpg"
   private String outputPath=null; // "/Users/maratmonnie/OneDrive/workspace/eclipseworkspace/photosONserver/grayscale.jpg"
 

	public GrayScale() {}
	
	public void grayscale() {
	    try {
	        File input = new File(this.inputPath);
	        
	        
	        this.image = ImageIO.read(input);
	        this.width = this.image.getWidth();
	        this.height = this.image.getHeight();
	        
	        for(int i=0; i<this.height; i++){
	        
	           for(int j=0; j<this.width; j++){
	           
	              Color c = new Color(this.image.getRGB(j, i));
	              int red = (int)(c.getRed() * 0.299);
	              int green = (int)(c.getGreen() * 0.587);
	              int blue = (int)(c.getBlue() *0.114);
	              Color newColor = new Color(red+green+blue,
	              
	              red+green+blue,red+green+blue);
	              
	              this.image.setRGB(j,i,newColor.getRGB());
	           }
	        }
	        
	        File ouptut = new File(this.outputPath);
	        ImageIO.write(this.image, "jpg", ouptut);
	        
	     } 
	     catch (Exception e) {}
	}

	public String getInputPath() {
		return this.inputPath;
	}

	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}

	public String getOutputPath() {
		return this.outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}
	


}
