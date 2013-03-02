package gradesys;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class DatastoreUtil {
	
	// Helper function that runs inside or outside a transaction.
	public static Entity getEntityByKey(Long id) throws EntityNotFoundException {
		/*
		 * Query query = new Query(entityKind); Query.Filter filter = new
		 * FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL,
		 * id); query.setFilter(filter);
		 */
		Key key = KeyFactory.createKey("course", id);

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		return datastore.get(key);
	}

}
