package com.videojj.videoservice.bean;

import com.mysql.jdbc.StringUtils;
import com.videojj.videocommon.exception.ServiceException;
import jodd.io.StreamUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/22 下午4:54.
 * @Description:
 */
public class BodyReaderHttpServletRequestWrapper extends
        HttpServletRequestWrapper {


    private byte[] body;


    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        String charset = request.getCharacterEncoding();
        if (StringUtils.isNullOrEmpty(charset)) {
            charset = "utf-8";
        }
        try {
            body = StreamUtil.readBytes(request.getReader(), charset);
        } catch (IOException e) {

            throw new ServiceException("读取body数据报错");
        }
    }


    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {


            @Override
            public int read() throws IOException {
                return bais.read();
            }


            @Override
            public boolean isFinished() {
                return false;
            }


            @Override
            public boolean isReady() {
                return false;
            }


            @Override
            public void setReadListener(ReadListener readListener) {

            }


        };
    }
}
