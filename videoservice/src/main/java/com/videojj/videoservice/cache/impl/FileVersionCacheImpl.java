package com.videojj.videoservice.cache.impl;

import com.videojj.videocommon.constant.Constants;
import com.videojj.videoservice.cache.FileVersionCache;
import com.videojj.videoservice.dao.TbTemplateZipFileMapper;
import com.videojj.videoservice.entity.TbTemplateZipFile;
import com.videojj.videoservice.entity.TbTemplateZipFileCriteria;
import com.videojj.videoservice.enums.IsDeletedEnum;
import com.videojj.videoservice.service.impl.InteractionZipFileServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.crazycake.shiro.RedisManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author @videopls.com
 * Created by  on 2018/12/13 下午3:24.
 * @Description:
 */
@Service
public class FileVersionCacheImpl implements FileVersionCache {

    @Resource
    private RedisManager redisManager;

    private final String key = "videoOs-NewVersion";

    @Resource
    private TbTemplateZipFileMapper tbTemplateZipFileMapper;

    @Override
    public String getNewVersion(){

        byte[] value = redisManager.get(key.getBytes());

        if(null == value || value.length == 0){

            TbTemplateZipFileCriteria tbTemplateZipFileCriteria = new TbTemplateZipFileCriteria();

            TbTemplateZipFileCriteria.Criteria crit = tbTemplateZipFileCriteria.createCriteria();

            crit.andIsDeletedEqualTo(IsDeletedEnum.NO.getValue());

            tbTemplateZipFileCriteria.setOrderByClause(Constants.CREATEDDESC);

            List<TbTemplateZipFile> zipFileList = tbTemplateZipFileMapper.selectByParam(tbTemplateZipFileCriteria);

            if(CollectionUtils.isNotEmpty(zipFileList)) {

                redisManager.set(key.getBytes(), zipFileList.get(0).getFileVersion().getBytes());

                return zipFileList.get(0).getFileVersion();
            }else{

                return null;
            }
        }

        String resultStr = new String(value);

        return resultStr;
    }

    @Override
    public void setNewVersion(String newVersion){
        if(StringUtils.isNotEmpty(newVersion)){
            redisManager.set(key.getBytes(),newVersion.getBytes());
        }
    }
}
