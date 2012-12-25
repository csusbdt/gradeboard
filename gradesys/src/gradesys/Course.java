package gradesys;

import javax.persistence.EntityManager;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;

public class Course {

	private static final String entityKind = "course";
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
	
	public static Course create(String name) throws CourseAlreadyExistsException {
		return new Course(createEntity(name));
	}

	public static Course getByName(String name) {
		Entity entity = getEntityByName(name);
		if (entity == null) {
			return null;
		} else {
			return new Course(entity);
		}
	}
	
	public static Course getByCourseId(Long courseId) throws EntityNotFoundException {
		//Key key = KeyFactory.createKey(Course.class.getSimpleName(), courseId);
		Entity entity = getEntityByKey(courseId);
		if (entity == null) {
			return null;
		} else {
			return new Course(entity);
		}
	}
	
	// Helper function that runs inside or outside a transaction.
		public static Entity getEntityByKey(Long id) throws EntityNotFoundException {
			/*Query query = new Query(entityKind);
			Query.Filter filter = new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, id); 
			query.setFilter(filter);*/
			Key key = KeyFactory.createKey("course", id);
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			return datastore.get(key);		
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
	private static void saveOrCreateEntity(Entity entity) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		datastore.put(entity);		
	}

	private static void deleteEntityByName(String name) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Entity entity = getEntityByName(name);
		if (entity != null) {
			datastore.delete(entity.getKey());
		}
		txn.commit();
	}
	
	private static Entity createEntity(String name) throws CourseAlreadyExistsException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Entity entity = getEntityByName(name);
		if (entity != null) {
			txn.commit();
			throw new CourseAlreadyExistsException();
		}
		entity = new Entity(entityKind);
		entity.setProperty(namePropertyName, name);
		datastore.put(entity);
		txn.commit();
		return entity;
	}
}
