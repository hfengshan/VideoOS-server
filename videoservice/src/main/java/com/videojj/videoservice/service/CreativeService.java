package com.videojj.videoservice.service;

import com.videojj.videoservice.dto.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/8 下午4:28.
 * @Description:
 */
public interface CreativeService {

    void addCreative(AddCreativeRequestDTO request);

    void deleteCreativeByCreativeId(Integer creativeId, String username);

    AddUploadCreativeResponseDTO addUpload(HttpServletRequest request, String username, MultipartFile multipartTemplateFile, Integer creativeId);

    UpdateUploadCreativeResponseDTO updateUpload(HttpServletRequest request,String username, Integer creativeFileId, Integer creativeId, MultipartFile multipartTemplateFile);

    void updateInfo(UpdateCreativeRequestDTO request, String username);

    void deleteCreativeFileByUrl(String fileUrl, String username);

    CreativePageInfoResponseDTO queryPageInfoByParam(Integer interactionTypeId);

    CreativeDetailInfoResponseDTO queryDetailById(Integer creativeId);

    CreativePageInfoResponseDTO queryAll(Integer interactionType);
}
