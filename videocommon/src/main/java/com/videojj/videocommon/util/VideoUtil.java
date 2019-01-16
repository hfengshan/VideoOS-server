package com.videojj.videocommon.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 视频工具类
 *
 * @author zhangzhewen
 * @date 2018/12/12
 */
public class VideoUtil {

    private static Logger log = LoggerFactory.getLogger(VideoUtil.class);

    /**
     * MultipartFile转File
     */
    public static int getVideoFileLength(HttpServletRequest request, MultipartFile multipartfile) throws IOException {

        //在指定目录，生成临时文件，然后再转换

        //在根目录下创建一个tempfileDir的临时文件夹
        String contextpath = request.getSession().getServletContext().getRealPath("/") + "tempfileDir/";
        File f = new File(contextpath);
        if (!f.exists()) {
            f.mkdirs();
        }

        //在tempfileDir的临时文件夹下创建一个新的和上传文件名一样的文件
        String filename = multipartfile.getOriginalFilename();
        String filepath = contextpath + "/" + filename;
        File newFile = new File(filepath);

        //将MultipartFile文件转换，即写入File新文件中，返回File文件
        multipartfile.transferTo(newFile);
        int len = getVideoTime(newFile.getAbsolutePath());

        //如果不需要File文件可删除
        if (newFile.exists()) {
            newFile.delete();
        }

        return len;
    }

    /**
     * 获取视频总时间
     */
    private static int getVideoTime(String video_path) {
        List<String> commands = new ArrayList<>();
        commands.add("ffmpeg");
        commands.add("-i");
        commands.add(video_path);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commands);
            Process p = builder.start();

            //从输入流中读取视频信息
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            br.close();

            //从视频信息中解析时长
            String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";
            Pattern pattern = Pattern.compile(regexDuration);
            Matcher m = pattern.matcher(stringBuilder.toString());
            if (m.find()) {
                int time = getTimelen(m.group(1));
                return time;
            }

        } catch (Exception e) {
            log.error("com.videojj.videocommon.util.VideoUtil getVideoTime() -----------> error!", e);
        }

        return 0;
    }

    // 格式:"00:00:10.68"
    private static int getTimelen(String timelen) {
        int min = 0;
        String strs[] = timelen.split(":");
        if (strs[0].compareTo("0") > 0) {
            // 秒
            min += Integer.valueOf(strs[0]) * 60 * 60;
        }
        if (strs[1].compareTo("0") > 0) {
            min += Integer.valueOf(strs[1]) * 60;
        }
        if (strs[2].compareTo("0") > 0) {
            min += Math.round(Float.valueOf(strs[2]));
        }
        return min;
    }

}
