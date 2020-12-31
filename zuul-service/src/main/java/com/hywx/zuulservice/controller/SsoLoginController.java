package com.hywx.zuulservice.controller;
//
//import com.hywx.zuulservice.config.WebSecurityConfig;
//import com.hywx.zuulservice.dao.User;
import com.hywx.zuulservice.service.UserService;
//import org.jasig.cas.client.util.AbstractCasFilter;
//import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;

/**
 * @program: sat-cloud
 * @description:
 * @author: tangjing
 * @create: 2020-06-23 10:41
 **/
@RestController
@RequestMapping("/sso")
public class SsoLoginController {

    @Autowired
    private UserService userService;

    private static String queryString = "";

    static final String SAVED_REQUEST = "SPRING_SECURITY_SAVED_REQUEST";

    @GetMapping("/user")
    public Principal currentUs(Principal principal) {
        return principal;
    }

    @GetMapping("/callback")
    public Principal callback(Principal principal) {
        return principal;
    }

    @RequestMapping(value = "/caslogin", method = RequestMethod.GET)
    public void caslogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        HttpSession session = request.getSession();
//        Assertion assertion = (Assertion) session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
//        if (assertion != null) {
//            //获取登录用户名
//            String username = assertion.getPrincipal().getName();
//            System.out.println("user ---------> " + username);
//            User temp = userService.findByName(username);
//            System.out.println("TEMP user ---------> " + (temp.getUsername()));
//            if (temp != null) {
//                session.setAttribute(WebSecurityConfig.SESSION_LOGIN, temp);
//
//                String jsessionid = session.getId();
//
//                System.out.println("jsessionid ------> " + jsessionid);
//
//                // 使用url传递参数,跳转到前端
//                response.sendRedirect("http://front.anumbrella.net:8000/home?jsessionid=" + jsessionid);
//
//                // 使用nginx代理,跳转到前端
//               // response.sendRedirect("http://nginx.anumbrella.net:81/home");
//            }
//        }else{
//            this.queryString=request.getQueryString();
////            HttpSessionRequestCache requestCache= new HttpSessionRequestCache();
////            if(this.queryString.contains("redirect_uri")){
////                requestCache.saveRequest(request,null);
////            }
//        }
    }
}
