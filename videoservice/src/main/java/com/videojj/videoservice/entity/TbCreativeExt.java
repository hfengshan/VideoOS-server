package com.videojj.videoservice.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/16 上午11:54.
 * @Description:
 */
@Getter
@Setter
@ToString
public class TbCreativeExt extends TbCreative{

    private String interactionName;

    private String templateName;
}
