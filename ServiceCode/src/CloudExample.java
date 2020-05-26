import java.util.List;
import java.util.Map;

import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.Address;
import org.openstack4j.model.compute.Addresses;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.openstack.OSFactory;

import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * OpenStack Example
 *
 */
public class CloudExample 
{
	OSClientV3 os = OSFactory.builderV3()
                .endpoint("https://keystone.rc.nectar.org.au:5000/v3/")
                .credentials("junqians@utas.edu.au", "N2E3NzI5YWMyNDdlNTM3",Identifier.byName("Default"))
                .scopeToProject(Identifier.byId("df33db2bd3fc4e4b8ed052a9d9f7f2d8"))
                .authenticate();
	
	public void createServer() {
		ServerCreate server = Builders.server()
                .name("Worker3")
                .flavor("cba9ea52-8e90-468b-b8c2-777a94d81ed3")
                .image("99d9449a-084f-4901-8bd8-c04aebd589ca")
                .keypairName("Worker")
                .addSecurityGroup("default")
                .addSecurityGroup("ssh")
                .availabilityZone("tasmania")
                .build();

		os.compute().servers().bootAndWaitActive(server, 1000000);
		
		
		ListServers();

		
		
		
	}
	
	
	public void Initial(String ipv4) {
		String host= "144.6.227.116";
	    String user="ubuntu";
	    //String password="";
	    String privateKey = "C:/Users/junqians/Downloads/Assignment3/worker.pem";
	    String command1="sudo apt-get --assume-yes update;sudo apt --assume-yes install default-jre;sudo apt-get --assume-yes install unzip";
	    
	    //sudo apt --assume-yes install default-jre
	    
	    try{
	    	java.util.Properties config = new java.util.Properties(); 
	    	config.put("StrictHostKeyChecking", "no");
	    	JSch jsch = new JSch();
	    	jsch.addIdentity(privateKey);
	    	Session session=jsch.getSession(user, host, 22);
	    	//session.setPassword(password);
	    	
	    	session.setConfig(config);
	    	session.connect();
	    	System.out.println("Connected");
	    	
	    	//command1
	    	Channel channel=session.openChannel("exec");
	        ((ChannelExec)channel).setCommand(command1);
	        channel.setInputStream(null);
	        ((ChannelExec)channel).setErrStream(System.err);
	        
	        InputStream in=channel.getInputStream();
	        channel.connect();
	        byte[] tmp=new byte[1024];
	        while(true){
	          while(in.available()>0){
	            int i=in.read(tmp, 0, 1024);
	            if(i<0)break;
	            System.out.print(new String(tmp, 0, i));
	          }
	          if(channel.isClosed()){
	            System.out.println("exit-status: "+channel.getExitStatus());
	            break;
	          }
	          try{Thread.sleep(1000);}catch(Exception ee){}
	        }
	        

	        
	        channel.disconnect();
	        session.disconnect();
	        System.out.println("Done");
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    
	    
	}
	
	public void UploadFile() {
		try {
		
			String host= "144.6.227.116";
		    String user="ubuntu";
		    //String password="";
		    String privateKey = "C:/Users/junqians/Downloads/Assignment3/worker.pem";
			
		    
		    java.util.Properties config = new java.util.Properties(); 
	    	config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();
			jsch.addIdentity(privateKey);
			Session session = jsch.getSession(user, host, 22);
	
			session.setConfig(config);
			session.connect();
			
	
			ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
			
			sftpChannel.connect();
	
			
			sftpChannel.put("C:/Users/junqians/Downloads/Assignment3.zip", "/home/ubuntu");
			
			sftpChannel.disconnect();
			
			System.out.println("Done");
		
		} catch (JSchException | SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

			//List of all Servers
		public void ListServers() {

	  List<? extends Server> servers = os.compute().servers().list();
		Server Detail = servers.get(0);
		Addresses ip = Detail.getAddresses();
		Map<String, List<? extends Address>>record = ip.getAddresses();
		
		List<? extends Address>record2 = record.get("tas");
		
		Address record3 = record2.get(0);
		
		String ipv4 = record3.getAddr();
		
		System.out.println(ipv4);
		
		
	}
		
	public void Deployment() {
		String host= "144.6.227.116";
	    String user="ubuntu";
	    //String password="";
	    String privateKey = "C:/Users/junqians/Downloads/Assignment3/worker.pem";
	    String command1="sudo unzip Assignment3";
	    
	    //sudo apt --assume-yes install default-jre
	    
	    try{
	    	java.util.Properties config = new java.util.Properties(); 
	    	config.put("StrictHostKeyChecking", "no");
	    	JSch jsch = new JSch();
	    	jsch.addIdentity(privateKey);
	    	Session session=jsch.getSession(user, host, 22);
	    	//session.setPassword(password);
	    	
	    	session.setConfig(config);
	    	session.connect();
	    	System.out.println("Connected");
	    	
	    	//command1
	    	Channel channel=session.openChannel("exec");
	        ((ChannelExec)channel).setCommand(command1);
	        channel.setInputStream(null);
	        ((ChannelExec)channel).setErrStream(System.err);
	        
	        InputStream in=channel.getInputStream();
	        channel.connect();
	        byte[] tmp=new byte[1024];
	        while(true){
	          while(in.available()>0){
	            int i=in.read(tmp, 0, 1024);
	            if(i<0)break;
	            System.out.print(new String(tmp, 0, i));
	          }
	          if(channel.isClosed()){
	            System.out.println("exit-status: "+channel.getExitStatus());
	            break;
	          }
	          try{Thread.sleep(1000);}catch(Exception ee){}
	        }
	        

	        
	        channel.disconnect();
	        session.disconnect();
	        System.out.println("Done");
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	}



	//Delete a Server
	public void deleteServer() {

			os.compute().servers().delete("b81a26c5-64d3-4ab5-bc4d-51d5d32fd77f");
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
	
	
    public static void main( String[] args )
    {
    	CloudExample openstack=new CloudExample();
    	openstack.createServer();
    	openstack.Initial("1");
    	openstack.UploadFile();
       
    	openstack.Deployment();
    	
    	//openstack.deleteServer();
    }
}
