package com.videojj.videoportal.controller;

import com.videojj.videocommon.constant.Constants;
import com.videojj.videocommon.dto.LoginInfoDTO;
import com.videojj.videocommon.dto.LoginResponseDTO;
import com.videojj.videocommon.dto.base.BaseResponseDTO;
import com.videojj.videocommon.util.MD5Util;
import com.videojj.videoservice.bo.LoginBo;
import com.videojj.videoservice.config.TvSessionManager;
import com.videojj.videoservice.dao.RedisSessionDao;
import com.videojj.videoservice.dto.IsValidTokenResponseDTO;
import com.videojj.videoservice.service.LoginService;
import com.videojj.videoservice.service.UserService;
import com.videojj.videoservice.util.BaseSuccessResultUtil;
import com.videojj.videoservice.util.PermissionUtil;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @Author @videopls.com
 * Created by  on 2018/7/20 上午11:11.
 * @Description:
 */
@RestController
public class LoginController {

    private static Logger log = LoggerFactory.getLogger("LoginController");

    @Resource
    private RedisSessionDao redisSessionDao;

    @Resource
    private UserService userService;

    @RequestMapping(value = "/videoos/login", method = RequestMethod.POST)
    public LoginResponseDTO login(@RequestBody LoginInfoDTO namespace) {

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

        String username = namespace.getUsername();

        String password = namespace.getPassword();

        log.info("LoginController.login ==> request param.username is{}",username);

        if (StringUtils.isEmpty(username)) {

            loginResponseDTO.setResCode(Constants.FAILCODE);

            loginResponseDTO.setResMsg("Params(username) can not be empty.");

            return loginResponseDTO;
        }
        if (StringUtils.isEmpty(password)) {

            loginResponseDTO.setResCode(Constants.FAILCODE);

            loginResponseDTO.setResMsg("用户名或者密码错误");

            return loginResponseDTO;
        }

        /**下面是新加的代码,不管怎样，移除掉线程里面的subject*/
            ThreadContext.remove(ThreadContext.SUBJECT_KEY);//移除线程里面的subject
            Subject subject = SecurityUtils.getSubject();//重新获取subject

        String sessionId = "";

        try {

            PermissionUtil.setCurrentUsername(username);
            String secretpassword= MD5Util.EncoderByMd5(password).toLowerCase();

            UsernamePasswordToken token = new UsernamePasswordToken(username, secretpassword);

            subject.login(token);

            sessionId = subject.getSession().getId().toString();

            LoginBo loginBo = userService.getAuthByUsername(username);

            if(null != loginBo) {

                loginResponseDTO.setAuthorList(loginBo.getAuthList());

                loginResponseDTO.setRoleName(loginBo.getRoleName());

                loginResponseDTO.setRoleId(loginBo.getRoleId());
            }
            if(StringUtils.isEmpty(loginBo.getRoleName())){

                loginResponseDTO.setResCode(Constants.FAILCODE);

                loginResponseDTO.setResMsg("该用户名没有分配角色");

                log.info("LoginController.login ==> 用户的角色不存在，用户名为:{}",username);

                return loginResponseDTO;
            }
            loginResponseDTO.setResCode(Constants.SUCESSCODE);

            loginResponseDTO.setToken(sessionId);

            loginResponseDTO.setResMsg("登录成功.");

            log.info("LoginController.login ==> login success .token is {}",sessionId);

        } catch (IncorrectCredentialsException e) {

            log.error("LoginController.login ==> password is wrong!!!",e);

            loginResponseDTO.setResCode(Constants.FAILCODE);

            loginResponseDTO.setResMsg("用户名或者密码错误");

            return loginResponseDTO;

        } catch (LockedAccountException e) {

            log.error("LoginController.login ==> user is frozen!!!",e);

            loginResponseDTO.setResCode(Constants.FAILCODE);

            loginResponseDTO.setResMsg("用户名或者密码错误");

            return loginResponseDTO;

        } catch (AuthenticationException e) {

            log.error("LoginController.login ==> user is not exist!!!",e);

            loginResponseDTO.setResCode(Constants.FAILCODE);

            loginResponseDTO.setResMsg("用户名或者密码错误");

            return loginResponseDTO;

        } catch (Exception e) {

            log.error("LoginController.login ==> login is error!!!",e);

            loginResponseDTO.setResCode(Constants.FAILCODE);

            loginResponseDTO.setResMsg("登录错误");

            return loginResponseDTO;
        }
        return loginResponseDTO;

    }

    @RequestMapping(value = "/videoos/logout", method = RequestMethod.POST)
    public BaseResponseDTO logout(HttpServletRequest request) {

        String token = request.getHeader("token");

        Session session = redisSessionDao.doReadSession(token);

        redisSessionDao.delete(session);

        return BaseSuccessResultUtil.producessSuccess();
    }

    @RequestMapping(value = "/videoos/token/isTokenValid", method = RequestMethod.GET)
    public IsValidTokenResponseDTO isTokenValid(@RequestParam String token) {

        IsValidTokenResponseDTO isValidTokenResponseDTO = new IsValidTokenResponseDTO();

        Session session = redisSessionDao.doReadSession(token);

        if(null == session){

            isValidTokenResponseDTO.setIsValid(false);

        }else{

            isValidTokenResponseDTO.setIsValid(true);
        }
        isValidTokenResponseDTO.setResCode(Constants.SUCESSCODE);

        isValidTokenResponseDTO.setResMsg(Constants.COMMONSUCCESSMSG);

        return isValidTokenResponseDTO;
    }


}
