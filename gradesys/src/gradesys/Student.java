package gradesys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.TransactionOptions;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;


public class Student {

	private static final String entityKind = "Student";
	
	private static final String namePropertyName = "email";
	
	private static final String name = "name";
	
	private static final String studentSecret = "studentSecret";
	
	private static final String pointsEarned = "pointsEarned";
	
	private static final String pointsAttempted = "pointsAttempted";
	
	private static final String currentPercent = "currentPercent";
	
	private static final String gradeEarned = "gradeEarned";

	private Entity entity = null;
	
	private static final Logger logger = Logger.getLogger(Student.class.getName());

	private Student(Entity entity) {
		this.entity = entity;
	}

	public Long getID() {
		return (Long) entity.getKey().getId();
	}
	
	public Key getKey() {
		return entity.getKey();
	}

	public String getName() {
		return (String) entity.getProperty(name);
	}

	private void setName(String uname) {
		entity.setProperty(name, uname);
	}
	
	public String getEmail() {
		return (String) entity.getProperty(namePropertyName);
	}
	
	private void setEmail(String e) {
		entity.setProperty(namePropertyName, e);
	}
	
	public String getStudentSecret() {
		return (String) entity.getProperty(studentSecret);
	}
	
	public void updateGrade(Long studentId, Long courseId) {
		List<GradableComponent> gcs = GradableComponent.getGradableComponentsByCourseId(courseId);
		int totalPoints = 0;
		int pointsRcvd = 0;
		for(GradableComponent gc : gcs) {
			totalPoints += gc.getPoints();
			Query query = new Query(Grade.entityKind);
			query.setAncestor(gc.getKey());

			Query.Filter filter = new FilterPredicate(namePropertyName,
					FilterOperator.EQUAL, studentId);
			query.setFilter(filter);
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Entity entity = datastore.prepare(query).asSingleEntity();
			if(entity != null) {
				Grade grade = new Grade(entity);
				int points = grade.getPoints().intValue();
				pointsRcvd  += points;
			}
		}
		
		try {
			Entity studentEntity = DatastoreUtil.getEntityByKey(entityKind, studentId);
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Transaction txn = datastore.beginTransaction();
			//Create student
			studentEntity.setProperty(pointsAttempted, totalPoints);
			studentEntity.setProperty(pointsEarned, pointsRcvd);
			if(totalPoints != 0) {
				double percent = pointsRcvd / totalPoints * 100;
				studentEntity.setProperty(currentPercent, percent);
			}
			
			datastore.put(studentEntity);					
			txn.commit();
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			
		}
		
	}

	public static String getNamePropertyname() {
		return namePropertyName;
	}

	public int getPointsEarned() {
		try {
			return Integer.valueOf(entity.getProperty(pointsEarned).toString());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			return 0;
		}
	}

	public int getPointsAttempted() {
		try {
			return Integer.valueOf(entity.getProperty(pointsAttempted).toString());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			return 0;
		}
	}

