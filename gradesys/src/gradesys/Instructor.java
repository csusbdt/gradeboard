package gradesys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class Instructor {

	private static final String entityKind = "Instructor";
	
	private static final String namePropertyName = "userId";
	
	private static final String name = "name";
	
	private static final String email = "email";

	private Entity entity = null;

	private Instructor(Entity entity) {
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
	
	private void setUserId(String uid) {
		entity.setProperty(namePropertyName, uid);
	}
	
	public String getEmail() {
		return (String) entity.getProperty(email);
	}
	
	private void setEmail(String e) {
		entity.setProperty(email, e);
	}

	public void save() {
		saveOrCreateEntity(entity);
	}

	public static void deleteByName(String name) {
		deleteEntityByName(name);
	}
	
	public static Instructor create(String name, String userId, String email) throws CourseAlreadyExistsException {
		return new Instructor(createEntity(name, userId, email));
	}

	public static Instructor getByUserId(String uid) {
		Entity entity = getEntityByName(uid);
		if (entity == null) {
			return null;
		} else {
			return new Instructor(entity);
		}
	}
	
	public static Instructor getByKey(Key key) {
		Entity entity = getEntityByKey(key);
		if (entity == null) {
			return null;
		} else {
			return new Instructor(entity);
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
	public static List<Instructor> getInstructorsByCourseId(String courseId, Key instructorKey) {
		Query query = new Query(Auth.entityKind);
		Query.Filter filter = new FilterPredicate(Auth.namePropertyName, FilterOperator.EQUAL, Long.valueOf(courseId)); 
		query.setFilter(filter);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Iterator<Entity> iterator = datastore.prepare(query).asIterator();
		List<Instructor> instructors = new ArrayList<Instructor>();
		while (iterator.hasNext()) {
			Entity entity = iterator.next();
			instructors.add(new Instructor(getEntityByKey(entity.getParent())));
		}
		return instructors;
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
	
	public static Instructor updateUserId(String userId, String nickName, String email) throws Exception {
		Instructor instructor = null;
		instructor = getByInstructorEmail(email);
		if (instructor == null) {
			throw new InstructorNotFoundException();
		} else {
			int index = nickName.indexOf("@");
			if(index > 0) {
				nickName = nickName.substring(0, index);
			}
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Transaction txn = datastore.beginTransaction();
			instructor.setName(nickName);
			instructor.setUserId(userId);
			instructor.setEmail(email);
			datastore.put(instructor.entity);
			txn.commit();
			return instructor;
		}
	}
	
	public static Instructor getByInstructorEmail(String uemail)
			throws EntityNotFoundException {		
		Query query = new Query(entityKind);
		Query.Filter filter = new FilterPredicate(email, FilterOperator.EQUAL, uemail); 
		query.setFilter(filter);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity entity = datastore.prepare(query).asSingleEntity();
		if (entity == null) {
			return null;
		} else {
			return new Instructor(entity);
		}
	}
	
	private static Entity createEntity(String iname, String userId, String em) throws CourseAlreadyExistsException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Entity entity = getEntityByName(name);
		if (entity != null) {
			txn.commit();
			throw new CourseAlreadyExistsException();
		}
		entity = new Entity(entityKind);
		entity.setProperty(namePropertyName, userId);
		entity.setProperty(name, iname);
		entity.setProperty(email, em);
		datastore.put(entity);
		txn.commit();
		return entity;
	}
	
	public static void main(String str[]) {
		try {
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();
			Instructor instructor = Instructor.getByUserId(user.getUserId());
			if(instructor == null)
				throw new Exception("Missing instructor");
			getInstructorsByCourseId("30", instructor.getKey());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
