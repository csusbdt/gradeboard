package gradesys;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.google.appengine.api.users.UserServiceFactory;


@SuppressWarnings("serial")
public class LogoutServlet extends HttpServlet {	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println(req.getParameter("page"));
		System.out.println(req.getServletPath());
		String redirectUrl = UserServiceFactory.getUserService().createLogoutURL("/");
		try {
			resp.getWriter().write(Util.getRedirectJson(redirectUrl));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			resp.getWriter().write("{}");
		}
	}
}
