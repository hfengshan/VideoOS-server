package com.videojj.videoportal.controller;

import com.videojj.videocommon.constant.Constants;

import com.videojj.videocommon.dto.base.BaseResponseDTO;
import com.videojj.videoservice.annotation.PageControllerService;
import com.videojj.videoservice.annotation.PermissionService;
import com.videojj.videoservice.dto.*;
import com.videojj.videoservice.entity.TbInteraction;
import com.videojj.videoservice.service.CheckService;
import com.videojj.videoservice.service.InteractionTypeService;
import com.videojj.videoservice.util.BaseSuccessResultUtil;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/1 下午2:33.
 * @Description:
 */
@Controller
public class InteractionTypeController {

    @Resource
    private InteractionTypeService interactionService;

    @Resource
    private CheckService checkService;

    @PermissionService
    @RequestMapping(value = "/videoos/interactionType/add", method = RequestMethod.POST)
    public @ResponseBody BaseResponseDTO add(HttpServletRequest request) throws Exception{

        MultipartFile multipartFile = null;

        String fileName = null;

        try {
            multipartFile = ((MultipartRequest) request).getFile("file");

            fileName = multipartFile.getOriginalFilename();
        }catch(Exception e){

            BaseResponseDTO responseDTO = new BaseResponseDTO();

            responseDTO.setResMsg("参数错误，文件信息没有传递过来");

            responseDTO.setResCode(Constants.FAILCODE);

            return responseDTO;
        }
        String interactionTypeName = request.getParameter("interactionTypeName").toString();

        String username = request.getAttribute("username").toString();

        List<TbInteraction> interList =  interactionService.selectByName(interactionTypeName);

        if(!CollectionUtils.isEmpty(interList)){

            BaseResponseDTO res = new BaseResponseDTO();

            res.setResCode(Constants.FAILCODE);

            res.setResMsg("名称重复，请更换应用名称");

            return res;
        }

        interactionService.addInteractionInfo(multipartFile.getInputStream(),fileName,interactionTypeName,username);

        return BaseSuccessResultUtil.producessSuccess();

    }


    @PageControllerService(orderByDefaultValue = "gmt_created desc")
    @PermissionService
    @RequestMapping(value = "/videoos/interactionType/queryByPage", method = RequestMethod.GET)
    public @ResponseBody
    InteractionPageInfoResponseDTO queryByPage(@RequestParam Integer currentPage,@RequestParam Integer pageSize) {

        InteractionPageInfoResponseDTO interactionPageInfoResponseDTO = interactionService.queryAllByPage(currentPage, pageSize);

        return interactionPageInfoResponseDTO;
    }

    @PageControllerService(orderByDefaultValue = "gmt_created desc",pageSizeDefaultValue = PageControllerService.MAX_PAGE_SIZE)
    @PermissionService
    @RequestMapping(value = "/videoos/interactionType/queryAll", method = RequestMethod.GET)
    public @ResponseBody
    AllInteractionInfoResponseDTO queryAll(HttpServletRequest request) {

        AllInteractionInfoResponseDTO allInteractionInfoResponseDTO = interactionService.queryAll();

        return allInteractionInfoResponseDTO;
    }


    @PermissionService
    @RequestMapping(value = "/videoos/interactionType/delete", method = RequestMethod.POST)
    public @ResponseBody
    BaseResponseDTO delete(@RequestBody InteractionDeleteRequestDTO param,@RequestAttribute String username) {

        if(checkService.isInUseInteractionTypeName(param.getInteractionTypeName())){

            BaseResponseDTO res = new BaseResponseDTO();

            res.setResCode(Constants.FAILCODE);

            res.setResMsg("该应用或该应用绑定的主题/素材处于在线状态，无法删除。");

            return(res);
        }

        interactionService.deleteInteractionByTypeName(param.getInteractionTypeName(),username);

        return BaseSuccessResultUtil.producessSuccess();
    }

    @PermissionService
    @RequestMapping(value = "/videoos/interactionType/query", method = RequestMethod.GET)
    public @ResponseBody
    InteractionInfoResponseDTO query(@RequestParam Integer interactionId) {

        InteractionInfoResponseDTO interactionPageInfoResponseDTO = interactionService.queryById(interactionId);

        return interactionPageInfoResponseDTO;
    }

    @PermissionService
    @RequestMapping(value = "/videoos/interactionType/modify", method = RequestMethod.POST)
    public @ResponseBody BaseResponseDTO update(HttpServletRequest request,@RequestAttribute String username) {

        MultipartFile multipartFile = ((MultipartRequest) request).getFile("file");

        String fileName = null;

        if(null != multipartFile) {

            fileName = multipartFile.getOriginalFilename();
        }
        Integer interactionTypeId = Integer.valueOf(request.getParameter("interactionTypeId"));

        String interactionTypeName = request.getParameter("interactionTypeName").toString();

        if(checkService.isInUseInteractionId(interactionTypeId)){

            BaseResponseDTO res = new BaseResponseDTO();

            res.setResCode(Constants.FAILCODE);

            res.setResMsg("互动应用在使用中，不能修改");

            return(res);
        }
        List<TbInteraction> interList =  interactionService.selectByName(interactionTypeName);

        if(!CollectionUtils.isEmpty(interList)
                &&interList.get(0).getId().intValue() != interactionTypeId.intValue()){

            BaseResponseDTO res = new BaseResponseDTO();

            res.setResMsg("名称重复，请更换应用名称");

            res.setResCode(Constants.FAILCODE);

            return res;
        }

        interactionService.updateInteractionInfo(multipartFile,fileName,interactionTypeName,username,interactionTypeId);

        return BaseSuccessResultUtil.producessSuccess();
    }



    @PermissionService
    @RequestMapping(value = "/videoos/interactionType/queryInteractionInfo", method = RequestMethod.GET)
    public @ResponseBody
    QueryInteractionInfoResponseDTO queryInteractionInfo(@RequestParam Integer creativeId) {

        QueryInteractionInfoResponseDTO interactionPageInfoResponseDTO = interactionService.queryInteractionInfo(creativeId);

        return interactionPageInfoResponseDTO;
    }



}
