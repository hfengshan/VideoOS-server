package com.videojj.videoservice.bean;

import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videoservice.config.CommonConfig;
import com.videojj.videoservice.config.FileServerConfig;
import com.videojj.videoservice.fileserver.CommonFileServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @Author @videopls.com
 * Created by  on 2018/10/22 下午3:39.
 * @Description:
 */

@Configuration
public class FileConfigBean {

    @Resource
    private CommonConfig commonConfig;

    @Resource
    private FileServerConfig fileServerConfig;

    private static Logger log = LoggerFactory.getLogger("FileConfigBean");

    @Bean
    public CommonFileServer getFileServer(){

        String className = commonConfig.getFileTool();

        try {

            Class clz = Class.forName("com.videojj.videoservice.fileserver."+className);

            Constructor con = clz.getConstructor(FileServerConfig.class);

            CommonFileServer commonFileServer=(CommonFileServer)con.newInstance(fileServerConfig);

            return commonFileServer;

        } catch (ClassNotFoundException e) {

            log.error("FileConfigBean.getFileServer ==>class name not found!!",e);

            throw new ServiceException("找不到相关的文件服务的类");

        } catch (InstantiationException e) {

            log.error("FileConfigBean.getFileServer ==>InstantiationException error!!",e);

            throw new ServiceException("实例化文件服务类时报错");
        } catch (IllegalAccessException e) {

            log.error("FileConfigBean.getFileServer ==>IllegalAccessException error!!",e);

            throw new ServiceException("实例化文件服务类时报错");
        }catch (NoSuchMethodException e){

            log.error("FileConfigBean.getFileServer ==> noSuchMethod!!!",e);

            throw new ServiceException("没有找到相关的构造函数");
        }catch (InvocationTargetException e){

            log.error("FileConfigBean.getFileServer ==> InvocationTargetException!!!",e);

            throw new ServiceException("根据构造函数实例化对象报错");
        }

    }
}
