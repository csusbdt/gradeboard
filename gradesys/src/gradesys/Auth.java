package gradesys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.AuthPermission;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class Auth {

	public static final String entityKind = "auth";
	public static final String namePropertyName = "courseId";
	private static final String permissions = "permissions";
	
	private Entity entity = null;

	private Auth(Entity entity) {
		this.entity = entity;
	}

	public Long getID() {
		return (Long) entity.getKey().getId();
	}
	
	public AuthPermissions getPermssions() {
		return AuthPermissions.valueOf(entity.getProperty(permissions).toString());
	}
	
	public void setPermssions(AuthPermissions permission) {
		entity.setProperty(permissions, permission.toString());
	}

	public Long getCourseId() {
		return (Long) entity.getProperty(namePropertyName);
	}

	public Key getInstructor() {
		return entity.getParent();
	}

	private void setName(Long courseId) {
		entity.setProperty(namePropertyName, courseId);
	}

	public void save() {
		saveOrCreateEntity(entity);
	}

	public static void deleteByName(Long courseId) {
		deleteEntityByName(courseId);
	}

	public static Auth create(Long courseId, Key instructorKey, AuthPermissions perms, boolean requiresTxn)
			throws CourseAlreadyExistsException {
		return new Auth(createEntity(courseId, instructorKey, perms, requiresTxn));
	}

	public static Auth getByCourseId(Long courseId) {
		Entity entity = getEntityByCourseId(courseId);
		if (entity == null) {
			return null;
		} else {
			return new Auth(entity);
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

	public static List<Course> getCoursesByInstructor(Key instructorKey)
			throws EntityNotFoundException {
		Query query = new Query(entityKind);
		query.setAncestor(instructorKey);
		/*
		 * Query.Filter filter = new FilterPredicate(namePropertyName,
		 * FilterOperator.EQUAL, courseId); query.setFilter(filter);
		 */
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Iterator<Entity> iterator = datastore.prepare(query).asIterator();
		List<Course> courses = new ArrayList<Course>();
		while (iterator.hasNext()) {
			Entity entity = iterator.next();
			Long courseId = (Long) entity.getProperty(namePropertyName);
			Course course = Course.getByCourseId(courseId);
			courses.add(course);
		}
		return courses;

	}

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

	private static Entity createEntity(Long courseId, Key instructorKey, AuthPermissions perms, boolean requiresTxn)
			throws CourseAlreadyExistsException {
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
		Entity entity = new Entity(entityKind, instructorKey);
		if(perms == null)
			entity.setProperty(permissions, AuthPermissions.USER.toString());
		else
			entity.setProperty(permissions, perms.toString());
		
		entity.setProperty(namePropertyName, courseId);
		datastore.put(entity);
		if(txn != null)
			txn.commit();
		return entity;
	}
}