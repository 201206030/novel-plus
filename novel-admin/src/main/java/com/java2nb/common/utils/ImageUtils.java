package com.java2nb.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * @author xiongxy
 * @date 2019-09-25 15:09:21
 */
@Slf4j
public class ImageUtils {
    /***
     * 剪裁图片
     * @param file 图片
     * @param x  起点横坐标
     * @param y  纵坐标
     * @param w  长
     * @param h  高
     * @throws IOException
     * @date 2019-09-25 15:09:21
     */
    public static BufferedImage cutImage(MultipartFile file, int x, int y, int w, int h,String prefix) {

        Iterator iterator = ImageIO.getImageReadersByFormatName(prefix);
        try {
            ImageReader reader = (ImageReader)iterator.next();
            //转换成输入流
            InputStream in = file.getInputStream();
            ImageInputStream iis = ImageIO.createImageInputStream(in);
            reader.setInput(iis, true);
            ImageReadParam param = reader.getDefaultReadParam();
            Rectangle rect = new Rectangle(x, y, w,h);
            param.setSourceRegion(rect);
            BufferedImage bi = reader.read(0,param);
            return bi;
        } catch (Exception e) {
            log.error(e.getMessage(),e);

        }
        return null;
    }
    /***
     * 图片旋转指定角度
     * @param bufferedimage 图像
     * @param degree      角度
     * @return
     * @date 2019-09-25 15:09:21
     */
    public static BufferedImage rotateImage(BufferedImage bufferedimage, int degree) {
        int w = bufferedimage.getWidth();
        int h = bufferedimage.getHeight();
        int type = bufferedimage.getColorModel().getTransparency();
        BufferedImage img;
        Graphics2D graphics2d;
        (graphics2d = (img = new BufferedImage(w, h, type))
                .createGraphics()).setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.setPaint(Color.WHITE);
        graphics2d.fillRect(0, 0, w, h);
        graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);
        graphics2d.drawImage(bufferedimage, 0, 0,Color.WHITE, null);
        graphics2d.dispose();
        return img;
    }
}
