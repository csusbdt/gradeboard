package gradesys;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;

public class GradableComponent {

	public static final String entityKind = "GradableComponent";
	
	private static final String namePropertyName = "name";
	
	private static final String points = "points";
	
	private static final String deadline = "deadline";
	
	private static final String courseId = "courseId";

	private Entity entity = null;
	
	private static final Logger logger = Logger.getLogger(GradableComponent.class.getName());

	private GradableComponent(Entity entity) {
		this.entity = entity;
	}

	public Long getID() {
		return (Long) entity.getKey().getId();
	}
	
	public Key getKey() {
		return entity.getKey();
	}

	public String getName() {
		return (String) entity.getProperty(namePropertyName);
	}

	private void setName(String uname) {
		entity.setProperty(namePropertyName, uname);
	}
	
	public int getPoints() {
		try {
			return Integer.valueOf(entity.getProperty(points).toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return 0;
		}
	}
	
	private void setPoints(String e) {
		entity.setProperty(points, e);
	}
	
	public Object getDeadline() {
		return entity.getProperty(deadline);
	}
	
	private void setDeadline(String e) {
		entity.setProperty(deadline, e);
	}

	public void save() {
		saveOrCreateEntity(entity);
	}

	public static void deleteByName(String name) {
		deleteEntityByName(name);
	}
	
	public static GradableComponent create(String name, String courseId, String points, String deadline) throws GradableComponentAlreadyExistsException {
		return new GradableComponent(createEntity(name, courseId, points, deadline));
	}
	
	public static GradableComponent update(String componentId, String courseId, String name, String points, String deadline) throws Exception {
		return new GradableComponent(updateEntity(componentId, courseId, name, points, deadline));
	}
	
	public static GradableComponent getByKey(Key key) {
		Entity entity = getEntityByKey(key);
		if (entity == null) {
			return null;
		} else {
			return new GradableComponent(entity);
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
	public static List<GradableComponent> getGradableComponentsByCourseId(Long cId) {
		Query query = new Query(GradableComponent.entityKind);
		Filter filter = FilterOperator.EQUAL.of(courseId, cId); 
		query.setFilter(filter);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Iterator<Entity> iterator = datastore.prepare(query).asIterator();
		List<GradableComponent> gcs = new ArrayList<GradableComponent>();
		while (iterator.hasNext()) {
			Entity entity = iterator.next();
			gcs.add(new GradableComponent(entity));
			logger.info("Listing Gradable Component " + entity.getProperty(namePropertyName));
		}
		return gcs;
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
	
	public static void deleteEntityByName(Long cId, Long gcId) {
		Entity entity = null;
		try {
			entity = DatastoreUtil.getEntityByKey(entityKind, gcId);
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			//
		}
		if(entity == null)
			return;
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Transaction txn = datastore.beginTransaction();
		datastore.delete(entity.getKey());
		txn.commit();
	}
	
	public static GradableComponent getByGradableComponentCourseId(String cId, String name)
			throws EntityNotFoundException {		
		Query query = new Query(entityKind);
		Query.CompositeFilter filter = Query.CompositeFilterOperator.and(
				FilterOperator.EQUAL.of(namePropertyName,  name),
				FilterOperator.EQUAL.of(courseId, cId));
		query.setFilter(filter);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity entity = datastore.prepare(query).asSingleEntity();
		if (entity == null) {
			return null;
		} else {
			return new GradableComponent(entity);
		}
	}
	
	private static Entity createEntity(String name, String cId, String pts, String dline) {
		
		Transaction txn = null;
		try {
			GradableComponent gc = getByGradableComponentCourseId(courseId, name);
			if(gc != null) {
				return gc.entity;
			}
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			TransactionOptions options = TransactionOptions.Builder.withXG(true);
			txn = datastore.beginTransaction(options);
			
			Entity gcEntity = null;
			if(gc == null) {
				//Create student
				gcEntity = new Entity(entityKind);
				gcEntity.setProperty(namePropertyName, name);
				gcEntity.setProperty(points, Long.valueOf(pts));
				gcEntity.setProperty(courseId, Long.valueOf(cId));
				gcEntity.setProperty(deadline, new Date(dline));
				datastore.put(gcEntity);
			} 				
			txn.commit();
			return gcEntity;
		} catch (Exception e) {
			return null;
		} finally {
			if(txn != null && txn.isActive())
				txn.rollback();
		}
	}
	
	private static Entity updateEntity(String gcId, String cId, String name,  String pts, String dline) throws GradableComponentNotFoundException {
		
		Transaction txn = null;
		try {
			Entity gcEntity = DatastoreUtil.getEntityByKey(entityKind, Long.valueOf(gcId));
			if(gcEntity == null) {
				throw new GradableComponentNotFoundException();
			}
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			txn = datastore.beginTransaction();
			gcEntity.setProperty(namePropertyName, name);
			gcEntity.setProperty(points, Long.valueOf(pts));
			gcEntity.setProperty(courseId, Long.valueOf(cId));
			gcEntity.setProperty(deadline, new Date(dline));
			datastore.put(gcEntity);					
			txn.commit();
			return gcEntity;
		} catch (Exception e) {
			if(e instanceof GradableComponentNotFoundException)
				throw (GradableComponentNotFoundException)e;
			return null;
		} finally {
			if(txn != null && txn.isActive())
				txn.rollback();
		}
	}
}