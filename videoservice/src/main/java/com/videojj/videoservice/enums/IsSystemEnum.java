package com.videojj.videoservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author @videopls.com
 * Created by  on 2018/9/3 下午7:15.
 * @Description:
 */
@AllArgsConstructor
@Getter
public enum IsSystemEnum {

    YES("系统级","Y"),

    NO("非系统级","N");

    private String name;

    private String value;

}
