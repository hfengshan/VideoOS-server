package com.videojj.videopublicapi.controller;

import com.videojj.videocommon.constant.Constants;
import com.videojj.videoservice.apidto.CommonResponseDTO;
import com.videojj.videoservice.encry.CommonAesService;

import javax.annotation.Resource;

/**
 * @Author @videopls.com
 * Created by  on 2018/12/18 下午3:48.
 * @Description:
 */
public abstract class AbstractController {

    @Resource
    protected CommonAesService commonAesService;

    public static final String REQUEST_FIELD_VIDEO_ID = "videoId";

    public static final String REQUEST_FIELD_CREATIVE_NAME = "creativeName";

    public static final String REQUEST_FIELD_BUSINESS_PARAM = "businessParam";

    public static final String REQUEST_FIELD_BUSINESS_INFO = "businessInfo";

    public static final String REQUEST_FIELD_USER_ID = "userId";

    public static final String REQUEST_FIELD_CREATIVE_ID = "creativeId";

    public static final String REQUEST_FIELD_EXTRA_INFO = "extraInfo";

    protected String returnFailJsonString(String resMsg) {

        CommonResponseDTO resDTO = new CommonResponseDTO();

        resDTO.setResCode(Constants.FAILCODE);

        resDTO.setResMsg(resMsg);

        return commonAesService.encryResult(resDTO);
    }
}
