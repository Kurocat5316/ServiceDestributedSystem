import java.io.*;
import java.net.*;

public class MasterSocket {
        static final int PORT = 40;
        public static void main(String[] args)
               throws IOException {
        	new MasterSlaves();
           ServerSocket s = new ServerSocket(PORT);
           System.out.println("Server Started the ip is: " + s.getInetAddress().getHostAddress());
           
           int threadid=0;
           try {
               while(true) {
                   // Blocks until a connection occurs:
                   Socket socket = s.accept();
                   try {

                           Thread work= new ServerOneClient(socket, threadid);
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
