package com.videojj.videoservice.service;

import com.videojj.videoservice.dto.*;

import java.text.ParseException;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/7 下午7:02.
 * @Description:
 */
public interface LaunchPlanService {

    boolean existsSameLaunchPlanOperationName(String launchPlanOperationName);

    void addLaunchPlanService(AddLaunchPlanRequestDTO request);

    void updateLaunchPlanService(UpdateLaunchPlanRequestDTO request) throws ParseException;

    LaunchPlanPageInfoResponseDTO queryPageInfo(Integer interactionTypeId);

    LaunchPlanPageInfoResponseDTO queryOperationPageInfo(Integer interactionTypeId,Byte launchTimeType);

    void auditLaunchPlanService(AuditLaunchPlanRequestDTO request);

    void deleteLaunchPlanService(DeleteLaunchPlanRequestDTO request);

    LaunchPlanDetailInfoResponseDTO queryDetailInfoById(Integer launchPlan);

    void offlineLaunchPlanService(DeleteLaunchPlanRequestDTO request);

    String[] splitLaunchVideoIds(String launchVideoIds);

    void launchLaunchPlanService(LaunchLaunchPlanRequestDTO request);
}
