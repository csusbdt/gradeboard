package gradesys;

import java.util.List;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class Util {

	public static boolean isEmpty(String text) {
		if(text == null || text.length()  == 0) {
			return true;
<<<<<<< HEAD
		}
		return false;
	}
	
	public static String getCoursesJson(List<Course> courses) throws JSONException {
		if(courses.size() == 0)
			return "{}";
		
		JSONArray jsonArray = new JSONArray();
		for(int i = 0; i < courses.size(); i++) {
			Course course = courses.get(i);
			jsonArray.put(course.getName());
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("courses", jsonArray);
		return jsonObject.toString();
=======
		}
		return false;
>>>>>>> b8fdd260c2af41e4792b7942344ba051b1f0a7e9
	}
}
