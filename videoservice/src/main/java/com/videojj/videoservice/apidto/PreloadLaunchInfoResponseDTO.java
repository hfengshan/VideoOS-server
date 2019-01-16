package com.videojj.videoservice.apidto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/24 下午2:08.
 * @Description:
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class PreloadLaunchInfoResponseDTO {

    private String resMsg;

    private String attachInfo;

    private String resCode;

    private List<String> fileUrlList;

}
