import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

import com.jcraft.jsch.JSchException;

public class Slave {
        static final int PORT = 2000;
        public static void main(String[] args)
               throws IOException {
           ServerSocket s = new ServerSocket(PORT);
           System.out.println("Server Started the ip is: " + s.getInetAddress().getHostAddress());

           int threadid=0;
           try {
               while(true) {
                   // Blocks until a connection occurs:
                   Socket socket = s.accept();
                   try {

                           Thread work= new SlaveServer(socket, threadid);
                           System.out.println("Thread: " + threadid + " connected");
                       threadid++;
                       work.start();

                   }
                   catch(IOException e) {
                       // If it fails, close the socket,
                       // otherwise the thread will close it:
                       socket.close();
                   }
               }

           } finally {
               s.close();
           }
        }
}


class SlaveServer extends Thread{
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private ServerInfo serv;
        int id;
        
        enum Language
        {JAVA, PYTHON};
        
        public SlaveServer(Socket s, int id)
               throws IOException {
           socket = s;
           this.id = id;
           
           in =
                   new BufferedReader(
                                   new InputStreamReader(
                                                   socket.getInputStream()));
           out =
                   new PrintWriter (
                           new BufferedWriter(
                                           new OutputStreamWriter(
                                                           socket.getOutputStream())), true);

        }

