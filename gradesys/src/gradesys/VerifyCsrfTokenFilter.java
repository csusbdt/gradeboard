package gradesys;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VerifyCsrfTokenFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		 HttpServletRequest httpReq = (HttpServletRequest) req;
         HttpServletResponse httpResp = (HttpServletResponse) resp;

         String tokenFromCookie = null;
         Cookie cookies[] = httpReq.getCookies();
         for (int i = 0; i < cookies.length; ++i)
         {
                 if (cookies[i].getName().equals("csrf"))
                 {
                         tokenFromCookie = cookies[i].getValue();
                 }
         }
         if (tokenFromCookie == null)
         {
                 Logger.getLogger("app").warning("Request with no csrf cookie.");
                 httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                 return;
         }

         String csrfToken = httpReq.getParameter("csrf");
         if (csrfToken == null)
         {
                 httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                 return;
         }

         if (csrfToken.equals(tokenFromCookie))
         {
                 chain.doFilter(req,  resp);
         }
         else
         {
                 httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
         }
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}
	
	@Override
	public void destroy() {
	}
}
