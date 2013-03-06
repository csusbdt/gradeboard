package gradesys;

import java.io.BufferedReader;
import java.io.StringReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
			jsonObject.put("id", String.valueOf(student.getID()));
			jsonArray.put(jsonObject);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("students", jsonArray);
		return jsonObject.toString();
	}
	
	public static String getGradableComponentJson(List<GradableComponent> gcs) throws JSONException {
		if(gcs.size() == 0)
			return "{}";
		
		JSONArray jsonArray = new JSONArray();
		for(int i = 0; i < gcs.size(); i++) {
			JSONObject jsonObject = new JSONObject();
			GradableComponent gc = gcs.get(i);
			jsonObject.put("name", gc.getName());
			jsonObject.put("points", String.valueOf(gc.getPoints()));
			jsonObject.put("id", String.valueOf(gc.getID()));
			String dline = DateFormat.getInstance().format((Date)gc.getDeadline());
			jsonObject.put("deadline", dline);
			jsonArray.put(jsonObject);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("gcs", jsonArray);
		return jsonObject.toString();
	}
	
	public static String getGradesJson(List<GradableComponent> gcs) throws JSONException {
		if(gcs.size() == 0)
			return "{}";
		
		JSONArray jsonArray = new JSONArray();
		for(int i = 0; i < gcs.size(); i++) {
			JSONObject jsonObject = new JSONObject();
			GradableComponent gc = gcs.get(i);
			jsonObject.put("name", gc.getName());
			jsonObject.put("id", String.valueOf(gc.getID()));
			jsonArray.put(jsonObject);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("grades", jsonArray);
		return jsonObject.toString();
	}
	
	public static String getGradesStudentJson(Map<Student, Grade> mapgs, String gcId, String courseId) throws JSONException {
		if(mapgs.size() == 0)
			return "{}";
		
		JSONArray jsonArray = new JSONArray();
		
		for(Student student: mapgs.keySet()) {
			Grade grade = mapgs.get(student);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name", student.getName());
			jsonObject.put("points", String.valueOf(grade.getPoints()));
			jsonObject.put("gcId", gcId);
			jsonObject.put("stuId", student.getID());
			jsonArray.put(jsonObject);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("grades", jsonArray);
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
	
	public static String getJsonErrorMsg(String msg) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("error", msg);
		return jsonObject.toString();
	}
	
	public static String getJsonString(String heading, String delimeter,  String[] msgHdngs, List<String>...msgs) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		
		JSONObject jsonObj = new JSONObject();
		for(int i = 0; i < msgs.length; i++) {
			List<String> msg = msgs[i];
			JSONArray jsonArray = new JSONArray();
			for(int j = 0; j < msg.size(); j++) {
				jsonArray.put(msg.get(j));
			}
			if(jsonArray.length() != 0)
				jsonObj.put(msgHdngs[i], jsonArray);
		}
		jsonObject.put(heading, jsonObj);
		return jsonObject.toString();
	}
	
	public static String courseNotFoundError() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("error", "Course not found in the store. Either course is deleted or not created.");
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
	
	public static List<List<String>> parseCSVformat(String csvdata) throws Exception {
		
		if(Util.isEmpty(csvdata)) {
			return null;
		}
		
		StringReader strReader = null;
		List<List<String>> csvList = new ArrayList<List<String>>();
		try {
			strReader = new StringReader(csvdata);
			BufferedReader reader = new BufferedReader(strReader);
			String line = null;
			while((line = reader.readLine()) != null) {
				String columns[] = line.trim().split(",");
				List <String> columnList = new ArrayList<String>();
				for(String clmn : columns) {
					columnList.add(clmn.trim());
				}
				csvList.add(columnList);
			}
			List<String> columns = csvList.get(0);
			if(columns.size() < 3) {
				throw new Exception("Found column size less than 3. Each student record should have LastName, FirstName and Email address.");
			}
			if(csvList.size() == 1) {
				throw new Exception("No records found. Each record should be seperated by new line character.");
			}
			int noOfcolumns = columns.size();
			for(int i = 1; i < csvList.size(); i++) {
				List<String> record = csvList.get(i);
				if(record.size() <  3) {
					throw new Exception("Some records are missing LastName, FirstName and Email address");
				}
			}
			return csvList;
		} catch(Exception ex) {
			throw ex;
		} finally {
			if(strReader != null) {
				strReader.close();
			}
		}
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