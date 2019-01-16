package com.videojj.videoportal.controller;

import com.videojj.videocommon.dto.base.BaseResponseDTO;
import com.videojj.videoservice.annotation.PermissionService;
import com.videojj.videoservice.dto.*;
import com.videojj.videoservice.service.UserService;
import com.videojj.videoservice.util.BaseSuccessResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/10 下午1:40.
 * @Description:
 */

@Controller
public class UserController {

    @Resource
    private UserService userService;

    private static Logger log = LoggerFactory.getLogger("UserController");

    @PermissionService
    @RequestMapping(value = "/videoos/user/add", method = RequestMethod.POST)
    public @ResponseBody
    BaseResponseDTO add(@RequestBody AddUserRequestDTO request, @RequestAttribute String username) {

        BaseResponseDTO result = userService.addUserService(request,username);

        return result ;

    }

    @PermissionService
    @RequestMapping(value = "/videoos/user/update", method = RequestMethod.POST)
    public @ResponseBody
    BaseResponseDTO update(@RequestBody UpdateUserRequestDTO request, @RequestAttribute String username) {

        BaseResponseDTO result = userService.updateUserService(request,username);

        return result;
    }

    @PermissionService
    @RequestMapping(value = "videoos/user/queryByPage", method = RequestMethod.GET)
    public @ResponseBody
    UserPageInfoResponseDTO queryInteractionTemplateByPage(@RequestParam Integer currentPage,
                                                                 @RequestParam Integer pageSize,
                                                                 @RequestParam(required = false) String qryusername
                                                                 ) {

        QueryUserRequestDTO param = new QueryUserRequestDTO();

        param.setCurrentPage(currentPage);

        param.setPageSize(pageSize);

        param.setQryusername(qryusername);

        UserPageInfoResponseDTO res = userService.queryPageInfoByParam(param);

        return res;


    }

    @PermissionService
    @RequestMapping(value = "/videoos/user/delete", method = RequestMethod.POST)
    public @ResponseBody
    BaseResponseDTO delete(@RequestBody DeleteUserRequestDTO request,@RequestAttribute String username) {

        userService.deleteUserService(request,username);

        return BaseSuccessResultUtil.producessSuccess();
    }

    @PermissionService
    @RequestMapping(value = "/videoos/user/updateByUsername")
    public @ResponseBody BaseResponseDTO updateByUsername(@RequestBody UpdateUserPasswordRequestDTO request){

        userService.updateByUsernameService(request.getUsername(),request.getPassword());

        return BaseSuccessResultUtil.producessSuccess();
    }

}
