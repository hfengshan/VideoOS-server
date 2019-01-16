package com.videojj.videoservice.service;

import com.videojj.videocommon.dto.base.BaseResponseDTO;
import com.videojj.videoservice.dto.*;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/14 下午2:26.
 * @Description:
 */
public interface RoleService {

    BaseResponseDTO addRoleService(AddRoleRequestDTO request, String username);

    BaseResponseDTO updateRoleService(UpdateRoleRequestDTO request, String username);

    void deleteRoleService(DeleteRoleRequestDTO request, String username);

    RolePageInfoResponseDTO queryPageInfoByParam(QueryRoleRequestDTO param);

    RoleOwnAuthInfoResponseDTO queryAuthInfoByParam(Integer roleId);

    AllRoleInfoResponseDTO queryAll();
}
