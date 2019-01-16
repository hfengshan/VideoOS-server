package com.videojj.videoservice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/6 下午8:39.
 * @Description:
 */
public class FileUtil {

    private static Logger log = LoggerFactory.getLogger("FileUtil");

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 删除单个文件
     *
     * @param fileName
     *            要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                log.info("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                log.error("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            log.error("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 可以对某关闭之前的输入流进行拷贝，因为是内存流所以不需要被关闭。
     *
     * @param input 原始文件输入流
     * @return 拷贝后的文件输入流
     * @throws IOException
     */
    public static InputStream cloneInputStream(InputStream input) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return new ByteArrayInputStream(baos.toByteArray());
        } catch (IOException e) {
            log.error("FileUtil.cloneInputStream ==> error!!",e);
            throw e;
        }
    }
}