	public int getCurrentPercent() {
		try {
			return Integer.valueOf(entity.getProperty(currentPercent).toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return 0;
		}
	}

	public String getGradeEarned() {
		try {
			return entity.getProperty(gradeEarned).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "";
		}
	}

	public void save() {
		saveOrCreateEntity(entity);
	}

//	public static void deleteByName(String name) {
//		deleteEntityByName(name);
//	}
//	
	public static Student create(String name, String courseId, String email) throws CourseAlreadyExistsException {
		return new Student(createEntity(name, courseId, email));
	}
	
	public static String createBulk(List<List<String>> studentData, String courseId) throws Exception {
		return createEntityBulk(studentData, courseId);
	}
	
	public static Student update(String studentId, String name, String email) throws CourseAlreadyExistsException {
		return new Student(updateEntity(studentId, name, email));
	}
	
	public static void delete(Long studentId, Long courseId) throws CourseAlreadyExistsException {
		deleteEntityByName(studentId, courseId);
	}
	
	public static Student getByKey(Key key) {
		Entity entity = getEntityByKey(key);
		if (entity == null) {
			return null;
		} else {
			return new Student(entity);
		}
	}

	// Helper function that runs inside or outside a transaction.
	private static Entity getEntityByName(String name) {
		Query query = new Query(entityKind);
		Query.Filter filter = new FilterPredicate(namePropertyName, FilterOperator.EQUAL, name); 
		query.setFilter(filter);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		return datastore.prepare(query).asSingleEntity();
	}
	
	// Helper function that runs inside or outside a transaction.
	private static Entity getEntityByKey(Key key) {
		Query query = new Query(entityKind);
		Query.Filter filter = new FilterPredicate("__key__", FilterOperator.EQUAL, key); 
		query.setFilter(filter);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		return datastore.prepare(query).asSingleEntity();
	}
	
	// Helper function that runs inside or outside a transaction.
	public static List<Student> getStudentsByCourseId(Long courseId) {
		Query query = new Query(entityKind);
		List<Student> students = new ArrayList<Student>();
		try {
			query.setAncestor(DatastoreUtil.getEntityByKey(Course.entityKind, courseId).getKey());
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			return students;
		}
		/*CompositeFilter filter = CompositeFilterOperator.and(
			     FilterOperator.EQUAL.of(Auth.namePropertyName, courseId),
			     FilterOperator.EQUAL.of(Auth.permissions, AuthPermissions.STUDENT.toString())); */
		//query.setFilter(filter);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Iterator<Entity> iterator = datastore.prepare(query).asIterator();
		
		while (iterator.hasNext()) {
			Entity entity = iterator.next();
			students.add(new Student(entity));
			logger.info("Student : " + entity.getProperty("name"));
		}
		return students;
	}

	// Helper function that runs inside or outside a transaction.
	private static void saveOrCreateEntity(Entity entity) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		datastore.put(entity);		
	}

	private static void deleteEntityByName(Long id, Long courseId) {
		Entity entity;
		try {
			entity = DatastoreUtil.getEntityByKey(entityKind, Long.valueOf(id));
		} catch (Exception e) {
			return;
		}
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Transaction txn = datastore.beginTransaction();
		
		if(entity != null) {
			Entity auth = Auth.getEntityByCourseIdAndStudentId(courseId, entity.getKey());
			if (auth != null) {
				datastore.delete(auth.getKey());
			}
		}
		
		if (entity != null) {
			datastore.delete(entity.getKey());
		}
		txn.commit();
		
//		txn = datastore.beginTransaction();
//		Entity studentEntity = null;
//		try {
//			studentEntity = DatastoreUtil.getEntityByKey(Student.entityKind, id);
//		} catch (EntityNotFoundException e) {
//			// TODO Auto-generated catch block
//			txn.commit();
//		}
		
//		txn.commit();
	}
	
	public static Student getByStudentEmail(String uemail)
			throws EntityNotFoundException {		
		Query query = new Query(entityKind);
		Query.Filter filter = new FilterPredicate(namePropertyName, FilterOperator.EQUAL, uemail); 
		query.setFilter(filter);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity entity = datastore.prepare(query).asSingleEntity();
		if (entity == null) {
			return null;
		} else {
			return new Student(entity);
		}
	}
	
	private static Entity createEntity(String sname,  String courseId, String em) throws CourseAlreadyExistsException {
		
		Transaction txn = null;
		try {
			Student student = getByStudentEmail(em);
			if(student != null) {
				//Check for auth if exists just return
				//Auth auth = Auth.getByCourseIdAndStudent(Long.valueOf(courseId), student.getKey());
				//if(auth != null)
				return student.entity;
			}
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			TransactionOptions options = TransactionOptions.Builder.withXG(true);
			txn = datastore.beginTransaction(options);
			
			Entity studentEntity = null;
			if(student == null) {
				//Create student
				
				Course course = Course.getByCourseId(Long.valueOf(courseId));
				if(course == null)
					throw new CourseNotFoundException();
				studentEntity = new Entity(entityKind, course.getKey());
				studentEntity.setProperty(namePropertyName, em);
				studentEntity.setProperty(name, sname);
				studentEntity.setProperty(studentSecret, Util.GenerateRandomNumber(2));
				studentEntity.setProperty(gradeEarned, "");
				studentEntity.setProperty(pointsEarned, 0);
				studentEntity.setProperty(pointsAttempted, 0);
				studentEntity.setProperty(currentPercent, 0);
				datastore.put(studentEntity);
			} 
			//Auth.create(Long.valueOf(courseId), studentEntity.getKey(), AuthPermissions.STUDENT, false);					
			txn.commit();
			return studentEntity;
		} catch (Exception e) {
			return null;
		} finally {
			if(txn != null && txn.isActive())
				txn.rollback();
		}
	}
	
