package com.videojj.videoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author //@videopls.com
 * Created by // on 2018/8/6 下午8:56.
 * @Description:
 */
@Getter
@Setter
@ToString
public class ConfirmAddTemplateRequestDTO {

    /**主题名称*/
    private String interactionTemplateName;
    /**模版文件名称*/
    private String fileName;
    /**旧的版本号*/
    private String oldVersion;
    /**应用名称*/
//    private String interactionTypeName;
    /**类型id*/
    private Integer interactionTypeId;
    /**压缩文件名称*/
    private String compressFileName;

    private String username;

    private String templateFileSourceName;
}
