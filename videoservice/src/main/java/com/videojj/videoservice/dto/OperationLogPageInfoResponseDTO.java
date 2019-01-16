package com.videojj.videoservice.dto;

import com.videojj.videocommon.dto.base.BaseResponseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author //@videopls.com
 * Created by // on 2018/10/10 下午5:30.
 * @Description:
 */
@Getter
@Setter
@ToString
public class OperationLogPageInfoResponseDTO extends BaseResponseDTO{


    private List<InnerOperationLog> operationLogList;

    private Integer totalRecord;

    private Integer totalPage;

    @Getter
    @Setter
    @ToString
    public static class InnerOperationLog{

        private Integer id;

        private String createdDate;

        private String username;

        private String operationDesc;

    }

}
