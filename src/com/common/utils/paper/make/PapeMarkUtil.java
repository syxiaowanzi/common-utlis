package com.common.utils.paper.make;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
/**
 * 	添加水印工具类
 * @author wangming
 */
public class PapeMarkUtil {
	
	/**
	 * 	图片添加文字
	 * 
	 * @param srcImgPath
	 *	需要添加水印的图片的路径
	 * @param outImgPath
	 *	添加水印后图片输出路径
	 * @param markContentColor
	 *	水印文字的颜色
	 * @param ContentAndPosition
	 *	内容和位置
	 */
	public static void markPaper(String srcImgPath, String outImgPath, Color markContentColor, ContentAndPosition cp ) {
		FileOutputStream outImgStream = null;
		try {
			// 读取原图片信息
			File srcImgFile = new File(srcImgPath);
			Image srcImg = ImageIO.read(srcImgFile);
			int srcImgWidth = srcImg.getWidth(null);
			int srcImgHeight = srcImg.getHeight(null);
			// 加水印
			BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bufImg.createGraphics();
			g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
			// Font font = new Font("Courier New", Font.PLAIN, 12);
			Font font = new Font("宋体", Font.PLAIN, 23);
			g.setColor(markContentColor); // 根据图片的背景设置水印颜色
			g.setFont(font);
			List<Map<String, String>>  contentList = new ArrayList<Map<String, String>>();
			Map<String, String>  mapI = new HashMap<String, String>();
			//将答案和坐标封装
			mapI.put("content", cp.getContent());
			mapI.put("position",cp.getPosition());
			contentList.add(mapI);
			for(Iterator it = contentList.iterator();it.hasNext();){
				Map map = (Map)it.next();
				String content = map.get("content")+"";
				String position =  map.get("position")+"";
				String x = position.split(",")[0];
				String y = position.split(",")[1];
				g.drawString(content, Integer.parseInt(x), Integer.parseInt(y)); // 在图片指定位置中填入内容
			}
			g.dispose();
			// 输出图片
			outImgStream = new FileOutputStream(outImgPath);
			ImageIO.write(bufImg, "jpg", outImgStream);
			outImgStream.flush();
			outImgStream.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outImgStream != null) {
				try {
					outImgStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 	图片添加水印
	 * 
	 * @param srcImgPath
	 *	需要添加水印的图片的路径
	 * @param outImgPath
	 *	添加水印后图片输出路径
	 * @param markContentColor
	 *	水印文字的颜色
	 * @param waterMarkContent
	 *	水印的文字
	 */
	public static void mark(String srcImgPath, String outImgPath, Color markContentColor, String waterMarkContent) {
		FileOutputStream outImgStream = null;
		try {
			// 读取原图片信息
			File srcImgFile = new File(srcImgPath);
			Image srcImg = ImageIO.read(srcImgFile);
			int srcImgWidth = srcImg.getWidth(null);
			int srcImgHeight = srcImg.getHeight(null);
			// 加水印
			BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bufImg.createGraphics();
			g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
			// Font font = new Font("Courier New", Font.PLAIN, 12);
			Font font = new Font("宋体", Font.PLAIN, 24);
			g.setColor(markContentColor); // 根据图片的背景设置水印颜色

			g.setFont(font);
			int x = srcImgWidth - getWatermarkLength(waterMarkContent, g) - 3;
			int y = srcImgHeight - 3;
			g.drawString(waterMarkContent, x, y); // 189，  212            199 227   593，  500
			g.dispose();
			// 输出图片
			outImgStream = new FileOutputStream(outImgPath);
			ImageIO.write(bufImg, "jpg", outImgStream);
			outImgStream.flush();
			outImgStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outImgStream != null) {
				try {
					outImgStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	

	/**
	 *	 获取水印文字总长度
	 * 
	 * @param waterMarkContent
	 * 	 水印的文字
	 * @param g
	 * @return 水印文字总长度
	 */
	public static int getWatermarkLength(String waterMarkContent, Graphics2D g) {
		return g.getFontMetrics(g.getFont()).charsWidth(
				waterMarkContent.toCharArray(), 0, waterMarkContent.length());
	}
	
	/**
	 * 	内容与坐标
	 */
	 public class ContentAndPosition{
		private String content; // 内容
		private String position; // 坐标
		
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getPosition() {
			return position;
		}
		public void setPosition(String position) {
			this.position = position;
		}
	}
	 
	 
	public static void main(String[] args) throws IOException {
		// 原图位置, 输出图片位置, 水印文字颜色, 水印文字
		//mark("E:/1.jpg", "E:/paperAfterWatermark.jpg",Color.black, "欢迎您！！！");
		
		PapeMarkUtil papeMarkUtil =new  PapeMarkUtil();
		ContentAndPosition cp = papeMarkUtil.new ContentAndPosition();
		cp.setContent("哈哈哈哈哈哈哈");
		cp.setPosition("200,400");
		
		markPaper("E:/1.jpg", "E:/paperAfterWatermark1.jpg", Color.black, cp);
		
	}
}
