package com.videojj.videoservice.realm;

import com.videojj.videoservice.dao.TbUserMapper;
import com.videojj.videoservice.entity.TbUser;
import com.videojj.videoservice.entity.TbUserCriteria;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by  on 2017/12/11.
 * 自定义权限匹配和账号密码匹配  多种方式实现，也可以通过继承AuthorizingRealm这个类的方式，不过需要实现权限控制
 */
public class TvShiroRealm extends AuthorizingRealm {

    @Resource
    private TbUserMapper tbUserMapper;

    @Override
    public String getName() {
        return "tvShiroRealm";
    }

    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof UsernamePasswordToken;
    }

    /**暂时不加权限控制 */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        return authorizationInfo;
    }

    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        //获取用户的输入的账号.
        String username = (String) token.getPrincipal();

        String password = new String((char[]) token.getCredentials());

        TbUserCriteria tbUserCriteria = new TbUserCriteria();

        TbUserCriteria.Criteria userCriteria = tbUserCriteria.createCriteria();

        userCriteria.andUsernameEqualTo(username);

        userCriteria.andIsDeletedEqualTo("N");

        List<TbUser> userInfoDoList = this.tbUserMapper.selectByParam(tbUserCriteria);

        if(CollectionUtils.isEmpty(userInfoDoList)||userInfoDoList.size()>1){

            throw new UnknownAccountException("账号或者密码错误");
        }
        if(!password.equals(userInfoDoList.get(0).getPassword())){

            throw new UnknownAccountException("密码或者密码错误");
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                username,
                password, //密码
                ByteSource.Util.bytes(username+password),//salt=username+salt
                getName()  //realm name
        );
        return authenticationInfo;
    }


}
