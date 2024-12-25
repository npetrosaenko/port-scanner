import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final int THEARDS = 100;
    private static final int TIMEOUT = 100;
    private static final int MIN_PORT_NUMBER = 0;
    private static final int MAX_PORT_NUMBER = 65535;
    public static void main(String[] args) throws InterruptedException, UnknownHostException {

    scan("housefan.ru");

    }
    private static void scan(String host) throws InterruptedException, UnknownHostException {
        InetAddress ServerAddress;
        try {
            ServerAddress = InetAddress.getByName(host);
            System.out.println("IP-адрес сервера " + host + " = " + ServerAddress.getHostAddress());
        }catch (UnknownHostException e) {}
        System.out.println("Scanning ports:");
        ExecutorService executorService = Executors.newFixedThreadPool(THEARDS);
        for (int port = MIN_PORT_NUMBER; port <= MAX_PORT_NUMBER; port++) {
            final int i  =port;
            executorService.execute(() -> {
                var inetSocketAddress = new InetSocketAddress(host, i);
                try(var socket = new Socket()){
                    socket.connect(inetSocketAddress, TIMEOUT);
                    System.out.println("Host: " + host + ", port " + i + " is opened");
                }catch (IOException e){

                }
            });

        }
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.MINUTES);
        System.out.println("FINISH!");
    }
}