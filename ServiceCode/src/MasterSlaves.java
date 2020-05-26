import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class MasterSlaves extends Thread {

	int index = 1;
	boolean flag2 = true;
	
	public MasterSlaves(){
		
		ServerList.serInfo.add(new ServerInfo("144.6.227.5", "ubuntu", "~/assignment3/master.pem", ""));
		ServerList.serInfo.add(new ServerInfo("144.6.224.142", "ubuntu", "~/assignment3/worker.pem", ""));
		
		start();
	}
	
	public void run(){
		while(true) {
			if(WorkerQueue.list.isEmpty()) {
				try {
					System.out.println("It is waiting.");
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else {
				try {
					CPUStatus();
					
					
					System.out.println("Seperating work");
					AllocateWork();
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
	private void CPUStatus() throws IOException {
		ArrayList<Float> CPU = new ArrayList<Float>();
		float min = 0;
		int i = 0;
		boolean flag = true;
		for(ServerInfo ser : ServerList.serInfo){
			Socket socketnet = new Socket(ser.host, 2000);

			BufferedReader in = new BufferedReader(
					                    new InputStreamReader(
					                    		socketnet.getInputStream()));;
	        PrintWriter out = new PrintWriter (
				        new BufferedWriter(
				                new OutputStreamWriter(
				                		socketnet.getOutputStream())), true);



			System.out.println("Get CPU Usage");

			out.println("CPUStatus");

			try {
				CPU.add( Float.parseFloat(in.readLine()));
				
				System.out.println("Getting CPU");
				
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			socketnet.close();
		}
		
		for(float usage : CPU) {
			if(usage < 70.00)
				flag = false;
			if(min < usage)
				min = usage;
			
			i = CPU.indexOf(min);
		}
		
		if(CPU.get(0) < 30.00 && CPU.get(1) < 30.00 && flag2) {
			NewInstances.createServer();
			flag2 = !flag2;
		}
		
		if(flag) {
			System.out.println("Robin Method");
			index ++;
			if(index >= ServerList.serInfo.size())
				index = 0;
		}
		else {
			System.out.println("Pull Method");
			index = i;
		}
		
		System.out.println("Got CPU Usage");

		
	}
	
	private void AllocateWork() {
		ServerInfo slaveInfo = ServerList.serInfo.get(index);
		int passcode = 0;
		passcode = WorkerQueue.list.remove(0);

		Memory.ChangeStatus(passcode, "processing", 0);

		FileHashMap tmp = Memory.GetDetail(passcode);
		try {
			Socket socketnet = new Socket(slaveInfo.host,2000);
			
			BufferedReader in = new BufferedReader(
					                    new InputStreamReader(
					                    		socketnet.getInputStream()));;
	        PrintWriter out = new PrintWriter (
                    new BufferedWriter(
                            new OutputStreamWriter(
                            		socketnet.getOutputStream())), true);
	        
	        System.out.println("Task Allocate");
			
			out.println("Execution");
	        
	        String signal = in.readLine();
	        
	        out.println(passcode);
	        
	        signal = in.readLine();
	        
	        out.println(tmp.name);
	        
	        signal = in.readLine();
	        
	        out.println(tmp.name2);
	        
	        signal = in.readLine();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
}
