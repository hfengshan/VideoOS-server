package com.videojj.videoservice.fileserver;

import java.io.InputStream;

/**
 * @Author @videopls.com
 * Created by  on 2018/9/4 下午6:57.
 * @Description:
 */
public interface CommonFileServer {

    public InputStream download(String url) throws Exception;

    public void upload(String url,InputStream in) throws Exception;

    public void delete(String url) throws Exception;

}
