package com.videojj.videoservice.service;

import com.videojj.videoservice.dto.AllInteractionInfoResponseDTO;
import com.videojj.videoservice.dto.InteractionInfoResponseDTO;
import com.videojj.videoservice.dto.InteractionPageInfoResponseDTO;
import com.videojj.videoservice.dto.QueryInteractionInfoResponseDTO;
import com.videojj.videoservice.entity.TbInteraction;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/1 下午2:58.
 * @Description:
 */
public interface InteractionTypeService {

    /**
     * @Author:
     * @Description:
     * @Date: 下午3:00 2018/8/1
     */
    void addInteractionInfo(InputStream fileInputStream, String fileName, String interactionTypeName,String username);
    /**
     * @Author:
     * @Description:
     * @Date: 下午5:39 2018/8/1
     */
    InteractionPageInfoResponseDTO queryAllByPage(Integer currentPage, Integer pageSize);
    /**
     * @Author:
     * @Description:
     * @Date: 下午8:18 2018/8/1
     */
    void deleteInteractionByTypeName(String interactionTypeName,String username);

    InteractionInfoResponseDTO queryByParam(String interactionTypeName);

    void updateInteractionInfo(MultipartFile multipartFile, String fileName, String interactionTypeName, String username, Integer interactionTypeId);

    AllInteractionInfoResponseDTO queryAll();

    InteractionInfoResponseDTO queryById(Integer interactionId);

    List<TbInteraction> selectByName(String interactionTypeName);

    String getInteractionDefaultImgUrl();

    QueryInteractionInfoResponseDTO queryInteractionInfo(Integer creativeId);
}
