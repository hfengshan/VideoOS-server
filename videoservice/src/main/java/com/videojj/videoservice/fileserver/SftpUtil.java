package com.videojj.videoservice.fileserver;

import com.jcraft.jsch.*;
import com.videojj.videoservice.config.FileServerConfig;
import com.videojj.videoservice.util.FileUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *  sftp连接工具类
 */
@Slf4j
@Getter
@Setter
public class SftpUtil implements CommonFileServer {

    private FileServerConfig sftpConfig;

    public SftpUtil(FileServerConfig sftpConfig) {
        this.sftpConfig = sftpConfig;
    }

    @Override
    public InputStream download(String url) throws Exception {
        InputStream in;
        SftpConnection sftpConnection = SftpConnection.createSftpConnection(sftpConfig, url);
        try {
            in = sftpConnection.download();
        } catch (Exception e) {
            throw e;
        } finally {
            sftpConnection.logout();
        }
        return in;
    }

    @Override
    public void upload(String url, InputStream in) throws Exception {
        SftpConnection sftpConnection = SftpConnection.createSftpConnection(sftpConfig, url);
        try {
            sftpConnection.upload(in);
        } catch (Exception e) {
            throw e;
        } finally {
            sftpConnection.logout();
        }
    }

    @Override
    public void delete(String url) throws Exception {
        SftpConnection sftpConnection = SftpConnection.createSftpConnection(sftpConfig, url);
        try {
            sftpConnection.delete();
        } catch (Exception e) {
            throw e;
        } finally {
            sftpConnection.logout();
        }
    }

    /**
     *  sftp操作的接口类，由此类的静态工厂方法创建对象，请注意在一次或多次sftp操作之后，请在finally语句块里进行logout。
     */
    private static class SftpConnection {
        private Session session;

        private ChannelSftp sftp;

        private FileServerConfig sftpConfig;

        /**
         * 文件的全路径
         */
        private String url;

        private SftpConnection(FileServerConfig sftpConfig, String url) {
            this.sftpConfig = sftpConfig;
            this.url = url;
        }

        public static SftpConnection createSftpConnection(FileServerConfig sftpConfig, String url) throws JSchException {
            SftpConnection sftpConnection = new SftpConnection(sftpConfig, sftpConfig.getRootPath() + url);
            sftpConnection.login();
            return sftpConnection;
        }

        /**
         *  连接sftp服务器
         *
         * @throws JSchException
         */
        private void login() throws JSchException{
            try {
                JSch jsch = new JSch();
                if (StringUtils.isNotEmpty(sftpConfig.getPrivateKey())) {
                    // 设置私钥
                    jsch.addIdentity(sftpConfig.getPrivateKey());
                    log.info("sftp connect,path of private key file：{}", sftpConfig.getPrivateKey());
                }
                log.info("sftp connect by host:{} username:{}", sftpConfig.getHost(), sftpConfig.getUsername());

                session = jsch.getSession(sftpConfig.getUsername(), sftpConfig.getHost(), sftpConfig.getPort());
                if (StringUtils.isNotEmpty(sftpConfig.getPassword())) {
                    session.setPassword(sftpConfig.getPassword());
                }
                Properties config = new Properties();
                config.put("StrictHostKeyChecking", "no");

                session.setConfig(config);
                session.connect();

                Channel channel = session.openChannel("sftp");
                channel.connect();

                sftp = (ChannelSftp) channel;
                log.info(String.format("sftp server host:[%s] port:[%s] is connect successfully", sftpConfig.getHost(), sftpConfig.getPort()));
            } catch (JSchException e) {
                log.error("Cannot connect to specified sftp server : {}:{} \n Exception message is: {}", new Object[]{sftpConfig.getHost(), sftpConfig.getPort(), e.getMessage()});
                throw e;
            }
        }

        /**
         * 关闭连接 server
         */
        public void logout() {
            if (sftp != null) {
                if (sftp.isConnected()) {
                    sftp.disconnect();
                }
            }
            if (session != null) {
                if (session.isConnected()) {
                    session.disconnect();
                }
            }
        }

        public InputStream download() throws Exception {
            InputStream in = FileUtil.cloneInputStream(sftp.get(url));
            log.info("file:[{}] is download successfully", url);
            return in;
        }

        public void upload(InputStream in) throws Exception {
            sftp.put(in, url);
            log.info("file:[{}] is upload successfully", url);
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("Unable to close InputStream. Occur IOException : {}", e);
                }
            }
        }

        public void delete() throws Exception {
            sftp.rm(url);
            log.info("file:[{}] is deleted successfully", url);
        }
    }


}
