package com.videojj.videocommon.dto.base;

import lombok.*;

/**
 * @Author @videopls.com
 * Created by  on 2018/7/25 上午10:12.
 * @Description:
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponseDTO {

    /**
     * 应答码
     */
    protected String            resCode;

    /**
     * 应答信息
     */
    protected String            resMsg;

    /**
     * 附加信息
     */
    protected String            attachInfo;
}
