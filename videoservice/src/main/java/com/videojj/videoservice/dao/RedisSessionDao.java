package com.videojj.videoservice.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.videojj.videoservice.util.PermissionUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.crazycake.shiro.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RedisSessionDao extends EnterpriseCacheSessionDAO {

    private static final Logger logger = LoggerFactory.getLogger(RedisSessionDao.class);

    /** Session过期时间,单位: 秒 此处设置为2天之内，可以带token自动登录*/
    private static final Integer SESSION_TIMEOUT = 48*60*60;

    private static final String prefix = "VIDEO_MANAGE_PLATFORM_";

    @Resource
    private RedisManager redisManager;

    // 创建session，保存到数据库
    @Override
    protected Serializable doCreate(Session session) {
        logger.info("### doCreate session: " + JSON.toJSONString(session));

        String username = PermissionUtil.getCurrentUsername();
//        Serializable sessionId = super.doCreate(session);
        Serializable sessionId = username.concat("_").concat(prefix).concat(UUID.randomUUID().toString());

        ((SimpleSession)session).setId(sessionId);

        byte[] key = sessionId.toString().getBytes();
//        byte[] key = sessionId.toString().getBytes();
        try {
            redisManager.set(key, sessionToByte(session),SESSION_TIMEOUT);

        }catch (Exception e){

            logger.warn("redisService execute error",e);
        }
        return sessionId;
    }

    // 获取session
    @Override
    public Session doReadSession(Serializable sessionId) {
        logger.info("### doReadSession sessionId: {}", sessionId);
        // 先从缓存中获取session，如果没有再去数据库中获取
        Session session = null;
        try {
            byte[] key = sessionId.toString().getBytes();
            byte[] bytes = redisManager.get(key);
            if (bytes != null && bytes.length > 0) {
                session = byteToSession(bytes);
            }

        }catch(Exception e){

            logger.warn("redisSessionDao.doReadSession execute Error!!!",e);
        }

        return session;
    }

    // 更新session的最后一次访问时间
    @Override
    protected void doUpdate(Session session) {
//        logger.info("### doUpdate session: " + new Gson().toJson(session));
        super.doUpdate(session);
        byte[] key = session.getId().toString().getBytes();
        try {

            redisManager.set(key, sessionToByte(session),SESSION_TIMEOUT);
        }catch (Exception e){

            logger.warn("redisService execute error",e);
        }
    }

    // 删除session
    @Override
    protected void doDelete(Session session) {

        super.doDelete(session);
        try {

            redisManager.del(session.getId().toString().getBytes());

        }catch (Exception e){

            logger.warn("redisService execute error",e);
        }
    }

    // 把session对象转化为byte保存到redis中
    public byte[] sessionToByte(Session session) {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        byte[] bytes = null;
        try {
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(session);
            bytes = bo.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    // 把byte还原为session
    public Session byteToSession(byte[] bytes) {
        ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
        ObjectInputStream in;
        SimpleSession session = null;
        try {
            in = new ObjectInputStream(bi);
            session = (SimpleSession) in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return session;
    }

    public void removeSession(String name){

        Set<byte[]> keys=redisManager.keys(name.concat("_").concat(prefix) +"*");

        keys.stream().forEach((k) -> {

            logger.info("RedisSessionDao.removeSession ==> remove session. key is {}",new String(k));

            redisManager.del(k);

        });

    }

}