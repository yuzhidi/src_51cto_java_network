package multithread4;
import java.net.*;
import java.io.*;
public class AdminClient{
  public static void main(String args[]){
    Socket socket=null;
    try{
      socket=new Socket("localhost",8001);
      // send shutdown cmd
      OutputStream socketOut=socket.getOutputStream();
      socketOut.write("shutdown\r\n".getBytes());
 
      // read server response
      BufferedReader br = new BufferedReader(
                                  new InputStreamReader(socket.getInputStream()));
      String msg=null;
      while((msg=br.readLine())!=null)
        System.out.println(msg);
    }catch(IOException e){
      e.printStackTrace();
    }finally{
      try{
        if(socket!=null)socket.close();
      }catch(IOException e){e.printStackTrace();}
    }
  }
}

