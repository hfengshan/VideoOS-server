package com.videojj.videoservice.service;

import com.videojj.videoservice.bo.RoleAndNodeBo;

/**
 * @Author @videopls.com
 * Created by  on 2018/7/24 下午5:43.
 * @Description:
 */
public interface LoginService {


    RoleAndNodeBo getAuthInfoByUsername(String username);
}
