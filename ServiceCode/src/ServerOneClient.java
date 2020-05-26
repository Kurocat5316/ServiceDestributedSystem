import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.Random;

public class ServerOneClient extends Thread{
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        
        int id;
        
        public ServerOneClient(Socket s, int id)
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
            	System.out.println("Run Start");
            	
				String require = in.readLine();
				
				System.out.println(require);
				
				
				
				switch(require){
					case "Upload":
						GetFile();
						break;
					case "Password":
						out.println("Get");
						String passcode = in.readLine();
						System.out.println(passcode);
						StatusCheck(Integer.parseInt(passcode));
						break;
					case "FinishWork":
						out.println("Get");
						String code = in.readLine();
						out.println("Get");
						String time = in.readLine();
						FinishWork(Integer.parseInt(code), Long.parseLong(time));
						break;
					case "ReSchedule":
						out.println("Get");
						String code2 = in.readLine();
						Reschedule(Integer.parseInt(code2));
						break;
					case "CancelWork":
						out.println("Get");
						String code3 = in.readLine();
						CancelWork(Integer.parseInt(code3));
						break;
	        	}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }
        
        private void CancelWork(int passcode) {
        	if(WorkerQueue.list.contains(passcode)){
        		WorkerQueue.list.remove(passcode);
        		Memory.DeleteElement(passcode);
        	}
        	else {
        		for(ServerInfo ser : ServerList.serInfo){
	        			try {
		        			Socket socketnet = new Socket(ser.host, 2000);
		
		        			BufferedReader in = new BufferedReader(
		        					                    new InputStreamReader(
		        					                    		socketnet.getInputStream()));;
		        	        
								PrintWriter out = new PrintWriter (
									        new BufferedWriter(
									                new OutputStreamWriter(
									                		socketnet.getOutputStream())), true);
								
								out.println("CancelJob");
								
								in.readLine();
								
								out.println(passcode);
								
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        	        }
        		Memory.DeleteElement(passcode);
        		out.println("Done");
        	}
        }

		private void Reschedule(int passcode){
			WorkerQueue.list.add(passcode);

			System.out.println("Rescheduling process done");
		}
        
        private void FinishWork(int passcode, long time) {
        	Memory.ChangeStatus(passcode, "finished", time);
        	
        	System.out.println("Finish Job");
        }
        
        private void StatusCheck(int passcode) {
        	
        	
        	System.out.println("StatusCheck");
        	
        	if(Memory.KeyCheck(passcode)) {
        		FileHashMap status = Memory.GetStatus(passcode);
        	        	
        	    out.println(status.status);
        	    
        	    try {
					in.read();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	    
				String seconds = Long.toString(status.time).subString(0, 1);
        	    out.println(seconds);
        	}
        	else {
        		out.println("not exist");
        	}
        	
        	
        	
        	
        }

		private void GetFile() {
			try {
				System.out.println("Delay Seting");
				socket.setSoTimeout(300000);
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
        	try {
        		int passcode = PassCodeGenerator();
        		
        		out.println(passcode);
        		
				String name1 = in.readLine();
				
				out.println("Got");
				
				System.out.println("Get name1");
				
				String name2 = in.readLine();
				
				out.println("Got");
				
				System.out.println("Get name2");
				
				String receiveDate = in.readLine();
				
				Memory.NewElement(passcode, name1, name2, receiveDate);
				
				WorkerQueue.list.add(passcode);
				
				System.out.println("Task Done");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	
        	
        }
		
		private int PassCodeGenerator() {
			int n = 0;
			while(true) {
				n = 10000 + new Random().nextInt(90000);
				if(!Memory.KeyCheck(n))
					break;
			}
			
			return n;
		}
}
