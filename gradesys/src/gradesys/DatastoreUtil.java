package gradesys;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.users.User;

public class DatastoreUtil {
	
	// Helper function that runs inside or outside a transaction.
	public static Entity getEntityByKey(String kind, Long id) throws EntityNotFoundException {
		/*
		 * Query query = new Query(entityKind); Query.Filter filter = new
		 * FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL,
		 * id); query.setFilter(filter);
		 */
		Key key = KeyFactory.createKey(kind, id);

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		return datastore.get(key);
	}
	
		
	public static User getUserIdFromAccount(User newUser) throws EntityNotFoundException,  Exception {
		
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Entity entity = new Entity("User");
		entity.setProperty("User", newUser);
		Key key = datastore.put(entity);
		txn.commit();
		entity = datastore.get(key);
		newUser = (User)entity.getProperty("User");
		String userId = newUser.getUserId();
		datastore.delete(key);	
		if(userId == null) {
			throw new Exception("Not a Google accoutn.");
		}
		return newUser;
	}
}