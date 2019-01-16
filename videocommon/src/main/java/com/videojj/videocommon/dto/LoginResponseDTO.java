package com.videojj.videocommon.dto;

import com.videojj.videocommon.dto.base.BaseResponseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


/**
 * @Author @videopls.com
 * Created by  on 2018/7/25 上午10:14.
 * @Description:
 */
@Getter
@Setter
@ToString
public class LoginResponseDTO extends BaseResponseDTO {

    private String token;

    private String roleName;

    private Integer roleId;

    private List<Integer> authorList;


}
