import java.io.*;
import java.net.*;
import java.util.*;

public class SimpleServer {
    private static final int MAGIC_NUMBER = 0xFFFFFFFF;

    public void send(Object object) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8000);
        Socket socket = serverSocket.accept();
        OutputStream out = socket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        // while (true) {
        System.out.println("server write");
        senddata(oos);
        // // oos.writeObject(object); // first time
        // // oos.writeObject(object); // second time send same object
        // oos.writeInt(4); // leo add
        // // oos.writeInt(17); // leo add
        // // oos.writeInt(256); // leo add
        // // oos.writeInt(1); // leo add
        // oos.writeFloat(1.2f); // leo add
        // oos.writeLong(65536); // leo add
        // oos.writeDouble(65.65); // leo add
        // }
        oos.close();
        socket.close();
    }

    private void senddata(ObjectOutputStream oos) {
        // use 30.542021, 104.069157 to test
        double latitude = 30.542021;
        double longitude = 104.069157;
        try {
            // if (mLocation == null) {
            // Log.e(TAG, "senddata(), location is null, simulation!");
            // } else {
            // latitude = mLocation.getLatitude();
            // longitude = mLocation.getLongitude();
            // }

            oos.writeInt(MAGIC_NUMBER);
            double temp = latitude;
            int high = (int) temp;
            int low = (int) ((temp - high) * 100000000);// 10-8
            System.out.println("senddata() getLatitude:" + temp + ", high:"
                    + high + " ,low:" + low);
            oos.writeInt(high);
            oos.writeInt(low);

            temp = longitude;
            high = (int) temp;
            low = (int) ((temp - high) * 100000000);// 10-8
            System.out.println("senddata() getLongitude:" + temp + ", high:"
                    + high + " ,low:" + low);
            oos.writeInt(high);
            oos.writeInt(low);

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