	private static String createEntityBulk(List<List<String>> studentData, String courseId) throws Exception {

		try {
			List<String> columns = studentData.remove(0);
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			
			List<String> updatedResults  = new ArrayList<String>();
			List<String> createdResults  = new ArrayList<String>();
			TransactionOptions options = TransactionOptions.Builder
					.withXG(true);
			for(List<String> record: studentData) {
				Transaction txn = null;
				try {
					txn = datastore.beginTransaction(options);
					String firstname = record.get(0);
					String lastname = record.get(1);
					String em = record.get(2);
					Student student = getByStudentEmail(em);
					Entity studentEntity = null;
					if(student != null) {
						studentEntity = student.entity;
						studentEntity.setProperty(namePropertyName, em);
						studentEntity.setProperty(name, firstname +","+lastname);
						studentEntity.setProperty(gradeEarned, "");
						studentEntity.setProperty(pointsEarned, 0);
						studentEntity.setProperty(pointsAttempted, 0);
						studentEntity.setProperty(currentPercent, 0);
						for(int i = 3; i < columns.size(); i++) {
							studentEntity.setProperty(columns.get(i),
									record.get(i));
						}
						updatedResults.add(record.toString());
						//Auth auth = Auth.getByCourseIdAndStudent(Long.valueOf(courseId), student.getKey());
						//if (auth != null) {
						//}
							continue;
					}
					else {
						// Create student
						Course course = Course.getByCourseId(Long.valueOf(courseId));
						if(course == null)
							throw new CourseNotFoundException();
						studentEntity = new Entity(entityKind, course.getKey());
						studentEntity.setProperty(namePropertyName, em);
						studentEntity.setProperty(name, firstname +","+lastname);
						studentEntity.setProperty(studentSecret,
								Util.GenerateRandomNumber(2));
						studentEntity.setProperty(gradeEarned, "");
						studentEntity.setProperty(pointsEarned, 0);
						studentEntity.setProperty(pointsAttempted, 0);
						studentEntity.setProperty(currentPercent, 0);

						for(int i = 3; i < columns.size(); i++) {
							studentEntity.setProperty(columns.get(i),
									record.get(i));
						}
						datastore.put(studentEntity);
						createdResults.add(record.toString());
					}

					Auth.create(Long.valueOf(courseId), studentEntity.getKey(), AuthPermissions.STUDENT, false);
					txn.commit();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					throw e;
				} finally {
					if (txn != null && txn.isActive())
						txn.rollback();
				}
			}
			return Util.getJsonString("Add Students Record Result", "", 
					new String[] {"updatedRecords", "createdRecords"}, updatedResults, createdResults);
		} catch (Exception e) {
			throw e;
		}
	}
	
	private static Entity updateEntity(String studentId, String studentName, String studentEmail) throws CourseAlreadyExistsException {
		
		Transaction txn = null;
		try {
			Entity studentEntity = DatastoreUtil.getEntityByKey(Student.entityKind, Long.valueOf(studentId));
			if(studentEntity == null) {
				throw new StudentNotFoundException();
			}
			Student student = new Student(studentEntity);
			if(student.getEmail().equalsIgnoreCase(studentEmail) && student.getName().equalsIgnoreCase(studentName)) {
				throw new StudentAlreadyExistsException();
			}
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			txn = datastore.beginTransaction();
			//Create student
			studentEntity.setProperty(namePropertyName, studentEmail);
			studentEntity.setProperty(name, studentName);
			datastore.put(studentEntity);					
			txn.commit();
			return studentEntity;
		} catch (Exception e) {
			return null;
		} finally {
			if(txn != null && txn.isActive())
				txn.rollback();
		}
	}

}