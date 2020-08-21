package com.example.realm;

import com.example.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
@Component
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;

    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("执行授权逻辑");
        String userName = (String) principals.getPrimaryPrincipal();
        // 获取用户的角色集合
        Set<String> roles = getRolesByUserName(userName);
        // 获取用户的权限集合
        Set<String> permissions = getPermissionsByUserName(userName);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        info.addRoles(roles);
        info.addStringPermissions(permissions);

        return info;
    }
    // 从数据库中获取权限信息
    private Set<String> getPermissionsByUserName(String userName) {
        Set<String> sets = new HashSet<>();
        sets.add("user:delete");
        sets.add("user:add");
        return sets;
    }

    // 从数据库中获取角色数据
    private Set<String> getRolesByUserName(String userName) {
        Set<String> sets = new HashSet<>();
        sets.add("admin");
        sets.add("user");
        return sets;
    }

    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行认证逻辑");
        // 从主体传来的认证信息中获得用户名
        String userName = (String) token.getPrincipal();
        // 通过用户名到数据库中获取凭证
        String passWord = getPasswordByUsername(userName);
        if (passWord == null){
            return null;
        }
        super.setName("customRealm");
        String realmName = this.getName();
        // 用户名和密码的判断由 shiro 完成
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userName,passWord,realmName);
        return info;
    }

    // 模拟从数据库中获取的数据
    private String getPasswordByUsername(String userName) {
        return userService.getPasswordByUserName(userName);
    }
}
