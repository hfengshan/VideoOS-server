package com.videojj.videoservice.service;

import com.videojj.videoservice.bo.TemplateAddBo;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @Author @videopls.com
 * Created by  on 2018/10/26 上午11:30.
 * @Description:
 */
public interface TemplateExtService {
    
    TemplateAddBo uploadFile(InputStream uploadStream, String zipFileName);

    void addTemplateInfo(TemplateAddBo templateAddBo);

    TemplateAddBo updateUploadFile(MultipartFile fileParam,Integer templateId);

    void updateRelationInfo(TemplateAddBo templateAddBo,Integer templateId);

    Boolean addCheckExist(String templateName);

    Boolean updateCheckExist(String templateName,Integer templateId);
}
