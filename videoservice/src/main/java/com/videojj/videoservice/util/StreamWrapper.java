package com.videojj.videoservice.util;

import com.mysql.jdbc.StringUtils;
import com.videojj.videocommon.exception.ServiceException;
import jodd.io.StreamUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 注意，是线程不安全的，不要共享
 * @Author @videopls.com
 * Created by  on 2018/10/30 下午4:05.
 * @Description:
 */
public class StreamWrapper {


    private byte[] content;


    public StreamWrapper(InputStream stream) {

        try {

            content = StreamUtil.readBytes(stream);

        } catch (IOException e) {

            throw new ServiceException("读取stream报错");
        }
    }

    public InputStream getInputStream(){

        return new ByteArrayInputStream(content);
    }

}
