package project.shop.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import project.shop.web.session.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    HttpSession session = request.getSession();
    if (session.getAttribute(SessionConst.LOGIN_MEMBER) == null || session == null) {
      log.info("미인증 사용자");
      response.sendRedirect("/login?redirectURL=" + request.getRequestURI());
      return false;
    }

    log.info("인증 사용자");
    return true;
  }



}
