package com.videojj.videoportal.controller;

import com.videojj.videocommon.dto.base.BaseResponseDTO;
import com.videojj.videoservice.annotation.PageControllerService;
import com.videojj.videoservice.annotation.PermissionService;
import com.videojj.videoservice.dto.*;
import com.videojj.videoservice.service.CreativeService;
import com.videojj.videoservice.util.BaseSuccessResultUtil;
import com.videojj.videoservice.validation.group.GroupSequence1;
import com.videojj.videoservice.validation.group.GroupSequence2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/8 上午10:40.
 * @Description:
 */
@Validated
@Controller
@GroupSequence({GroupSequence1.class, GroupSequence2.class,CreativeController.class})
public class CreativeController {

    private static Logger log = LoggerFactory.getLogger(CreativeController.class);

    @Resource
    private CreativeService creativeService;

    @PermissionService
    @RequestMapping(value = "/videoos/creative/addUpload", method = RequestMethod.POST)
    public @ResponseBody
    AddUploadCreativeResponseDTO addUploadInteractionFile(HttpServletRequest request,@RequestParam("file") MultipartFile multipartTemplateFile, @RequestAttribute String username) {

        return creativeService.addUpload(request,username, multipartTemplateFile,null);
    }

    @PermissionService
    @RequestMapping(value = "/videoos/creative/add", method = RequestMethod.POST)
    public @ResponseBody
    BaseResponseDTO addCreative(@RequestBody @Validated AddCreativeRequestDTO request, @RequestAttribute String username) {

        request.setUsername(username);

        creativeService.addCreative(request);

        return BaseSuccessResultUtil.producessSuccess();
    }

    @PermissionService
    @RequestMapping(value = "/videoos/creative/delete", method = RequestMethod.POST)
    public @ResponseBody
    BaseResponseDTO deleteCreative(@RequestBody @Validated DeleteCreativeRequestDTO request,@RequestAttribute String username) {

        creativeService.deleteCreativeByCreativeId(request.getCreativeId(),username);

        return BaseSuccessResultUtil.producessSuccess();
    }
    @PermissionService
    @RequestMapping(value = "/videoos/creativefile/delete", method = RequestMethod.POST)
    public @ResponseBody
    BaseResponseDTO deleteCreativeFile(@RequestBody @Validated DeleteFileRequest request,@RequestAttribute String username) {

        creativeService.deleteCreativeFileByUrl(request.getFileUrl(),username);

        return BaseSuccessResultUtil.producessSuccess();
    }



    @PermissionService
    @RequestMapping(value = "/videoos/creative/modifyUpload", method = RequestMethod.POST)
    public @ResponseBody
    UpdateUploadCreativeResponseDTO updateUploadInteractionFile(HttpServletRequest request,@Validated UpdateUploadInteractionFileRequestDTO requestDTO,@RequestAttribute String username) {

        return creativeService.updateUpload(request,username,requestDTO.getCreativeFileId() ,requestDTO.getCreativeId(), requestDTO.getFile());
    }

    @PermissionService
    @RequestMapping(value = "/videoos/creative/modify", method = RequestMethod.POST)
    public @ResponseBody
    BaseResponseDTO updateCreative(@RequestBody @Validated UpdateCreativeRequestDTO request, @RequestAttribute String username) {

        creativeService.updateInfo(request,username);

        return BaseSuccessResultUtil.producessSuccess();

    }

    @PageControllerService
    @PermissionService
    @RequestMapping(value = "/videoos/creative/queryByPage", method = RequestMethod.GET)
    public @ResponseBody
    CreativePageInfoResponseDTO queryCreativeByPage(@RequestParam(required = false) Integer interactionTypeId) {
        return creativeService.queryPageInfoByParam(interactionTypeId);
    }

    @PermissionService
    @RequestMapping(value = "/videoos/creative/queryDetail", method = RequestMethod.GET)
    public @ResponseBody
    CreativeDetailInfoResponseDTO queryDetail(@RequestParam(required = false) @NotNull(message = "{com.videojj.validation.NotNull.creativeId.message}") Integer creativeId) {

        return creativeService.queryDetailById(creativeId);
    }

    @PermissionService
    @RequestMapping(value = "/videoos/creative/queryAll", method = RequestMethod.GET)
    public @ResponseBody
    CreativePageInfoResponseDTO queryAll(@RequestParam Integer interactionType) {

        return creativeService.queryAll(interactionType);
    }


}
