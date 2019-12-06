
package sampleAPIs.restserver.filters;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CsrfTokenResponseHeaderBindingFilter extends OncePerRequestFilter {

  private static final String REQUEST_ATTRIBUTE_NAME = "_csrf";
  private static final String RESPONSE_HEADER_NAME = "X-CSRF-HEADER";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

    CsrfToken token = (CsrfToken) request.getAttribute(REQUEST_ATTRIBUTE_NAME);
    if (token != null) {
      response.setHeader(RESPONSE_HEADER_NAME, token.getToken());
    }

    filterChain.doFilter(request, response);
  }
}