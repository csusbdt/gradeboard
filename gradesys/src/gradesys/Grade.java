package gradesys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;

public class Grade {

	public static final String entityKind = "grade";
	public static final String namePropertyName = "studentId";
	public static final String points = "points";
	public static final List<String> students = null;
	
	private Entity entity = null;

	private Grade(Entity entity) {
		this.entity = entity;
	}

	public Long getID() {
		return (Long) entity.getKey().getId();
	}
	
	public static Map<Student, Grade> getStudents( Long gcId, Long courseId) {
		
		List<Student> students = Student.getStudentsByCourseId(courseId);
		if(students == null || students.size() == 0) {
			return null;
		}
		
		Map<Student, Grade> mapGrades = new HashMap<Student, Grade> ();
		for(Student student : students) {
			Long id = student.getID();
			Query query = new Query(entityKind);
			
			Query.Filter filter = new FilterPredicate(namePropertyName,
					FilterOperator.EQUAL, id); 
			query.setFilter(filter);
					
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();		
			Entity entity = datastore.prepare(query).asSingleEntity();
			if(entity == null) {
				try {
					entity = createEntity(courseId, gcId, id, true);
				} catch (EntityNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			Grade grade = new Grade(entity);
			mapGrades.put(student, grade);
		}
		return mapGrades;
	}
	
	public Key getGradableComponentKey() {
		return entity.getParent();
	}

	public void save() {
		saveOrCreateEntity(entity);
	}

	public static void deleteByName(Long courseId) {
		deleteEntityByName(courseId);
	}

//	public static Grade create( Key gcKey, Long courseId, Long studentId, boolean requiresTxn)
//			throws GradeAlreadyExistsException, EntityNotFoundException {
//		return new Grade(createEntity(courseId, gcKey, studentId, requiresTxn));
//	}

	public static Grade getByCourseId(Long courseId) {
		Entity entity = getEntityByCourseId(courseId);
		if (entity == null) {
			return null;
		} else {
			return new Grade(entity);
		}
	}
	
	public Long getPoints() {
		return (Long)entity.getProperty(points);
	}
	
	
	public static Grade getByCourseIdAndStudentId(Long courseId, Key studentKey) {
		Entity entity = getEntityByCourseIdAndStudentId(courseId, studentKey);
		if (entity == null) {
			return null;
		} else {
			return new Grade(entity);
		}
	}


	// Helper function that runs inside or outside a transaction.
	private static Entity getEntityByCourseId(Long courseId) {
		Query query = new Query(entityKind);
		Query.Filter filter = new FilterPredicate(namePropertyName,
				FilterOperator.EQUAL, courseId);
		query.setFilter(filter);
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		return datastore.prepare(query).asSingleEntity();
	}
	

	// Helper function that runs inside or outside a transaction.
	private static Entity getEntityByCourseIdAndStudentId(Long courseId, Key studentKey) {
		Query query = new Query(entityKind);
		query.setAncestor(studentKey);		
		Query.Filter filter = new FilterPredicate(namePropertyName,
				FilterOperator.EQUAL, courseId);
		query.setFilter(filter);
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		return datastore.prepare(query).asSingleEntity();
	}

	/*public static Grade getGradeByGCId(Long gcId, Long courseId)
			throws EntityNotFoundException {
		
		Entity gc = DatastoreUtil.getEntityByKey(entityKind, gcId);
	
		Query query = new Query(entityKind);
		query.setAncestor(gc.getKey());
		
		/*Query.Filter filter = new FilterPredicate(namePropertyName,
		 * FilterOperator.EQUAL, courseId); query.setFilter(filter);
		 */
	/*	DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();		
		Entity entity = datastore.prepare(query).asSingleEntity();
		if(entity == null) {
			return new Grade(createEntity(courseId, studentId, gc.getKey(), true));
		}
		
		return new Grade(entity);
	}*/

	public static Course getCourseDetailsByInstructor(String id,
			Key instructorKey) throws EntityNotFoundException {
		Query query = new Query(entityKind);
		query.setAncestor(instructorKey);
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		PreparedQuery pq = datastore.prepare(query);
		boolean exists = false;
		for (Entity result : pq.asIterable()) {
			String courseId = result.getProperty(namePropertyName).toString();
			if(courseId.equals(id)) {
				exists = true;
				break;
			}
//			  String firstName = (String) result.getProperty("firstName");
//			  String lastName = (String) result.getProperty("lastName");
//			  Long height = (Long) result.getProperty("height");
//
//			  System.out.println(firstName + " " + lastName + ", " + height + " inches tall");
		}
		if(!exists)
			return null;
		Course course = Course.getByCourseId(Long.valueOf(id));
		return course;
	}

	// Helper function that runs inside or outside a transaction.
	private static void saveOrCreateEntity(Entity entity) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		datastore.put(entity);
	}

	private static void deleteEntityByName(Long courseId) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Entity entity = getEntityByCourseId(courseId);
		if (entity != null) {
			datastore.delete(entity.getKey());
		}
		txn.commit();
	}

	private static Entity createEntities(Long courseId, Key gcKey,  boolean requiresTxn) throws EntityNotFoundException {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = null;
		if(requiresTxn)
			txn = datastore.beginTransaction();
		/*Entity entity = getEntityByCourseId(courseId);
		if (entity != null) {
			txn.commit();
			throw new CourseAlreadyExistsException();
		}*/
		List<Student> students = Student.getStudentsByCourseId(courseId);
		
		if(students == null || students.size() == 0) {
			txn.commit();
			return null;
		}
		
		for(Student student : students) {
			Entity entity = new Entity(entityKind, gcKey);
			entity.setProperty(namePropertyName, student.getID());
			entity.setProperty(points, 0);
			datastore.put(entity);
		}
		
		if(txn != null)
			txn.commit();
		
		return DatastoreUtil.getEntityByKey(GradableComponent.entityKind, gcKey.getId());
	}
	
	private static Entity createEntity(Long courseId, Long gcId, Long studentId, boolean requiresTxn) throws EntityNotFoundException {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = null;
		if(requiresTxn)
			txn = datastore.beginTransaction();
		/*Entity entity = getEntityByCourseId(courseId);
		if (entity != null) {
			txn.commit();
			throw new CourseAlreadyExistsException();
		}*/
		Entity gcEntity = DatastoreUtil.getEntityByKey(GradableComponent.entityKind, gcId);
		Entity entity = new Entity(entityKind, gcEntity.getKey());
		entity.setProperty(namePropertyName, studentId);
		entity.setProperty(points, 0);
		datastore.put(entity);
		
		if(txn != null)
			txn.commit();
		
		return entity;
	}

}
