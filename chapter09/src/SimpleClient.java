import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;
public class SimpleClient {
  public void receive()throws Exception{
    Socket socket = new Socket("localhost",8000);
    InputStream in=socket.getInputStream();
    byte[] buffer= ByteBuffer.allocate(16).array();
    

    System.out.println(in.read(buffer));
    for(int i=0;i< 15;i++) {
        System.out.println(Byte.toString(buffer[i]));
    }
    byteToInt2(buffer, 0);
    byteToInt2(buffer, 4);
    byteToInt2(buffer, 8);
    byteToInt2(buffer, 12);
    in.close();
    socket.close();
  }
  
  public static int byteToInt2(byte[] b, int index) {

      int mask = 0xff;
      int temp = 0;
      int n = 0;
      for (int i = 0; i < 4; i++) {
          n <<= 8;
          temp = b[i+index] & mask;
          n |= temp;
      }
      System.out.println(n);
      return n;
  }
  public static void main(String args[])throws Exception {
    new SimpleClient().receive(); 
  }
}
//doc.api.java
/*
An ObjectInputStream deserializes primitive data and objects previously written using an ObjectOutputStream. 

ObjectOutputStream and ObjectInputStream can provide an application with persistent storage for graphs of objects
 when used with a FileOutputStream and FileInputStream respectively. 
 ObjectInputStream is used to recover those objects previously serialized. 
 Other uses include passing objects between hosts using a socket stream or for marshaling
 and unmarshaling arguments and parameters in a remote communication system.
 
  For example to read from a stream as written by the example in ObjectOutputStream: 


        FileInputStream fis = new FileInputStream("t.tmp");
        ObjectInputStream ois = new ObjectInputStream(fis);

        int i = ois.readInt();
        String today = (String) ois.readObject();
        Date date = (Date) ois.readObject();

        ois.close();


*/