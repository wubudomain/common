package top.wboost.common.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageUtil {

    /**
     * 图片变灰
     * @param bufferedImage
     * @return
     */
    public static BufferedImage gray(BufferedImage bufferedImage) {
        BufferedImage bi = bufferedImage;
        // 得到宽和高
        int width = bi.getWidth(null);
        int height = bi.getHeight(null);
        // 读取像素
        int[] pixels = new int[width * height];
        bi.getRGB(0, 0, width, height, pixels, 0, width);
        //变灰
        int newPixels[] = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            int r = (pixels[i] >> 16) & 0xff;
            int g = (pixels[i] >> 8) & 0xff;
            int b = (pixels[i]) & 0xff;

            int gray = (int) (0.229 * r + 0.587 * g + 0.114 * b);
            newPixels[i] = newPixels[i] = (gray << 16) + (gray << 8) + gray;
        }

        // 基于 newPixels 构造一个 BufferedImage
        BufferedImage newbi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        newbi.setRGB(0, 0, width, height, newPixels, 0, width);
        newPixels = null;
        return newbi;
    }

    /**
     * 图片变蓝
     * @param sourcePath
     * @param targetPath
     * @param color
     * @return
     */
    public static BufferedImage transferAlpha(BufferedImage newbi, int r, int g, int b) {
        try {
            ImageIcon imageIcon = new ImageIcon(newbi);
            BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(),
                    BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
            g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
            int alpha = 0;
            for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
                for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
                    int rgb = bufferedImage.getRGB(j2, j1);
                    if (checkColor(rgb, 16, 0)) {
                        //rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
                        //rgb = new Color(69, 137, 148).getRGB();
                        rgb = new Color(r, g, b).getRGB();
                    }
                    bufferedImage.setRGB(j2, j1, rgb);
                }
            }
            g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
            return bufferedImage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static BufferedImage getBufferedImage(InputStream inputStream) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public static boolean writeBufferedImage(BufferedImage img, OutputStream outputStream) {
        boolean success = false;
        try {
            success = ImageIO.write(img, "PNG", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static byte[] transferAlpha(String sourcePath, String targetPath, int color) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            File iFile = new File(sourcePath);
            if (!iFile.exists())
                return byteArrayOutputStream.toByteArray();

            ImageIcon imageIcon = new ImageIcon(ImageIO.read(iFile));
            BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(),
                    BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
            g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
            int alpha = 0;
            for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
                for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
                    int rgb = bufferedImage.getRGB(j2, j1);
                    if (checkColor(rgb, 16, color)) {
                        //rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
                        rgb = new Color(69, 137, 148).getRGB();
                    }
                    bufferedImage.setRGB(j2, j1, rgb);
                }
            }
            g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());

            File targetFile = null;
            if (targetPath == null) {
                targetFile = new File(sourcePath + "_" + color + ".png");
            } else {
                targetFile = new File(targetPath);
                if (!targetFile.exists()) {
                    File dir = new File(targetFile.getParent());
                    if (!dir.exists())
                        dir.mkdirs();
                }
            }
            ImageIO.write(bufferedImage, "png", targetFile);

            //返回处理后图像的byte[]  
            //ImageIO.write(bufferedImage, "png", byteArrayOutputStream);  
        } catch (Exception e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();

    }

    /** 
     * 检查颜色是否为 白色 或者 黑色阈值范围 
     * @param rgb 颜色值 
     * @param color 0:白色 1:黑色 
     * @return 检查结果 
     */
    private static boolean checkColor(int rgb, int offset, int color) {
        int R = (rgb & 0xff0000) >> 16;
        int G = (rgb & 0xff00) >> 8;
        int B = (rgb & 0xff);
        //颜色是否为灰
        if (R == 231) {
            return true;
        }
        if (color == 0) {
            return ((255 - R) <= offset) && ((255 - G) <= offset) && ((255 - B) <= offset);
        } else {
            return ((R <= offset) && (G <= offset) && (B <= offset));
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedImage img = gray(
                getBufferedImage(new FileInputStream(new File("C:\\Users\\jwsun\\Desktop\\appmaptile.png"))));
        writeBufferedImage(img, new FileOutputStream(new File("C:\\Users\\jwsun\\Desktop\\appmaptile3.png")));
        BufferedImage blue = transferAlpha(img, 69, 137, 148);
        writeBufferedImage(blue, new FileOutputStream(new File("C:\\Users\\jwsun\\Desktop\\appmaptile4.png")));
    }

}
