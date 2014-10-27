import java.io.*;
import java.net.*;
public class Server {
  private int port=8000;
  private ServerSocket mServerSocket;

  public Server() throws IOException {
    mServerSocket = new ServerSocket(port,3);  // connection request queue length 3
    System.out.println("server launch");
  }

  public void service() {
    while (true) {
      Socket socket=null;
      try {
        socket = mServerSocket.accept();  // get a connection from request queue
        System.out.println("New connection accepted " +
        socket.getInetAddress() + ":" +socket.getPort());
        sendPrimaryData(socket);
      }catch (IOException e) {
         e.printStackTrace();
      }finally {
         try{
             System.out.println("close socket");
           if(socket!=null)socket.close();
         }catch (IOException e) {e.printStackTrace();}
      }
    }
  }

  private PrintWriter getWriter(Socket socket)throws IOException{
      OutputStream socketOut = socket.getOutputStream();
      return new PrintWriter(socketOut,true);
    }

  private void sendPrimaryData(Socket socket) {
      PrintWriter pw = null;
      try {
          pw = getWriter(socket);
      } catch (IOException e) {
          e.printStackTrace();
          return;
      }
      System.out.println("pw.write(100);");
      pw.write(100);
      System.out.println("pw.write(200);");
      pw.write(200);
      System.out.println("pw.write(300);");
      pw.write(300);
      pw.flush();
  }

  public static void main(String args[])throws Exception {
    Server server=new Server();
//    Thread.sleep(60000*10);  // sleep 10 minutes
    server.service();
  }
}
