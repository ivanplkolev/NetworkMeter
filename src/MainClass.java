

import client.Utility;
import server.FileServer;

import java.net.InetAddress;
import java.util.Scanner;

public class MainClass {


    public static void main(String... args) throws Exception {
        new FileServer().start();

        startConcoleMenu();
    }


    static void startConcoleMenu() throws Exception {
        System.out.println("Hello user, what do you want to do:");
        System.out.println("Check network speed - 1");
        System.out.println("Check network speed for specific Ip addr- 2");
        System.out.println("Check open ports in ip - 3");
        System.out.println("Find Ips in local network - 4");
        System.out.println("Find my IP - 5");
        System.out.println("Exit - 9");
        Scanner in = new Scanner(System.in);

        int res = in.nextInt();

        switch (res) {
            case 1:
                Utility.checkNetworkSpeed();
                break;
            case 2:
                checkNetworkSpeedForIP();
                break;
            case 3:
                Utility.scanIpAddress(null);
                break;
            case 4:
                Utility.findLocalIPs();
                break;
            case 5:
                Utility.findMyIP();
                break;
            case 9:
                System.exit(0);
                break;
            default:
                System.out.println("Wrong input -----> Start again");
        }
        startConcoleMenu();
    }

    public static void checkNetworkSpeedForIP() throws Exception {
        System.out.println("Please enter the local IP");

        Scanner in = new Scanner(System.in);
        String res = in.nextLine().trim();

        Utility.checkNetworkSpeed(InetAddress.getByName(res), FileServer.PORT);

    }


}
