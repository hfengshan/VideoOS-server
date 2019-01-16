package com.videojj.videoservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author @videopls.com
 * Created by  on 2018/9/3 下午7:05.
 * @Description:
 */
@AllArgsConstructor
@Getter
public enum CreativeStatusEnum {

    USE("使用中",(byte) 1),

    NO_USE("未使用",(byte) 0);

    private String name;

    private Byte value;


    public static CreativeStatusEnum getEnumByValue(String value) {

        if (values() != null && values().length > 0 && null != value) {

            for (CreativeStatusEnum en : values()) {

                if (en.value.equals(value)) {

                    return en;
                }
            }
        }

        return null;
    }

    public static CreativeStatusEnum getEnumByName(String name) {

        if (values() != null && values().length > 0 && null != name) {

            for (CreativeStatusEnum en : values()) {

                if (en.name.equals(name)) {

                    return en;
                }
            }
        }

        return null;
    }

    public static Byte getValueByName(String name) {

        if (values() != null && values().length > 0 && null != name) {

            for (CreativeStatusEnum en : values()) {

                if (en.name.equals(name)) {

                    return en.value;
                }
            }
        }

        return null;
    }

}
