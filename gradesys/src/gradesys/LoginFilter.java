package gradesys;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class LoginFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
	
	

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpReq = (HttpServletRequest) req;
		HttpServletResponse httpResp = (HttpServletResponse) resp;
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String servletPath = httpReq.getServletPath();
		if(user == null) {
			String operation = req.getParameter("op");
			if(!Util.isEmpty(operation) && !operation.toLowerCase().equals("login")) {
				String[] params = servletPath.split("/");
				if(params != null && params.length > 1) {
					String page = req.getParameter("page");
					if(Util.isEmpty(page)) {
						servletPath = "/" + params[1];
					} else {
						servletPath = "/" + params[1] + "/" + page;
					}
				}				
			}
			String loginUrl = userService.createLoginURL(servletPath);
			//browser is sending the request. not through ajax.
			if(operation == null) {
				httpResp.sendRedirect(loginUrl);
			}
			else {
				try {
					httpResp.getWriter().write(Util.getRedirectJson(loginUrl));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					httpResp.getWriter().write("{}");
				}
			}
		} else {
			String operation = req.getParameter("op");
			if(!Util.isEmpty(operation) && operation.toLowerCase().equals("login")) {
				try {					
					httpResp.getWriter().write(Util.getRedirectJson("/instructor/index.html"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					httpResp.getWriter().write("{}");
				}
				return;
			}
			
			String csrfToken = CsrfCipher.encryptUserId(user.getUserId());
			httpReq.setAttribute("csrfToken", csrfToken);
			httpReq.setAttribute("user", user);
			chain.doFilter(req, resp);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
