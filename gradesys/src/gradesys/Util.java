package gradesys;

import java.util.List;
import java.util.Random;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
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
	
	public static String getStudentsJson(List<Student> students) throws JSONException {
		if(students.size() == 0)
			return "{}";
		
		JSONArray jsonArray = new JSONArray();
		for(int i = 0; i < students.size(); i++) {
			JSONObject jsonObject = new JSONObject();
			Student student = students.get(i);
			jsonObject.put("name", student.getName());
			jsonObject.put("email", student.getEmail());
			jsonArray.put(jsonObject);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("students", jsonArray);
		return jsonObject.toString();
	}
	
	public static String getInstructorsJson(List<Instructor> instructors) throws JSONException {
		if(instructors.size() == 0)
			return "{}";
		
		JSONArray jsonArray = new JSONArray();
		for(int i = 0; i < instructors.size(); i++) {
			JSONObject jsonObject = new JSONObject();
			Instructor instructor = instructors.get(i);
			jsonObject.put("name", instructor.getName());
			jsonObject.put("email", instructor.getEmail());
			jsonArray.put(jsonObject);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("instructors", jsonArray);
		return jsonObject.toString();
	}
	
	public static String getCourseJson(String course) throws JSONException {
		if(course == null || course.length() == 0)
			return "{}";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("course", course);
		return jsonObject.toString();
	}
	
	
	public static String getCourseJson(String course, String id) throws JSONException {
		if(course == null || course.length() == 0 || Util.isEmpty(id))
			return "{}";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("courseName", course);
		jsonObject.put("courseId", id);
		return jsonObject.toString();
	}
	
	public static String getMissingParameter(String msg) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("errorMsg", msg);
		return jsonObject.toString();
	}
	
	public static String courseNotFoundError() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("errorMsg", "Course not found in the store. Either course is deleted or not created.");
		return jsonObject.toString();
	}
	
	
	public static String getRedirectJson(String url) throws JSONException {
		if(url == null || url.length() == 0)
			return "{}";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("redirectUrl", url);
		return jsonObject.toString();
	}
	
	public static String GenerateRandomNumber(int charLength) {  
	    String val = "";      

//	    // char (1), random A-Z
//	    int ranChar = 65 + (new Random()).nextInt(90-65);
//	    char ch = (char)ranChar;        
//	    val += ch;      
//
//	    // numbers (6), random 0-9
//	    Random r = new Random();
//	    int n = 100 + (int)r.nextFloat() * 900;
//	    val += String.valueOf(n);
//
//	    val += "-";
	    // char or numbers (5), random 0-9 A-Z
	    for(int i = 0; i<6;){
	        int ranAny = 48 + (new Random()).nextInt(90-65);

	        if(!(57 < ranAny && ranAny<= 65)){
	        char c = (char)ranAny;      
	        val += c;
	        i++;
	        }

	    }

	    return val;
    }
	
	public static void main(String str[]) {
		try {
			for(int i =0; i < 20; i++)
				System.out.println(Util.GenerateRandomNumber(2));
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
}