package gradesys;

import java.util.List;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class Util {

	public static boolean isEmpty(String text) {
		if(text == null || text.length()  == 0) {
			return true;
		}
		return false;
	}
	
	public static String getCoursesJson(List<Course> courses) throws JSONException {
		if(courses.size() == 0)
			return "{}";
		
		JSONArray jsonArray = new JSONArray();
		for(int i = 0; i < courses.size(); i++) {
			JSONObject jsonObject = new JSONObject();
			Course course = courses.get(i);
			jsonObject.put("name", course.getName());
			jsonObject.put("id", course.getID());
			jsonArray.put(jsonObject);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("courses", jsonArray);
		return jsonObject.toString();
	}
	
	public static String getCourseJson(String course) throws JSONException {
		if(course == null || course.length() == 0)
			return "{}";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("course", course);
		return jsonObject.toString();
	}
	
	public static String getRedirectJson(String url) throws JSONException {
		if(url == null || url.length() == 0)
			return "{}";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("redirectUrl", url);
		return jsonObject.toString();
	}
}
