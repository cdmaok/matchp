package cn.edu.xmu.gxj.matchp.model;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

public class Pic {
	private int height;
	private int width;
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	
	public Pic(String urlStr) throws MalformedURLException, IOException{
		BufferedImage image = ImageIO.read(new URL(urlStr));
		this.height = image.getHeight();
		this.width = image.getWidth();
	}
	
}
