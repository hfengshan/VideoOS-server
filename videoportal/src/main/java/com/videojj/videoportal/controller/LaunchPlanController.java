package com.videojj.videoportal.controller;

import com.videojj.videocommon.dto.base.BaseResponseDTO;
import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videoservice.annotation.PageControllerService;
import com.videojj.videoservice.annotation.PermissionService;
import com.videojj.videoservice.dto.*;
import com.videojj.videoservice.service.CheckService;
import com.videojj.videoservice.service.LaunchPlanService;
import com.videojj.videoservice.util.BaseSuccessResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Author
 * Created by  on 2018/8/7 下午5:15.
 * Description:
 */
@Controller
public class LaunchPlanController {

    @Resource
    private LaunchPlanService launchPlanService;

    @Resource
    private CheckService checkService;

    @PermissionService
    @RequestMapping(value = "/videoos/launchplan/add", method = RequestMethod.POST)
    public @ResponseBody
    BaseResponseDTO add(@RequestBody @Validated AddLaunchPlanRequestDTO request, @RequestAttribute String username) {

        request.setUsername(username);

        launchPlanService.addLaunchPlanService(request);

        return BaseSuccessResultUtil.producessSuccess();
    }

    @PermissionService
    @RequestMapping(value = "/videoos/launchplan/launch", method = RequestMethod.POST)
    public @ResponseBody
    BaseResponseDTO launch(@RequestBody @Validated LaunchLaunchPlanRequestDTO request) {

        LaunchPlanDetailInfoResponseDTO value = launchPlanService.queryDetailInfoById(request.getLaunchPlanId());
        String launchPlanName = checkService.checkLaunchPlan(value.getLaunchVideoId(), value.getLaunchDateStart(),
                value.getLaunchDateEnd(), value.getLaunchTimeType(), value.getLaunchTime(), value.getLaunchLenTime());
        if (StringUtils.isNotEmpty(launchPlanName)) {
            throw new ServiceException("后端校验失败，该投放计划与投放计划"+launchPlanName+"时间冲突，无法投放，需下线被冲突的投放计划才行。");
        }

        launchPlanService.launchLaunchPlanService(request);

        return BaseSuccessResultUtil.producessSuccess();
    }

    @PermissionService
    @RequestMapping(value = "/videoos/launchplan/modify", method = RequestMethod.POST)
    public @ResponseBody
    BaseResponseDTO update(@RequestBody UpdateLaunchPlanRequestDTO request,@RequestAttribute String username) throws Exception {

        request.setUsername(username);

        launchPlanService.updateLaunchPlanService(request);

        return BaseSuccessResultUtil.producessSuccess();

    }

    @PageControllerService(orderByDefaultValue = "tlp.gmt_created desc")
    @PermissionService
    @RequestMapping(value = "videoos/launchplan/queryByPage", method = RequestMethod.GET)
    public @ResponseBody
    LaunchPlanPageInfoResponseDTO queryByPage(@RequestParam(required = false) Integer interactionTypeId,@RequestParam(required = false) Byte launchTimeType) {
        return launchPlanService.queryOperationPageInfo(interactionTypeId,launchTimeType);
    }

    @PermissionService
    @RequestMapping(value = "/videoos/launchplan/audit", method = RequestMethod.POST)
    public @ResponseBody
    BaseResponseDTO update(@RequestBody AuditLaunchPlanRequestDTO request , @RequestAttribute String username) {

        request.setUsername(username);

        launchPlanService.auditLaunchPlanService(request);

        return BaseSuccessResultUtil.producessSuccess();

    }

    @PermissionService
    @RequestMapping(value = "/videoos/launchplan/delete", method = RequestMethod.POST)
    public @ResponseBody
    BaseResponseDTO delete(@RequestBody DeleteLaunchPlanRequestDTO request,@RequestAttribute String username) {

        request.setUsername(username);

        launchPlanService.deleteLaunchPlanService(request);

        return BaseSuccessResultUtil.producessSuccess();

    }

    @PermissionService
    @RequestMapping(value = "/videoos/launchplan/offline", method = RequestMethod.POST)
    public @ResponseBody
    BaseResponseDTO offline(@RequestBody DeleteLaunchPlanRequestDTO request,@RequestAttribute String username) {

        request.setUsername(username);

        launchPlanService.offlineLaunchPlanService(request);

        return BaseSuccessResultUtil.producessSuccess();

    }

    @PermissionService
    @RequestMapping(value = "videoos/launchplan/queryDetail", method = RequestMethod.GET)
    public @ResponseBody
    LaunchPlanDetailInfoResponseDTO queryDetail(@RequestParam Integer launchPlanId) {

        LaunchPlanDetailInfoResponseDTO res = launchPlanService.queryDetailInfoById(launchPlanId);

        return res;
    }

}
