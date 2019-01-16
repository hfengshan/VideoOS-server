package com.videojj.videoservice.service;

import com.videojj.videoservice.bo.InteractionCompressBo;
import com.videojj.videoservice.dto.*;
import com.videojj.videoservice.entity.TbTemplate;
import com.videojj.videoservice.entity.TbTemplateZipFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/6 下午3:04.
 * @Description:
 */
public interface InteractionZipFileService {

    TemplatePageInfoResponseDTO queryPageInfoByParam(TemplatePageInfoRequestDTO templatePageInfoRequestDTO);

    void deleteInteractionByTypeName(Integer interactionTemplateId,String username);

    TemplateDetailInfoResponseDTO queryTemplateById(Integer templateId);

    File getTemplateFile(Integer templateId,String token);

    TemplatePageInfoResponseDTO queryAllByParam(TemplatePageInfoRequestDTO templatePageInfoRequestDTO);

}
