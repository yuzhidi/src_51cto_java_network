import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;

public class SimpleServer {
    private static final int MAGIC_NUMBER = 0xFFFFFFFF;
    boolean run;

    public void send(Object object) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8000);
        Socket socket = serverSocket.accept();
        OutputStream os = socket.getOutputStream();
//        while (run) {
            System.out.println("server write");
            senddata(os);
//        }
        os.close();
        socket.close();
    }

    static private void setInt(int i, byte result[], int index) {
        // BIG-ENDIAN
        result[index + 0] = (byte) ((i >> 24) & 0xFF);
        result[index + 1] = (byte) ((i >> 16) & 0xFF);
        result[index + 2] = (byte) ((i >> 8) & 0xFF);
        result[index + 3] = (byte) (i & 0xFF);
    }

    private void senddata(OutputStream oos) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        // use 30.542021, 104.069157 to test
        double latitude = 30.542021;
        double longitude = 104.069157;

        try {

            double temp = latitude;
            int high = (int) temp;
            int low = (int) ((temp - high) * 100000000);// 10-8
            System.out.println("senddata() getLatitude:" + temp + ", high:"
                    + high + " ,low:" + low);
            byte[] bytebuffer = buffer.array();
            setInt(high, bytebuffer, 0);
            setInt(low, bytebuffer, 4);

            temp = longitude;
            high = (int) temp;
            low = (int) ((temp - high) * 100000000);// 10-8
            System.out.println("senddata() getLongitude:" + temp + ", high:"
                    + high + " ,low:" + low);
            setInt(high, bytebuffer, 8);
            setInt(low, bytebuffer, 12);
            oos.write(bytebuffer);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("senddata() " + e.getMessage());
        }
    }

    public static void main(String args[]) throws IOException {
        Object object = null;
        if (args.length > 0 && args[0].equals("Date"))
            object = new Date();
        else if (args.length > 0 && args[0].equals("Customer1"))
            object = new Customer1("Tom", "1234");
        else if (args.length > 0 && args[0].equals("Customer2")) {
            Customer2 customer = new Customer2("Tom");
            Order2 order1 = new Order2("number1", customer);
            Order2 order2 = new Order2("number2", customer);
            customer.addOrder(order1);
            customer.addOrder(order2);
            object = customer;
        } else if (args.length > 0 && args[0].equals("Customer3")) {
            object = new Customer3("Tom", "1234");
        } else if (args.length > 0 && args[0].equals("Customer4")) {
            Customer4 customer = new Customer4("Tom");
            Order4 order1 = new Order4("number1", customer);
            Order4 order2 = new Order4("number2", customer);
            customer.addOrder(order1);
            customer.addOrder(order2);
            object = customer;
        } else if (args.length > 0 && args[0].equals("Customer5")) {
            object = new Customer5("Tom", 25);
        } else {
            object = "Hello";
        }
        System.out.println("waiting for sent object:" + object);
        new SimpleServer().send(object);
    }
}
