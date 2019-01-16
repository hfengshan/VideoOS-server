package com.videojj.videoportal.controller;

import com.videojj.videocommon.constant.Constants;
import com.videojj.videocommon.dto.base.BaseResponseDTO;
import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videoservice.annotation.PermissionService;
import com.videojj.videoservice.bo.TemplateAddBo;
import com.videojj.videoservice.entity.TbTemplate;
import com.videojj.videoservice.entity.TbTemplateCriteria;
import com.videojj.videoservice.service.CheckService;
import com.videojj.videoservice.service.TemplateExtService;
import com.videojj.videoservice.util.BaseSuccessResultUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author @videopls.com
 * Created by  on 2018/10/26 上午11:28.
 * @Description:
 */
@Controller
public class TemplateExtControlelr {

    @Resource
    private TemplateExtService templateExtService;

    @Resource
    private CheckService checkService;

    private final String TYPE_NAME = "interactionTypeName";

    private final String TYPE_ID = "interactionTypeId";

    private final String TEMPLATE_NAME = "templateName";

    private final String TEMPLATE_ID = "templateId";

    private final String FILE = "file";

    @PermissionService
    @RequestMapping(value = "/videoos/template/add", method = RequestMethod.POST)
    public @ResponseBody BaseResponseDTO add(HttpServletRequest request, @RequestAttribute String username) throws Exception{

        MultipartFile fileParam = null;

        String fileName = null;

        String interactionTypeName = request.getParameter(TYPE_NAME).toString();

        String interactionTypeId = request.getParameter(TYPE_ID).toString();

        String templateName = request.getParameter(TEMPLATE_NAME).toString();

        Boolean result = templateExtService.addCheckExist(templateName);

        if(result){

            BaseResponseDTO res = new BaseResponseDTO();

            res.setResCode(Constants.FAILCODE);

            res.setResMsg("主题名称已经存在，请更换主题名称");

            return res;
        }

        try {

            fileParam = ((MultipartRequest) request).getFile(FILE);

            fileName = fileParam.getOriginalFilename();

        } catch (Exception e) {

            BaseResponseDTO responseDTO = new BaseResponseDTO();

            responseDTO.setResMsg("没有文件");

            responseDTO.setResCode(Constants.FAILCODE);

            return responseDTO;
        }

        TemplateAddBo templateAddBo = templateExtService.uploadFile(fileParam.getInputStream(),fileParam.getOriginalFilename());

        if(!templateAddBo.getUploadResult()){

            BaseResponseDTO res = new BaseResponseDTO();

            res.setResCode(Constants.FAILCODE);

            res.setResMsg(templateAddBo.getUploadMsg());

            return res;
        }

        templateAddBo.setInteractionTypeId(Integer.parseInt(interactionTypeId));

        templateAddBo.setInteractionTypeName(interactionTypeName);

        templateAddBo.setTemplateName(templateName);

        templateAddBo.setUsername(username);

        templateAddBo.setZipFileName(fileName);

        templateExtService.addTemplateInfo(templateAddBo);

        return BaseSuccessResultUtil.producessSuccess();
    }

    @PermissionService
    @RequestMapping(value = "/videoos/template/update", method = RequestMethod.POST)
    public
    @ResponseBody
    BaseResponseDTO update(HttpServletRequest request, @RequestAttribute String username) {

        MultipartFile fileParam = ((MultipartRequest) request).getFile(FILE);

        TemplateAddBo templateAddBo = null;

        Integer templateId = Integer.parseInt(request.getParameter(TEMPLATE_ID));

        String interactionTypeName = request.getParameter(TYPE_NAME).toString();

        String interactionTypeId = request.getParameter(TYPE_ID).toString();

        String templateName = request.getParameter(TEMPLATE_NAME).toString();

        Boolean result = templateExtService.updateCheckExist(templateName,templateId);

        if(result){

            BaseResponseDTO res = new BaseResponseDTO();

            res.setResMsg("主题名称已经存在，请更换主题名称");

            res.setResCode(Constants.FAILCODE);

            return res;
        }
        if(checkService.isInUseTemplateId(templateId)){

            BaseResponseDTO res = new BaseResponseDTO();

            res.setResCode(Constants.FAILCODE);

            res.setResMsg("主题在使用中，不能更新");

            return(res);
        }

        if(null != fileParam){

            String fileName = fileParam.getOriginalFilename();

            templateAddBo = templateExtService.updateUploadFile(fileParam,templateId);

            templateAddBo.setZipFileName(fileName);

        }else{

            templateAddBo = new TemplateAddBo();

            templateAddBo.setUploadResult(true);
        }

        if(!templateAddBo.getUploadResult()){

            BaseResponseDTO res = new BaseResponseDTO();

            res.setResMsg(templateAddBo.getUploadMsg());

            res.setResCode(Constants.FAILCODE);

            return res;
        }

        templateAddBo.setInteractionTypeId(Integer.parseInt(interactionTypeId));

        templateAddBo.setInteractionTypeName(interactionTypeName);

        templateAddBo.setTemplateName(templateName);

        templateAddBo.setUsername(username);

        templateExtService.updateRelationInfo(templateAddBo,templateId);

        return BaseSuccessResultUtil.producessSuccess();
    }
}