package com.videojj.videoservice.service;

import com.videojj.videocommon.dto.base.BaseResponseDTO;
import com.videojj.videoservice.bo.LoginBo;
import com.videojj.videoservice.dto.*;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/10 下午2:00.
 * @Description:
 */
public interface UserService {

    public BaseResponseDTO addUserService(AddUserRequestDTO request, String username);

    BaseResponseDTO updateUserService(UpdateUserRequestDTO request, String username);

    UserPageInfoResponseDTO queryPageInfoByParam(QueryUserRequestDTO param);

    void deleteUserService(DeleteUserRequestDTO request, String username);

    LoginBo getAuthByUsername(String username);

    void updateByUsernameService(String username, String password);
}
