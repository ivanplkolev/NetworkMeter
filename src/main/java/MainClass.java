
import aws.BucketManager;
import client.Client;
import multicasting.DeviceScanner;
import multicasting.HeartBeat;
import server.FileServer;

import java.util.Scanner;

public class MainClass {


    public static void main(String... args) throws Exception {
        new FileServer().start();
        new HeartBeat(Client.getMyLocalAdderss()).start();
        new DeviceScanner().start();

        Thread.currentThread().setName("MainThread");
        startConcoleMenu();
    }


    static void startConcoleMenu() throws Exception {
        System.out.println("Hello, what do you want to do:");
        System.out.println("1) Check network speed ");
        System.out.println("2) Check open ports on ip");
        System.out.println("3) Find Ips in local network ");
        System.out.println("4) Find my IP");

        System.out.println("5) Check network speed to specific IP");
        System.out.println("6) Check network speed to S3 Bucket");

        System.out.println("9) Exit");
        Scanner in = new Scanner(System.in);

        int res = in.nextInt();

        switch (res) {
            case 1:
                Client.checkNetworkSpeed();
                break;
            case 2:
                Client.scanIpAddress();
                break;
            case 3:
                Client.findLocalIPs();
                break;
            case 4:
                Client.findMyIP();
                break;
            case 5:
                Client.checkNetworkSpeedSpecificIp();
                break;
            case 6:
                Client.uploadToS3Bucket();
//                BucketManager.createBucket();
                break;
            case 9:
                System.exit(0);
                break;
            default:
                System.out.println("Wrong input -----> Start again...");
        }
        System.exit(0);
//        startConcoleMenu();
    }


}
