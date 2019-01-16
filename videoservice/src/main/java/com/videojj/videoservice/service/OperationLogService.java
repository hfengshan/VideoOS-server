package com.videojj.videoservice.service;

import com.videojj.videoservice.dto.OperationLogPageInfoResponseDTO;

/**
 * @Author @videopls.com
 * Created by  on 2018/10/10 下午2:58.
 * @Description:
 */
public interface OperationLogService {

    OperationLogPageInfoResponseDTO queryAllByPage(Integer currentPage, Integer pageSize);

    void addOperationLog(String operationDesc, Integer operationType);

    void writeOperationLog_23(String templateName);

    void writeOperationLog_42(String launchPlanName);

    void writeOperationLog_43(String launchPlanName);

    void writeOperationLog_63(String creativeName);

    void writeOperationLog_83(String roleName);

    void writeOperationLog_102(String username, String originalRoleName, String latestRoleName);

    void writeOperationLog_104(String username);
}
