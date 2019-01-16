package com.videojj.videoportal.controller;

import com.videojj.videocommon.constant.Constants;
import com.videojj.videocommon.dto.base.BaseResponseDTO;
import com.videojj.videoservice.annotation.PermissionService;
import com.videojj.videoservice.dao.RedisSessionDao;
import com.videojj.videoservice.dto.*;
import com.videojj.videoservice.service.CheckService;
import com.videojj.videoservice.service.InteractionZipFileService;
import com.videojj.videoservice.util.BaseSuccessResultUtil;
import org.apache.shiro.session.Session;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * Created by  on 2018/8/3 上午10:11.
 *
 */
@Controller
@CrossOrigin
public class InteractionTemplateController {

    @Resource
    private InteractionZipFileService interactionZipFileService;

    @Resource
    private CheckService checkService;

    @PermissionService
    @RequestMapping(value = "/videoos/interactionTemplate/queryByPage", method = RequestMethod.GET)
    public @ResponseBody
    TemplatePageInfoResponseDTO queryInteractionTemplateByPage(@RequestParam Integer currentPage,@RequestParam Integer pageSize,@RequestParam(required = false) Integer interactionTypeId) {

        TemplatePageInfoRequestDTO templatePageInfoRequestDTO = new TemplatePageInfoRequestDTO();

        templatePageInfoRequestDTO.setCurrentPage(currentPage);

        templatePageInfoRequestDTO.setPageSize(pageSize);

        templatePageInfoRequestDTO.setInteractionTypeId(interactionTypeId);

        TemplatePageInfoResponseDTO res = interactionZipFileService.queryPageInfoByParam(templatePageInfoRequestDTO);

        return res;
    }

    @PermissionService
    @RequestMapping(value = "/videoos/interactionTemplate/queryAll", method = RequestMethod.GET)
    public @ResponseBody
    TemplatePageInfoResponseDTO queryInteractionTemplateByPage(@RequestParam(required = false) Integer interactionTypeId) {

        TemplatePageInfoRequestDTO templatePageInfoRequestDTO = new TemplatePageInfoRequestDTO();

        templatePageInfoRequestDTO.setInteractionTypeId(interactionTypeId);

        TemplatePageInfoResponseDTO res = interactionZipFileService.queryAllByParam(templatePageInfoRequestDTO);

        return res;
    }

    @PermissionService
    @RequestMapping(value = "/videoos/interactionTemplate/delete", method = RequestMethod.POST)
    public @ResponseBody
    BaseResponseDTO delete(@RequestBody TemplateDeleteRequestDTO param,@RequestAttribute String username) {

        param.setUsername(username);

        if(checkService.isInUseTemplateId(param.getInteractionTemplateId())){

            BaseResponseDTO res = new BaseResponseDTO();

            res.setResCode(Constants.FAILCODE);
            
            res.setResMsg("该主题或该主题绑定的素材处于在线状态，无法删除。");

            return(res);
        }

        interactionZipFileService.deleteInteractionByTypeName(param.getInteractionTemplateId(),param.getUsername());

        return BaseSuccessResultUtil.producessSuccess();
    }
    @PermissionService
    @RequestMapping(value = "/videoos/template/queryDetail", method = RequestMethod.GET)
    public @ResponseBody
    TemplateDetailInfoResponseDTO query(@RequestParam Integer templateId) {

        TemplateDetailInfoResponseDTO templateDetailInfoResponseDTO = interactionZipFileService.queryTemplateById(templateId);

        templateDetailInfoResponseDTO.setResMsg(Constants.COMMONSUCCESSMSG);

        templateDetailInfoResponseDTO.setResCode(Constants.SUCESSCODE);

        return templateDetailInfoResponseDTO;
    }
    @PermissionService
    @RequestMapping(value = "/videoos/template/download", method = RequestMethod.POST)
    public ResponseEntity<FileSystemResource> listExport(@RequestBody DownLoadTemplateRequestDTO param) {

        File file = interactionZipFileService.getTemplateFile(param.getTemplateId(),null);

        return export(file);
    }

    public ResponseEntity<FileSystemResource> export(File file) {
        if (file == null) {
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + file.getName());
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new FileSystemResource(file));
    }

    @RequestMapping(value = "/videoos/template/getdown", method = RequestMethod.GET)
    public ResponseEntity<FileSystemResource> getlistExport(@RequestParam Integer templateId,@RequestParam String token, HttpServletResponse response) {

        File file = interactionZipFileService.getTemplateFile(templateId,token);

        return export(file);
    }


}
