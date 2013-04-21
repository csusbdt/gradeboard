package gradesys;

import java.io.BufferedReader;
import java.io.StringReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.appengine.api.datastore.EntityNotFoundException;

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
	
	static String testJson = "{\"title\":\"CSE 405 Grade Status Report\",\"students\":[{\"id\":\"dv\",\"total\":210,\"rank\":1,\"percent\":105,\"scores\":[10,10,20,10,10,20,30,10,30,20,20,20],\"grade\":\"A\",\"attempted\":200},{\"id\":\"xz\",\"total\":210,\"rank\":1,\"percent\":105,\"scores\":[10,10,20,10,10,20,30,10,30,20,20,20],\"grade\":\"A\",\"attempted\":200},{\"id\":\"sj\",\"total\":170,\"rank\":3,\"percent\":106,\"scores\":[10,10,20,10,10,20,30,10,30,20,\"\",\"\"],\"grade\":\"A\",\"attempted\":160},{\"id\":\"wq\",\"total\":120,\"rank\":4,\"percent\":109,\"scores\":[10,10,20,10,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":110},{\"id\":\"we\",\"total\":110,\"rank\":5,\"percent\":100,\"scores\":[10,10,10,10,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":110},{\"id\":\"zb\",\"total\":110,\"rank\":5,\"percent\":100,\"scores\":[10,10,10,10,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":110},{\"id\":\"kt\",\"total\":108,\"rank\":7,\"percent\":98,\"scores\":[10,10,8,10,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":110},{\"id\":\"88\",\"total\":107,\"rank\":8,\"percent\":97,\"scores\":[10,10,7,10,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":110},{\"id\":\"zm\",\"total\":106,\"rank\":9,\"percent\":96,\"scores\":[10,10,7,9,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":110},{\"id\":\"gh\",\"total\":100,\"rank\":10,\"percent\":100,\"scores\":[10,10,10,10,10,20,30,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":100},{\"id\":\"zx\",\"total\":100,\"rank\":10,\"percent\":91,\"scores\":[10,10,0,10,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A-\",\"attempted\":110},{\"id\":\"df\",\"total\":98,\"rank\":12,\"percent\":98,\"scores\":[10,8,10,10,10,20,30,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":100},{\"id\":\"ak\",\"total\":98,\"rank\":12,\"percent\":98,\"scores\":[10,8,10,10,10,20,30,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":100},{\"id\":\"dh\",\"total\":97,\"rank\":14,\"percent\":97,\"scores\":[10,10,9,10,10,20,28,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":100},{\"id\":\"vb\",\"total\":95,\"rank\":15,\"percent\":95,\"scores\":[10,10,10,9,9,19,28,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":100},{\"id\":\"qw\",\"total\":81,\"rank\":16,\"percent\":74,\"scores\":[10,10,12,9,10,20,0,10,\"\",\"\",\"\",\"\"],\"grade\":\"C\",\"attempted\":110},{\"id\":\"dg\",\"total\":70,\"rank\":17,\"percent\":70,\"scores\":[10,10,10,10,10,20,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"C-\",\"attempted\":100},{\"id\":\"cc\",\"total\":65,\"rank\":18,\"percent\":65,\"scores\":[10,8,0,9,10,0,28,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"D\",\"attempted\":100},{\"id\":\"zo\",\"total\":45,\"rank\":19,\"percent\":45,\"scores\":[10,8,9,9,9,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"rt\",\"total\":44,\"rank\":20,\"percent\":44,\"scores\":[10,7,9,9,9,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"rx\",\"total\":30,\"rank\":21,\"percent\":38,\"scores\":[10,\"\",\"\",10,10,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":80},{\"id\":\"xc\",\"total\":17,\"rank\":22,\"percent\":17,\"scores\":[9,8,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"tj\",\"total\":14,\"rank\":23,\"percent\":14,\"scores\":[10,4,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"kl\",\"total\":10,\"rank\":24,\"percent\":10,\"scores\":[10,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"ds\",\"total\":10,\"rank\":24,\"percent\":10,\"scores\":[10,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"op\",\"total\":10,\"rank\":24,\"percent\":10,\"scores\":[10,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"hj\",\"total\":8,\"rank\":27,\"percent\":8,\"scores\":[8,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"45\",\"total\":5,\"rank\":28,\"percent\":5,\"scores\":[5,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"yu\",\"total\":0,\"rank\":29,\"percent\":0,\"scores\":[0,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"vm\",\"total\":0,\"rank\":29,\"percent\":0,\"scores\":[0,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100}],\"objectives\":[\"java\",\"http\",\"thread\",\"gae\",\"bstrap\",\"ajax\",\"ds\",\"style\",\"number\",\"local\",\"secure\",\"apis\"],\"points\":[10,10,10,10,10,20,30,10,30,20,20,20]}";	

	static public String getGradesheetJson(long courseId) throws Exception {
		Course course;
		try {
			course = Course.getByCourseId(courseId);
		} catch (EntityNotFoundException e) {
			throw new CourseNotFoundException();
		}
		JSONObject courseJson = new JSONObject();
		courseJson.put("title", course.getName());
		
		JSONArray studentsJson = new JSONArray();
		List<GradableComponent> gcs = GradableComponent.getGradableComponentsByCourseId(courseId);
		List<Student> students = Student.getStudentsByCourseId(course.getID());
		Map<Student, String> mapScores = Grade.getStudentScores(courseId, students, gcs);
		for(int i = 0; i < students.size(); i++) {
			Student student = students.get(i);
			JSONObject studentJson = new JSONObject();
			String sid = student.getStudentSecret();
			studentJson.put("id", sid);
			String scores = mapScores.get(student);
			String[] strArray = scores.split(",");
			JSONArray scoresArray = new JSONArray();
			for(String str: strArray) {
				scoresArray.put(Integer.valueOf(str));
			}
			studentJson.put("scores", scoresArray);
			studentJson.put("grade", student.getGradeEarned());
			studentJson.put("attempted", student.getPointsAttempted());
			studentJson.put("percent", student.getCurrentPercent());
			studentsJson.put(studentJson);
		}
		JSONArray objNameArray = new JSONArray();
		JSONArray objPointsArray = new JSONArray();
		for(int i = 0; i < gcs.size(); i++) {
			GradableComponent gc = gcs.get(i);
			objNameArray.put(gc.getName());
			objPointsArray.put(gc.getPoints());
		}
		courseJson.put("objectives", objNameArray);
		courseJson.put("points", objPointsArray);
		courseJson.put("students", studentsJson);
		System.out.println("Student Json: " + courseJson.toString());
		return courseJson.toString();
		//return courseJson.toString();
		
//		JSONArray objectives = new JSONArray();
//		for (int i = 0; i < 10; ++i) {				
//			objectives.put("objective");  // need to get course objectives
//		}
//		
//		JSONArray points = new JSONArray();
//		for (int i = 0; i < 10; ++i) {				
//			points.put(110);  // need to get points for objectives
//		}
//
//		JSONArray students = new JSONArray();
//		List<Student> studentList = Student.getStudentsByCourseId(courseId);
//		Iterator<Student> it = studentList.iterator();
//		while (it.hasNext()) {
//			Student student = it.next();
//			JSONObject studentObject = new JSONObject();
//			studentObject.put("id",  "" + student.getID());
//			int total = 0;
//			int rank = 0;
//			int percent = 0;
//			String grade = "A";
//			int attempted = 250;
//			JSONArray scores = new JSONArray();
//			for (int i = 0; i < 10; ++i) {				
//				scores.put(new JSONObject(78));  // need to get scores for student
//			}
//			studentObject.put("total",  total);
//			studentObject.put("rank",  rank);
//			studentObject.put("grade", grade);
//			studentObject.put("percent", percent);
//			studentObject.put("attempted", attempted);
//			studentObject.put("scores", scores);
//			students.put(studentObject);
//		}
//
//		JSONObject json = new JSONObject();
//		
//		json.put("title", "CSE 405 Grade Status Report");
//		json.put("students", students);
//		json.put("objectives", objectives);
//		json.put("points", points);*/
//
	
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