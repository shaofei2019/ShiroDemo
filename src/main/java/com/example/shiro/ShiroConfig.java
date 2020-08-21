package com.example.shiro;

import com.example.cache.RedisCacheManager;
import com.example.realm.CustomRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Autowired
    RedisCacheManager redisCacheManager;
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultSecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 添加Shiro内置过滤器
        /**
         * Shiro 内置过滤器，可以实现权限相关的拦截器
         * 常用的过滤规则：
         *      anon：无需认证即可访问
         *      authc：必须认证才可访问
         *      user：如果使用remenberMe的功能可以直接访问
         *      perms：该资源必须得到资源权限才可以访问
         *      roles：该资源必须得到角色权限才可以访问
         */
        Map<String,String> filterMap = new LinkedHashMap<String,String>();

        filterMap.put("/getSession","authc");
        filterMap.put("/test","user");
        filterMap.put("/login","anon");
        filterMap.put("/testA","user,roles[admin1]");

        // 跳转登录页面
        shiroFilterFactoryBean.setLoginUrl("/toLogin");
        // 设置未授权页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/noauth");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

        return shiroFilterFactoryBean;
    }
    @Bean("realmWithMatcher")
    public CustomRealm shiroRealm() {
        // 构建 Realm
        CustomRealm realm = new CustomRealm();
        // 创建加密器
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        // 设置加密算法
        matcher.setHashAlgorithmName("md5");
        // 将加密器注入 realm 中
        realm.setCredentialsMatcher(matcher);

        realm.setAuthenticationCachingEnabled(true);// 开始身份验证缓存
        realm.setAuthenticationCacheName("authenticationCache");
        realm.setAuthorizationCachingEnabled(true);
        realm.setAuthorizationCacheName("authorizationCache");

        return realm;
    }
    @Bean("securityManager")
    public DefaultWebSecurityManager  securityManager(@Qualifier("realmWithMatcher") CustomRealm realm){
        // 创建SecurityManager
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();


        // 创建 Session 管理器 DefaultWebSessionManager 该管理器会自己维护会话，不依赖于servlet
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        // 设置session过期时间，10s
        sessionManager.setGlobalSessionTimeout(10000l);
        // 注入session管理器
        manager.setSessionManager(sessionManager);


        // 创建记住我管理器
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        // 设置记住我的时间，记住我的状态通过cookie保存
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        // 秒
        simpleCookie.setMaxAge(60);
        rememberMeManager.setCookie(simpleCookie);
        rememberMeManager.setCipherKey(Base64.decode("6ZmI6I2j5Y+R5aSn5ZOlAA=="));
        // 记住我管理器 注入到 manager 中
        manager.setRememberMeManager(rememberMeManager);


        // 创建缓存管理器, autowired注入

        // 将缓存管理器注入到 manager中
        manager.setCacheManager(redisCacheManager);

        // 注入 realm
        manager.setRealm(realm);

        return manager;
    }

    /**
     *  开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * 开启aop注解支持
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}