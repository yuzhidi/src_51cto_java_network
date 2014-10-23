package multithread3;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.*;

public class EchoServer {
    private int port = 8000;
    private ServerSocket mServerSocket;
    private SendHandler mSendHandler;
    // doc.api.java
    /*
     * An Executor that provides methods to manage termination and methods that
     * can produce a Future for tracking progress of one or more asynchronous
     * tasks. An ExecutorService can be shut down, which will cause it to reject
     * new tasks.
     */
    private ExecutorService executorService; // / thread pool
    private final int POOL_SIZE = 4; // single CPU working thread number in
                                     // thread pool

    public EchoServer() throws IOException {
        mServerSocket = new ServerSocket(port);
        // Runtime availableProcessors() return system CPU number
        // the more CPU, the more work thread in thread pool
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
                .availableProcessors() * POOL_SIZE);

        System.out.println("server launch");
    }

    public void service() {
        while (true) {
            Socket socket = null;
            try {
                System.out.println("Main Thread id: "
                        + Thread.currentThread().getId());
                socket = mServerSocket.accept();
                // doc.api.java:Executes the given command at some time in the
                // future.
                if (mSendHandler == null) {
                    mSendHandler = new SendHandler();
                    executorService.execute(mSendHandler);
                }
                // TODO as test, so multi-thread use one sender.
                executorService
                        .execute(new ListenHandler(socket, mSendHandler));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]) throws IOException {
        new EchoServer().service();
    }
}

class SendHandler implements Runnable {
    ArrayList<PrintWriter> mSoclist = new ArrayList<PrintWriter>();

    public void addSocket(PrintWriter s) {
        if (mSoclist.contains(s)) {
            return;
        }
        mSoclist.add(s);
    }

    public void removeSocket(PrintWriter s) {
        mSoclist.remove(s);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("Send thread is die!!!");
                break;
            }

            if (mSoclist.size() == 0) {
                continue;
            }
            System.out.println("Send thread send message");
            for (PrintWriter pw : mSoclist) {
                pw.println("test sender");
            }
        }

    }

}

class ListenHandler implements Runnable {
    private Socket socket;
    private SendHandler mSendHandler;
    private PrintWriter mPw;

    public ListenHandler(Socket socket, SendHandler sender) {
        this.socket = socket;
        this.mSendHandler = sender;
    }

    private PrintWriter getWriter(Socket socket) throws IOException {
        OutputStream socketOut = socket.getOutputStream();
        return new PrintWriter(socketOut, true);
    }

    private BufferedReader getReader(Socket socket) throws IOException {
        InputStream socketIn = socket.getInputStream();
        return new BufferedReader(new InputStreamReader(socketIn));
    }

    public String echo(String msg) {
        return "echo:" + msg;
    }

    public void run() {
        try {
            System.out.println("SubThread id: "
                    + Thread.currentThread().getId());
            System.out.println("New connection accepted "
                    + socket.getInetAddress() + ":" + socket.getPort());
            BufferedReader br = getReader(socket);
            mPw = getWriter(socket);

            mSendHandler.addSocket(mPw);

            mPw.println("Hello, this message is from server!");

            // user input
            // BufferedReader localReader = new BufferedReader(
            // new InputStreamReader(System.in));
            String msg = null;

            // 虽然我想在这里处理命令行的输入，但是，一个地方即处理网络输入，又处理cmd输入是不对的。
            while ((msg = br.readLine()) != null) {

                mPw.println(msg);
                System.out.println("msg");

                if (msg.equals("bye")) {
                    break;
                }
            }
            // String msg = null;
            // while ((msg = br.readLine()) != null) {
            // System.out.println(msg);
            // pw.println(echo(msg));
            // if (msg.equals("bye"))
            // break;
            // }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    mSendHandler.removeSocket(mPw);
                    System.out.println("Thread id: "
                            + Thread.currentThread().getId() + " socket close");
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
// doc.api.java
/*
 * Usage Examples Here is a sketch of a network service in which threads in a
 * thread pool service incoming requests. It uses the preconfigured
 * Executors.newFixedThreadPool(int) factory method: class NetworkService
 * implements Runnable { private final ServerSocket serverSocket; private final
 * ExecutorService pool;
 * 
 * public NetworkService(int port, int poolSize) throws IOException {
 * serverSocket = new ServerSocket(port); pool =
 * Executors.newFixedThreadPool(poolSize); }
 * 
 * public void run() { // run the service try { for (;;) { pool.execute(new
 * Handler(serverSocket.accept())); } } catch (IOException ex) {
 * pool.shutdown(); } } }
 * 
 * class Handler implements Runnable { private final Socket socket;
 * Handler(Socket socket) { this.socket = socket; } public void run() { // read
 * and service request on socket } }
 * 
 * The following method shuts down an ExecutorService in two phases, first by
 * calling shutdown to reject incoming tasks, and then calling shutdownNow, if
 * necessary, to cancel any lingering tasks: void
 * shutdownAndAwaitTermination(ExecutorService pool) { pool.shutdown(); //
 * Disable new tasks from being submitted try { // Wait a while for existing
 * tasks to terminate if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
 * pool.shutdownNow(); // Cancel currently executing tasks // Wait a while for
 * tasks to respond to being cancelled if (!pool.awaitTermination(60,
 * TimeUnit.SECONDS)) System.err.println("Pool did not terminate"); } } catch
 * (InterruptedException ie) { // (Re-)Cancel if current thread also interrupted
 * pool.shutdownNow(); // Preserve interrupt status
 * Thread.currentThread().interrupt(); } }
 * 
 * Memory consistency effects: Actions in a thread prior to the submission of a
 * Runnable or Callable task to an ExecutorService happen-before any actions
 * taken by that task, which in turn happen-before the result is retrieved via
 * Future.get().
 * 
 * 
 * 
 * Since: 1.5
 * 
 * ------------------------------------------------------------------------------
 * --
 */