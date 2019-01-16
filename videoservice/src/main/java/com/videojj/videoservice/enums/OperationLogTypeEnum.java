package com.videojj.videoservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author @videopls.com
 * Created by  on 2018/10/11 下午7:05.
 * @Description:
 */
@AllArgsConstructor
@Getter
public enum OperationLogTypeEnum {

    _1("新增[{0}]应用",1),
    _2("修改[{0}]应用",2),
    _3("删除[{0}]应用",3),

    _21("新增[{0}]主题",21),
    _22("修改[{0}]主题",22),
    _23("删除[{0}]主题",23),

    _41("新增[{0}]投放计划",41),
    _42("下线[{0}]投放计划",42),
    _43("投放[{0}]投放计划",43),

    _61("新增[{0}]素材",61),
    _62("修改[{0}]素材",62),
    _63("删除[{0}]素材",63),

    _81("新增[{0}]角色",81),
    _82("修改[{0}]角色",82),
    _83("删除[{0}]角色",83),

    _101("新增账号[{0}]",101),
    _102("修改账号[{0}]的角色从[{1}]到[{2}]",102),
    _103("修改账号[{0}]的密码",103),
    _104("删除账号[{0}]",104);

    private String operationDescTemplate;

    private Integer operationType;

    public static OperationLogTypeEnum getEnumByOperationType(Integer value) {

        if (values() != null && values().length > 0 && null != value) {

            for (OperationLogTypeEnum en : values()) {

                if (en.operationType.equals(value)) {

                    return en;
                }
            }
        }

        return null;
    }

}
