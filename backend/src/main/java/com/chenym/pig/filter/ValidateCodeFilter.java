package com.chenym.pig.filter;

import cn.hutool.core.util.StrUtil;
import com.chenym.pig.utils.ImageCodeGenerator;
import com.chenym.pig.utils.R;
import com.chenym.pig.utils.ValidateCodeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class ValidateCodeFilter extends OncePerRequestFilter {
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (StrUtil.equals("/api/auth/login", httpServletRequest.getRequestURI())
                && StrUtil.equalsIgnoreCase(httpServletRequest.getMethod(), "post")) {
            try {
                // 1. 进行验证码的校验
                validate(httpServletRequest);
            } catch (ValidateCodeException e) {
                // 2. 如果校验不通过，调用SpringSecurity的校验失败处理器
                httpServletResponse.setCharacterEncoding("UTF-8");
                httpServletResponse.setContentType("application/json;charset=UTF8");
                PrintWriter writer = httpServletResponse.getWriter();
                R r = new R<>(e);
                writer.write(new ObjectMapper().writeValueAsString(r));
                writer.close();
                return;
            }
        }
        // 3. 校验通过，就放行
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * 检查code
     *
     * @param request
     */
    private void validate(HttpServletRequest request) {
        String code = request.getParameter("code");
        if (StrUtil.isBlank(code)) {
            throw new ValidateCodeException("验证码不能为空");
        }
        String existCode = (String)sessionStrategy.getAttribute(new ServletWebRequest(request), ImageCodeGenerator.SESSION_KEY);
        if (!StrUtil.equalsIgnoreCase(code, existCode)) {
            throw new ValidateCodeException("验证码不正确");
        }
    }
}
