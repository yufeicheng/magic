package com.magic.interview.service.parameter_annotation;

import com.magic.dao.mapper.UserMapper;
import com.magic.dao.model.User;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Cheng Yufei
 * @create 2020-01-14 10:26
 **/
public class CurrentUserHandler implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(CurrentUser.class) && User.class.isAssignableFrom(methodParameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String sessionId = request.getHeader("sessionId");

        /*WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
        UserMapper userMapper = context.getBean(UserMapper.class);*/

        UserMapper userMapper = MyApplicationContext.getBean(UserMapper.class);
        User user = userMapper.selectById(Integer.valueOf(sessionId));
        //User user = userMapper.selectByPrimaryKey(Integer.valueOf(sessionId));
        return user;
    }
}
