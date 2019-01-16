package com.videojj.videoservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author @videopls.com
 * Created by  on 2018/9/3 下午6:50.
 * @Description:
 */
@AllArgsConstructor
@Getter
public enum LaunchStatusEnum {

    NOT_PASS("审核未通过",(byte) 0),

    PASS("审核通过",(byte) 1),

    WAIT_PASS("待审核",(byte) 2),

    CLOSE("关闭",(byte) 3);

    private String name;

    private Byte value;


    public static LaunchStatusEnum getEnumByValue(String value) {

        if (values() != null && values().length > 0 && null != value) {

            for (LaunchStatusEnum en : values()) {

                if (en.value.equals(value)) {

                    return en;
                }
            }
        }

        return null;
    }

    public static LaunchStatusEnum getEnumByName(String name) {

        if (values() != null && values().length > 0 && null != name) {

            for (LaunchStatusEnum en : values()) {

                if (en.name.equals(name)) {

                    return en;
                }
            }
        }

        return null;
    }

    public static Byte getValueByName(String name) {

        if (values() != null && values().length > 0 && null != name) {

            for (LaunchStatusEnum en : values()) {

                if (en.name.equals(name)) {

                    return en.value;
                }
            }
        }

        return null;
    }

}
