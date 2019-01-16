package com.videojj.videoservice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author @videopls.com
 * Created by  on 2018/10/18 下午6:43.
 * @Description:
 */
public class ImageUtil {

    private static Logger log = LoggerFactory.getLogger("FileUtil");

    public static int getImgWidth(InputStream in) {
        int width = -1;
        try {
            BufferedImage src = javax.imageio.ImageIO.read(in);
            width = src.getWidth(null); // 得到源图宽
        } catch (Exception e) {

            log.error("read image width from stream error!!");
        }finally {
            try {
                in.close();
            } catch (IOException e) {

            }
        }
        return width;
    }

    public static int getImgHeight(InputStream in) {
        BufferedImage src = null;
        int height = -1;
        try {
            src = javax.imageio.ImageIO.read(in);
            height = src.getHeight(null); // 得到源图高

        } catch (Exception e) {

            log.error("read image height from stream error!!");
        }finally {
            try {
                in.close();
            } catch (IOException e) {

            }
        }
        return height;
    }

}
