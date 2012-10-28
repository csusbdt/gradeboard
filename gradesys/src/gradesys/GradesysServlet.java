package gradesys;
import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class GradesysServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, New world!");
	}
}
