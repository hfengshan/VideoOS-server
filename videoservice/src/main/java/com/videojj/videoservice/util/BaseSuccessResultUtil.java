package com.videojj.videoservice.util;

import com.videojj.videocommon.constant.Constants;
import com.videojj.videocommon.dto.base.BaseResponseDTO;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/16 上午10:43.
 * @Description:
 */
public class BaseSuccessResultUtil {

    public static BaseResponseDTO producessSuccess(){

        BaseResponseDTO res = new BaseResponseDTO();

        res.setResCode(Constants.SUCESSCODE);

        res.setResMsg(Constants.COMMONSUCCESSMSG);

        return res;
    }
}
