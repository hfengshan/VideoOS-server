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
public enum LaunchTimeTypeEnum {

    VIDIO_TIME("视频时间",(byte) 0),

    REAL_TIME("实时投放",(byte) 1),

    BJ_TIME("北京时间",(byte) 2);

    private String name;

    private Byte value;


    public static LaunchTimeTypeEnum getEnumByValue(String value) {

        if (values() != null && values().length > 0 && null != value) {

            for (LaunchTimeTypeEnum en : values()) {

                if (en.value.equals(value)) {

                    return en;
                }
            }
        }

        return null;
    }

    public static LaunchTimeTypeEnum getEnumByName(String name) {

        if (values() != null && values().length > 0 && null != name) {

            for (LaunchTimeTypeEnum en : values()) {

                if (en.name.equals(name)) {

                    return en;
                }
            }
        }

        return null;
    }

    public static Byte getValueByName(String name) {

        if (values() != null && values().length > 0 && null != name) {

            for (LaunchTimeTypeEnum en : values()) {

                if (en.name.equals(name)) {

                    return en.value;
                }
            }
        }

        return null;
    }

}
