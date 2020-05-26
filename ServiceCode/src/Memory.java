import java.util.concurrent.ConcurrentHashMap;

public class Memory {
private static ConcurrentHashMap<Integer, FileHashMap> data = new ConcurrentHashMap<Integer, FileHashMap>();
	
	public static void NewElement(int key, String name, String name2, String receiveDate) {

			data.put(key, new FileHashMap(name, name2, receiveDate));
	}
	
	public static boolean KeyCheck(int key) {
		return data.containsKey(key);
	}
	
	public static FileHashMap GetStatus(int key) {
		FileHashMap tmp = data.get(key);
		return tmp;
	}
	
	public static FileHashMap GetDetail(int key) {
		FileHashMap tmp = data.get(key);
		return tmp;
	}

	public static void ChangeStatus(int key, String status, long time){
		FileHashMap tmp = data.get(key);
		tmp.status = status;
		tmp.time = time;
		data.put(key, tmp);
	}
	
	public static void DeleteElement(int key) {
		data.remove(key);
	}
}
