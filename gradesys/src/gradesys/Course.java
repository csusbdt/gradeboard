package gradesys;

import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonPrimitive;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonPrimitive;

public class Course {

	public static final String entityKind = "course";
	private static final String namePropertyName = "name";

	private Entity entity = null;

	private Course(Entity entity) {
		this.entity = entity;
	}

	public Long getID() {
		return (Long) entity.getKey().getId();
	}

	public String getName() {
		return (String) entity.getProperty(namePropertyName);
	}

	private void setName(String name) {
		entity.setProperty(namePropertyName, name);
	}

	public void save() {
		saveOrCreateEntity(entity);
	}

	public static void deleteByName(String name) {
		deleteEntityByName(name);
	}

	public static Course create(String name, Instructor instructor)
			throws CourseAlreadyExistsException {
		return new Course(createEntity(name, instructor));
	}

	public static Course save(String id, String newCourseName) throws Exception {
		Course course = getByCourseId(Long.valueOf(id));
		if (course == null) {
			throw new CourseNotFoundException();
		} else {
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Transaction txn = datastore.beginTransaction();
			course.setName(newCourseName);
			datastore.put(course.entity);
			txn.commit();
			return course;
		}
	}

	public static Course getByName(String name) {
		Entity entity = getEntityByName(name);
		if (entity == null) {
			return null;
		} else {
			return new Course(entity);
		}
	}

	public static Course getByCourseId(Long courseId)
			throws EntityNotFoundException {
		if (courseId == 0) 
			return null;
		Entity entity = DatastoreUtil.getEntityByKey(Course.entityKind, courseId);
		if (entity == null) {
			return null;
		} else {
			return new Course(entity);
		}
	}
	

	// Helper function that runs inside or outside a transaction.
	private static Entity getEntityByName(String name) {
		Query query = new Query(entityKind);
		Query.Filter filter = new FilterPredicate(namePropertyName,
				FilterOperator.EQUAL, name);
		query.setFilter(filter);
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		return datastore.prepare(query).asSingleEntity();
	}

