package gradesys;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class InstructorControllerServlet extends HttpServlet {	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		try {
			
			String operation = req.getParameter("op");
			if(!Util.isEmpty(operation)) {
				
				if(operation.equalsIgnoreCase("courseadd")) {
					String courseName = req.getParameter("courseName");
					//Course.add(courseName);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
