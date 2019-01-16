package com.videojj.videoservice.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author @videopls.com
 * Created by  on 2018/10/26 上午11:53.
 * @Description:
 */
@Getter
@Setter
@ToString
public class TemplateAddBo {

    private String interactionTypeName;

    private Integer interactionTypeId;

    private String templateName;

    private Boolean uploadResult;

    private String uploadMsg;

    private String newVersion;

    private List<String> uploadFileNameList;

    private String username;

    private String zipFileName;
}
