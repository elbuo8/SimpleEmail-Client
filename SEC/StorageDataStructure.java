package SEC;


/**
 * This interface describes an object that is going to keep an organized storage of diverse objects
 * and refer to them by keys. This data structure can be, and is intended to be read from/written to
 * a file. The keys are String objects.
 * @author hfranqui
 *
 */
public interface StorageDataStructure<E> {

	/**
	 * Adds a new object to the storage. If a key already exists in the storage, the object
	 * with that key is replaced by the given one.
	 * @param key The key of the object to be stored
	 * @param obj The object to be stored.
	 */
	public void add(String key, E obj);
	
	/**
	 * Removes the object with the specified key from the storage.
	 * @param key The key of the object to be removed.
	 * @return True if the object was successfully removed, false otherwise.
	 */
	public boolean remove(String key);
	
	/**
	 * Returns the object with the given key in the storage.
	 * @param key The key of the object to be returned.
	 * @return The object that has the key specified.
	 */
	public E get(String key);
	
	/**
	 * Checks whether an object with the given key exists in the storage.
	 * @param key The key of the object to be checked.
	 * @return True if the object is in the storage, false otherwise.
	 */
	public boolean contains(String key);
	
	/**
	 * Loads the storage from a given String array, that could be loaded from a file.
	 * @param file The String array containing a storage object.
	 * @return True if the String array contained a proper storage object, false otherwise.
	 */
	public boolean loadFromFileFormat(String[] file);
	
	/**
	 * Returns a String array describing the data storage that can be saved to a file. 
	 * @return The String array containing the data storage.
	 */
	public String[] toFileFormat();
	
	/**
	 * Returns an array of all the objects of type E being stored in the storage.
	 * @return Array of objects stored.
	 */
	public E[] getAllValues();
	
	/**
	 * Returns the actual size of the storage. 
	 * @return The size of the storage.
	 */
	public int size();
	
	/**
	 * Checks whether the data storage is empty or not.
	 * @return True if it's empty, false otherwise.
	 */
	public boolean isEmpty();
	
	/**
	 * Empties the Data Storage.
	 */
	public void clear();
	
}
