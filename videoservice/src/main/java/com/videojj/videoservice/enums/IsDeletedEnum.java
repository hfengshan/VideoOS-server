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
public enum IsDeletedEnum {

    YES("已删除","Y"),

    NO("未删除","N");

    private String name;

    private String value;


    public static IsDeletedEnum getEnumByValue(String value) {

        if (values() != null && values().length > 0 && null != value) {

            for (IsDeletedEnum en : values()) {

                if (en.value.equals(value)) {

                    return en;
                }
            }
        }

        return null;
    }

    public static IsDeletedEnum getEnumByName(String name) {

        if (values() != null && values().length > 0 && null != name) {

            for (IsDeletedEnum en : values()) {

                if (en.name.equals(name)) {

                    return en;
                }
            }
        }

        return null;
    }

    public static String getValueByName(String name) {

        if (values() != null && values().length > 0 && null != name) {

            for (IsDeletedEnum en : values()) {

                if (en.name.equals(name)) {

                    return en.value;
                }
            }
        }

        return null;
    }
}
