package com.videojj.videoportal.controller;

import com.videojj.videocommon.dto.base.BaseResponseDTO;
import com.videojj.videoservice.annotation.PermissionService;
import com.videojj.videoservice.dto.*;
import com.videojj.videoservice.service.RoleService;
import com.videojj.videoservice.util.BaseSuccessResultUtil;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/14 下午2:24.
 * @Description:
 */

@RestController
public class RoleController {

    @Resource
    private RoleService roleService;

    @PermissionService
    @RequestMapping(value = "/videoos/role/add", method = RequestMethod.POST)
    public @ResponseBody
    BaseResponseDTO add(@RequestBody AddRoleRequestDTO request, @RequestAttribute String username) {

        BaseResponseDTO responseDTO = roleService.addRoleService(request,username);

        return responseDTO;

    }

    @PermissionService
    @RequestMapping(value = "/videoos/role/update", method = RequestMethod.POST)
    public @ResponseBody
    BaseResponseDTO update(@RequestBody UpdateRoleRequestDTO request, @RequestAttribute String username) {

        BaseResponseDTO responseDTO = roleService.updateRoleService(request,username);

        return responseDTO;
    }

    @PermissionService
    @RequestMapping(value = "/videoos/role/delete", method = RequestMethod.POST)
    public @ResponseBody
    BaseResponseDTO delete(@RequestBody DeleteRoleRequestDTO request, @RequestAttribute String username) {

        roleService.deleteRoleService(request,username);

        return BaseSuccessResultUtil.producessSuccess();

    }

    @PermissionService
    @RequestMapping(value = "videoos/role/queryByPage", method = RequestMethod.GET)
    public @ResponseBody
    RolePageInfoResponseDTO queryInteractionTemplateByPage(@RequestParam Integer currentPage,
                                                           @RequestParam Integer pageSize) {

        QueryRoleRequestDTO param = new QueryRoleRequestDTO();

        param.setCurrentPage(currentPage);

        param.setPageSize(pageSize);

        RolePageInfoResponseDTO res = roleService.queryPageInfoByParam(param);

        return res;

    }

    @PermissionService
    @RequestMapping(value = "videoos/role/queryAll", method = RequestMethod.GET)
    public @ResponseBody
    AllRoleInfoResponseDTO queryAllRole(HttpServletRequest request) {

        AllRoleInfoResponseDTO res = roleService.queryAll();

        return res;

    }

    @PermissionService
    @RequestMapping(value = "videoos/role/modifyQuery", method = RequestMethod.GET)
    public @ResponseBody
    RoleOwnAuthInfoResponseDTO queryInteractionTemplateByPage(@RequestParam Integer roleId) {

        RoleOwnAuthInfoResponseDTO res = roleService.queryAuthInfoByParam(roleId);

        return res;

    }
}
