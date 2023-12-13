package image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class PicResize 
{
	private PicResize() {}
	
	public static BufferedImage getProfilePic(String filePath)
	{
		BufferedImage resizedImg = null;
		try
		{
			File file = new File(filePath);
			InputStream is = new FileInputStream(file);
			Image img = new ImageIcon(file.toString()).getImage();
			
			int resizeWidth = 80;
			int resizeHeight = 92;
			
			resizedImg = resize(is, resizeWidth, resizeHeight);
		}
		catch (IOException e) {e.printStackTrace();}
		
		return resizedImg;
	}
	
	public static BufferedImage getIngameProfile(BufferedImage originalImage) 
	{
        // 새로운 이미지 생성
        BufferedImage resizedImage = new BufferedImage(230, 230, BufferedImage.TYPE_INT_ARGB);

        // Graphics2D 객체를 얻어옴
        Graphics2D g2d = resizedImage.createGraphics();

        // 이미지를 새로운 크기로 그림
        g2d.drawImage(originalImage.getScaledInstance(230, 230, Image.SCALE_SMOOTH), 0, 0, null);

        // Graphics2D 객체 해제
        g2d.dispose();

        return resizedImage;
    }
	
	// 채팅으로 보낼 사진을 리사이징. 
	public static BufferedImage getChatImage(BufferedImage originalImage) 
	{
        // 새로운 이미지 생성
        BufferedImage resizedImage = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);

        // Graphics2D 객체를 얻어옴
        Graphics2D g2d = resizedImage.createGraphics();

        // 이미지를 새로운 크기로 그림
        g2d.drawImage(originalImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH), 0, 0, null);

        // Graphics2D 객체 해제
        g2d.dispose();

        return resizedImage;
    }
	
	private static BufferedImage resize(InputStream is, int width, int height) throws IOException
	{
		BufferedImage inputImage = ImageIO.read(is);
		BufferedImage outputImage = new BufferedImage(width, height, inputImage.getType());
		Graphics2D gp2d = outputImage.createGraphics();
		gp2d.drawImage(inputImage, 0, 0, width, height, null);
		gp2d.dispose();
		
		return outputImage;
	}
	
	
}
