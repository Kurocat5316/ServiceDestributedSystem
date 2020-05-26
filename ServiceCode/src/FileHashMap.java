
public class FileHashMap {
	String name;
	String name2;
	String name3;
	//String uploadDate;
	String receiveDate;
	String status;
	long time;
	
	FileHashMap(String name, String name2, String receiveDate){
		this.name = name;
		this.name2 = name2;
		this.name3 = "output.txt";
		this.receiveDate = receiveDate;
		status = "in queue";
		long time = 0;
	}
}
