package com.videojj.videoservice.service;

import com.videojj.videoservice.dto.AddLaunchPlanRequestDTO;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/27 下午3:59.
 * @Description:
 */
public interface CheckService {

    public boolean isInUseTemplateId(Integer templateId);

    public boolean isInUseInteractionId(Integer interactionId);

    public boolean isInUseCreativeId(Integer creativeId);

    boolean isInUseCreativeUrl(String fileUrl);

    boolean isInUseTemplateName(String interactionTypeName);

    void updateUseStatus(Integer launchPlanId);

    boolean isInUseInteractionTypeName(String interactionTypeName);

    String checkLaunchPlan(AddLaunchPlanRequestDTO request);
}
