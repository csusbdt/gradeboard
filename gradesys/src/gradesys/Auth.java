package gradesys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class Auth {

	private static final String entityKind = "auth";
	private static final String namePropertyName = "courseId";
	private Entity entity = null;

	private Auth(Entity entity) {
		this.entity = entity;
	}

	public Long getID() {
		return (Long) entity.getKey().getId();
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

	public static Auth create(Long courseId, Key instructorKey)
			throws CourseAlreadyExistsException {
		return new Auth(createEntity(courseId, instructorKey));
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
		Query.Filter filter = new FilterPredicate(namePropertyName,
				FilterOperator.EQUAL, id);
		query.setFilter(filter);
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Entity instructorEntity = datastore.prepare(query).asSingleEntity();
		Long courseId = (Long) instructorEntity.getProperty(namePropertyName);
		Course course = Course.getByCourseId(courseId);
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

	private static Entity createEntity(Long courseId, Key instructorKey)
			throws CourseAlreadyExistsException {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Entity entity = getEntityByCourseId(courseId);
		if (entity != null) {
			txn.commit();
			throw new CourseAlreadyExistsException();
		}
		entity = new Entity(entityKind, instructorKey);
		entity.setProperty(namePropertyName, courseId);
		datastore.put(entity);
		txn.commit();
		return entity;
	}
}