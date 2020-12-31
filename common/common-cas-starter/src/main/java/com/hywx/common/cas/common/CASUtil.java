package com.hywx.common.cas.common;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: sso
 * @description:
 * @author: tangjing
 * @create: 2020-07-07 17:04
 **/
public class CASUtil {
    public static String getAccountNameFromCas(HttpServletRequest request) {
        Assertion assertion = (Assertion) request.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
        if(assertion!= null){
            AttributePrincipal principal = assertion.getPrincipal();
            return principal.getName();
        }else return null;

    }
}
