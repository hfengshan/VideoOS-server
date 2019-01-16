package com.videojj.videoportal.controller;

import com.videojj.videoservice.annotation.PermissionService;
import com.videojj.videoservice.dto.OperationLogPageInfoResponseDTO;
import com.videojj.videoservice.service.OperationLogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author @videopls.com
 * Created by  on 2018/10/11 下午2:33.
 * @Description:
 */
@Controller
public class OperationLogController {

    @Resource
    private OperationLogService operationLogService;

    @PermissionService
    @RequestMapping(value = "/videoos/operationLog/queryByPage", method = RequestMethod.GET)
    public @ResponseBody
    OperationLogPageInfoResponseDTO queryByPage(@RequestParam Integer currentPage,@RequestParam Integer pageSize) {
        OperationLogPageInfoResponseDTO operationLogPageInfoResponseDTO = operationLogService.queryAllByPage(currentPage, pageSize);
        return operationLogPageInfoResponseDTO;
    }
}