	// Helper function that runs inside or outside a transaction.
	private static void saveOrCreateEntity(Entity entity) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		datastore.put(entity);
	}

	private static void deleteEntityByName(String name) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Entity entity = getEntityByName(name);
		if (entity != null) {
			datastore.delete(entity.getKey());
		}
		txn.commit();
	}
	
	public static void deleteCourse(Long courseId) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Entity course = null;
		try {
			course = DatastoreUtil.getEntityByKey(entityKind, courseId);
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			return;
		}
		if(course == null)
			return;
		Transaction txn = datastore.beginTransaction();
			datastore.delete(course.getKey());
		txn.commit();
	}

	private static Entity createEntity(String name, Instructor instructor)
			throws CourseAlreadyExistsException {

		Transaction txn = null;
		try {
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			TransactionOptions options = TransactionOptions.Builder.withXG(true);
			txn = datastore.beginTransaction(options);
			Entity entity = getEntityByName(name);
			if (entity != null) {
				txn.commit();
				throw new CourseAlreadyExistsException();
			}
			entity = new Entity(entityKind);
			entity.setProperty(namePropertyName, name);
			datastore.put(entity);
			Long id = (Long) entity.getKey().getId();
			Auth.create(id, instructor.getKey(), AuthPermissions.SUPER_USER, false);		
			txn.commit();
			return entity;
		} catch (Exception e) {
			if(e instanceof CourseAlreadyExistsException) {
				throw (CourseAlreadyExistsException)e;
			}
			return null;
		} finally {
			if(txn != null && txn.isActive())
				txn.rollback();
		}
	}
		
	static String testJson = "{\"title\":\"CSE 405 Grade Status Report\",\"students\":[{\"id\":\"dv\",\"total\":210,\"rank\":1,\"percent\":105,\"scores\":[10,10,20,10,10,20,30,10,30,20,20,20],\"grade\":\"A\",\"attempted\":200},{\"id\":\"xz\",\"total\":210,\"rank\":1,\"percent\":105,\"scores\":[10,10,20,10,10,20,30,10,30,20,20,20],\"grade\":\"A\",\"attempted\":200},{\"id\":\"sj\",\"total\":170,\"rank\":3,\"percent\":106,\"scores\":[10,10,20,10,10,20,30,10,30,20,\"\",\"\"],\"grade\":\"A\",\"attempted\":160},{\"id\":\"wq\",\"total\":120,\"rank\":4,\"percent\":109,\"scores\":[10,10,20,10,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":110},{\"id\":\"we\",\"total\":110,\"rank\":5,\"percent\":100,\"scores\":[10,10,10,10,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":110},{\"id\":\"zb\",\"total\":110,\"rank\":5,\"percent\":100,\"scores\":[10,10,10,10,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":110},{\"id\":\"kt\",\"total\":108,\"rank\":7,\"percent\":98,\"scores\":[10,10,8,10,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":110},{\"id\":\"88\",\"total\":107,\"rank\":8,\"percent\":97,\"scores\":[10,10,7,10,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":110},{\"id\":\"zm\",\"total\":106,\"rank\":9,\"percent\":96,\"scores\":[10,10,7,9,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":110},{\"id\":\"gh\",\"total\":100,\"rank\":10,\"percent\":100,\"scores\":[10,10,10,10,10,20,30,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":100},{\"id\":\"zx\",\"total\":100,\"rank\":10,\"percent\":91,\"scores\":[10,10,0,10,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A-\",\"attempted\":110},{\"id\":\"df\",\"total\":98,\"rank\":12,\"percent\":98,\"scores\":[10,8,10,10,10,20,30,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":100},{\"id\":\"ak\",\"total\":98,\"rank\":12,\"percent\":98,\"scores\":[10,8,10,10,10,20,30,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":100},{\"id\":\"dh\",\"total\":97,\"rank\":14,\"percent\":97,\"scores\":[10,10,9,10,10,20,28,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":100},{\"id\":\"vb\",\"total\":95,\"rank\":15,\"percent\":95,\"scores\":[10,10,10,9,9,19,28,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":100},{\"id\":\"qw\",\"total\":81,\"rank\":16,\"percent\":74,\"scores\":[10,10,12,9,10,20,0,10,\"\",\"\",\"\",\"\"],\"grade\":\"C\",\"attempted\":110},{\"id\":\"dg\",\"total\":70,\"rank\":17,\"percent\":70,\"scores\":[10,10,10,10,10,20,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"C-\",\"attempted\":100},{\"id\":\"cc\",\"total\":65,\"rank\":18,\"percent\":65,\"scores\":[10,8,0,9,10,0,28,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"D\",\"attempted\":100},{\"id\":\"zo\",\"total\":45,\"rank\":19,\"percent\":45,\"scores\":[10,8,9,9,9,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"rt\",\"total\":44,\"rank\":20,\"percent\":44,\"scores\":[10,7,9,9,9,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"rx\",\"total\":30,\"rank\":21,\"percent\":38,\"scores\":[10,\"\",\"\",10,10,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":80},{\"id\":\"xc\",\"total\":17,\"rank\":22,\"percent\":17,\"scores\":[9,8,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"tj\",\"total\":14,\"rank\":23,\"percent\":14,\"scores\":[10,4,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"kl\",\"total\":10,\"rank\":24,\"percent\":10,\"scores\":[10,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"ds\",\"total\":10,\"rank\":24,\"percent\":10,\"scores\":[10,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"op\",\"total\":10,\"rank\":24,\"percent\":10,\"scores\":[10,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"hj\",\"total\":8,\"rank\":27,\"percent\":8,\"scores\":[8,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"45\",\"total\":5,\"rank\":28,\"percent\":5,\"scores\":[5,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"yu\",\"total\":0,\"rank\":29,\"percent\":0,\"scores\":[0,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"vm\",\"total\":0,\"rank\":29,\"percent\":0,\"scores\":[0,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100}],\"objectives\":[\"java\",\"http\",\"thread\",\"gae\",\"bstrap\",\"ajax\",\"ds\",\"style\",\"number\",\"local\",\"secure\",\"apis\"],\"points\":[10,10,10,10,10,20,30,10,30,20,20,20]}";	

	static public String getGradesheetJson(long courseId) throws CourseNotFoundException {
		/*Course course;
		try {
			course = getByCourseId(courseId);
		} catch (EntityNotFoundException e) {
			throw new CourseNotFoundException();
		}
		
		JsonArray objectives = new JsonArray();
		for (int i = 0; i < 10; ++i) {				
			objectives.add(new JsonPrimitive("objective"));  // need to get course objectives
		}
		
		JsonArray points = new JsonArray();
		for (int i = 0; i < 10; ++i) {				
			points.add(new JsonPrimitive(110));  // need to get points for objectives
		}

		JsonArray students = new JsonArray();
		List<Student> studentList = Student.getStudentsByCourseId(courseId);
		Iterator<Student> it = studentList.iterator();
		while (it.hasNext()) {
			Student student = it.next();
			JsonObject studentObject = new JsonObject();
			studentObject.addProperty("id",  "" + student.getID());
			int total = 0;
			int rank = 0;
			int percent = 0;
			String grade = "A";
			int attempted = 250;
			JsonArray scores = new JsonArray();
			for (int i = 0; i < 10; ++i) {				
				scores.add(new JsonPrimitive(78));  // need to get scores for student
			}
			studentObject.addProperty("total",  total);
			studentObject.addProperty("rank",  rank);
			studentObject.addProperty("grade", grade);
			studentObject.addProperty("percent", percent);
			studentObject.addProperty("attempted", attempted);
			studentObject.add("scores", scores);
			students.add(studentObject);
		}

		JsonObject json = new JsonObject();
		
		json.add("title", new JsonPrimitive("CSE 405 Grade Status Report"));
		json.add("students", students);
		json.add("objectives", objectives);
		json.add("points", points);*/

		return null;
	}
}