        public void run() {
        	try {
				String order = in.readLine();
				
				
				
				switch(order){
					case "CPUStatus":
						System.out.println("CPU Check");
						
						CheckCPU();
						break;
					case "Execution":
						out.println("Get");
						Execution();
						break;
					case "CancelJob":
						out.println("Get");
						WorkTerminate();
						break;
				}
				
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        
        	
        }

		private void WorkTerminate(){
			int passcode = 0;
			try {
				passcode = Integer.parseInt(in.readLine());
				
				ThreadRecord.CancelWork(passcode);
				
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
        
        private void CheckCPU() {
			ProcessBuilder processBuilder = new ProcessBuilder("/bin/sh", "-c", "sar -u 1 1 | tail -n 1 |awk '{print $NF}'");
			System.out.println("Initial");
			Process proc;
			try {
				proc = processBuilder.start();
				BufferedReader stdInput = new BufferedReader(new 
						InputStreamReader(proc.getInputStream()));

					// Read the output from the command

					String s = null;
					
					System.out.println("Getting CPU");
					while ((s = stdInput.readLine()) != null) {
						out.println(s);
						System.out.println(s);
					}
					//System.out.println(s);
					//out.println(s);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        
        private void Execution(){
        	
        	
        	try {
				int passcode = Integer.parseInt(in.readLine());
				
				out.println("Get");
				
				String name1 = in.readLine();
				
				out.println("Get");
				
				String name2 = in.readLine();
				
				out.println("Get");
				
				Thread job = new ProcessExecution(passcode, name1, name2);
				ThreadRecord.NewElement(passcode, job);
				job.start();

			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

}


class ProcessExecution extends Thread {
	private int passcode;
	private String name1;
	private String name2;
    private ServerInfo serv;
    int id;
    
    enum Language
    {JAVA, PYTHON};
    
    public ProcessExecution(int passcode, String name1, String name2) throws IOException {
    	this.passcode = passcode;
    	this.name1 = name1;
    	this.name2 = name2;
    	serv = new ServerInfo("144.6.227.101", "ubuntu", "~/assignment3/master.pem", "");
    }

    public void run() {
		String stringPasscode = String.valueOf(passcode);

		try
		{
			
			//downloads all files from ~/id and executes and uploads output
			long time = workerProcess(serv, stringPasscode, name1, name2);
			
			System.out.println("Change Status");
			Socket socket = new Socket(serv.host, 40);
			
			BufferedReader in =
					new BufferedReader(
									new InputStreamReader(
													socket.getInputStream()));
			PrintWriter out =
					new PrintWriter (
							new BufferedWriter(
											new OutputStreamWriter(
															socket.getOutputStream())), true);

			
			out.println("FinishWork");
			
			String signal = in.readLine();
			
			out.println(passcode);
			
			signal = in.readLine();
			
			out.println(time);
			
			System.out.println("Change Status done");
		} 
		catch (JSchException | IOException e)
		{
			try
			{

				System.out.println("Faile & Reshedule");

				Socket socket = new Socket(serv.host, 40);
				
				BufferedReader in =
						new BufferedReader(
										new InputStreamReader(
														socket.getInputStream()));
				PrintWriter out =
						new PrintWriter (
								new BufferedWriter(
												new OutputStreamWriter(
																socket.getOutputStream())), true);

				
				out.println("ReSchedule");
				
				String response = in.readLine();
				
				out.println(passcode);
			}catch(IOException e1){
				e1.printStackTrace();
			}

			e.printStackTrace();
		}

    }
    
    
    public static long workerProcess(ServerInfo info, String id, String name1, String name2) throws IOException, JSchException
	{
		String remoteFilePath = "/var/www/html/files/"+id;
		System.out.println("Downloading files");
		downloadFiles(info, remoteFilePath);
		System.out.println("Files downloaded");
		String localpath = "/home/"+info.user+"/"+id;


		long retval = 0;
		long starttime;
		Language lang = getLang(localpath);
		starttime = System.nanoTime();
		switch (lang)
		{
			case JAVA:
				System.out.println("Executing " + getExecName(localpath));
				processCommand("java -cp ~/" + id + "/*.jar " + getExecName(localpath) + " " + getInputName(localpath) + " > ~/"+id+"/output.txt");
				System.out.println("File Executed");
				break;
			case PYTHON:
				System.out.println("Executing " + getExecName(localpath));
				processCommand("python " + localpath +"/"+ getExecName(localpath) + ".py " + getInputName(localpath) + " > ~/"+id+"/output.txt");
				System.out.println("File Executed");
				break;
		}
		retval = System.nanoTime() - starttime;
		System.out.println("Uploading output");
		uploadFile(info, localpath + "/output.txt", remoteFilePath + "/output.txt");
		ThreadRecord.FinishWork(Integer.parseInt(id));
		System.out.println("File uploaded");
		
		return retval;
		
		
	}

	public static void downloadFiles (ServerInfo info, String remoteFilePath) throws JSchException
	{
		System.out.println("scp -r -i " + info.privateKey + " " + info.user+"@"+info.host+":" + remoteFilePath + " ~/");
		processCommand("scp -r -i " + info.privateKey + " " + info.user+"@"+info.host+":" + remoteFilePath + " ~/");
	}
		
	public static void uploadFile (ServerInfo info, String outputSource, String outputDest) throws JSchException
	{
		System.out.println("scp -i " + info.privateKey + " " + outputSource + " " + info.user+"@"+info.host+":"+outputDest);
		processCommand("scp -i " + info.privateKey + " " + outputSource + " " + info.user+"@"+info.host+":"+outputDest);	
		
	}
	
	public static void processCommand (String Command)
	{

		try
		{
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", Command);
			Process pro = builder.start();
			pro.waitFor();
			pro.destroy();

		} 
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static String getExecName(final String foldername)
	{
		
		File folder = new File(foldername);
		for(final File fileEntry : folder.listFiles())
		{
			String filename = fileEntry.getName();
			String extension = "";

			int i = filename.lastIndexOf('.');
			if (i > 0) {
			    extension = filename.substring(i+1);
			}
			if( extension.equals("jar") || extension.equals("py"))
			{
				return filename.substring(0, i);
			}
		}
		return "";
	}

	public static String getInputName(final String foldername)
	{
		File folder = new File(foldername);
		for(final File fileEntry : folder.listFiles())
		{
			String filename = fileEntry.getName();
			String extension = "";

			int i = filename.lastIndexOf('.');
			if (i > 0) {
			    extension = filename.substring(i+1);
			}
			if( extension.equals("txt"))
			{
				return fileEntry.getPath();
			}
		}
		return "";
	}

	public static Language getLang(String foldername)
	{
		File folder = new File(foldername);
		for(final File fileEntry : folder.listFiles())
		{
			String filename = fileEntry.getName();
			String extension = "";

			int i = filename.lastIndexOf('.');
			if (i > 0) {
			    extension = filename.substring(i+1);
			}
			if( extension.equals("jar"))
			{
				return Language.JAVA;
			}
			else if(extension.equals("py"))
			{
				return Language.PYTHON;
			}
		}
		return Language.JAVA;
	}

    
   
}

class ServerInfo
{
	String host;
	String user;
	String privateKey;
	String password;

	public ServerInfo(String host, String user, String privateKey, String password)
	{
		this.host = host;
		this.user = user;
		this.password = password;
		this.privateKey = privateKey;

	}
}


class ThreadRecord {
	private static ConcurrentHashMap<Integer, Thread> dataRecord = new ConcurrentHashMap<Integer, Thread>();

	public static void NewElement(int key, Thread thread) {
			dataRecord.put(key, thread);
	}

	public static void FinishWork(int key) {
			dataRecord.remove(key);
	}

	public static void CancelWork(int key){
		Thread thread = dataRecord.remove(key);
		if(thread != null){
			thread.interrupt();
			System.out.println("Stop Job Successful");
		}else{
			System.out.println("Work Alrady Done");
		}
	}
}

