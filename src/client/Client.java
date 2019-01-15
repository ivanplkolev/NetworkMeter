package client;


import multicasting.DeviceScanner;
import server.FileServer;
import utils.FileKeeper;
import utils.FileSize;
import utils.Logger;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Client {

    private static int BUFFER_SIZE = 100;


    public static void checkNetworkSpeed() throws Exception {

        System.out.println("Please choose device to scan");

        List<String> activeDevices = DeviceScanner.getActiveDevices();

        for (int i = 0; i < activeDevices.size(); i++) {
            System.out.println(i + "   " + activeDevices.get(i));
        }

        Scanner in = new Scanner(System.in);
        int res = in.nextInt();
        String[] choosedAddres = activeDevices.get(res).split(" ");

        checkNetworkSpeed(InetAddress.getByName(choosedAddres[0]), FileServer.PORT);
    }

    private static void checkNetworkSpeed(InetAddress addrd, int port) throws Exception {

        float timeFor1MB = sendFile(FileKeeper.getFile(FileSize.MB_1), addrd, port) / 1000f;
        System.out.println("End results Time for transfer 1MB= " + timeFor1MB + "s which means " + (1 / timeFor1MB) + "MB/s.");

        float timeFor10MB = sendFile(FileKeeper.getFile(FileSize.MB_10), addrd, port) / 1000f;
        System.out.println("End results Time for transfer 10MB= " + timeFor10MB + " which means " + (1 / timeFor10MB) + "MB/s.");

        float timeForZipped = sendFile(FileKeeper.getZippedFile(), addrd, port) / 1000f;
        float zippingTime = FileKeeper.getTimeForZipping() / 1000f;

        System.out.println("End results Time for transfer zipped " + timeForZipped + "s +  " + zippingTime + "s time for zipping");
        System.out.println("Total Time for unzipped 11MB= " + (timeFor1MB + timeFor10MB) +
                "s time for zipping and sending" + (timeForZipped + zippingTime) + "s.");
    }

    private static long sendFile(File file, InetAddress serverAddress, int port) throws Exception {

        Socket socket = new Socket(serverAddress, port);
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

        oos.writeObject(file.getName());

        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;

        long startTime = System.currentTimeMillis();
        while ((bytesRead = fis.read(buffer)) > 0) {
            oos.writeObject(bytesRead);
            oos.writeObject(Arrays.copyOf(buffer, buffer.length));
        }

        oos.close();
        ois.close();

        return System.currentTimeMillis() - startTime;
    }

    public static void scanIpAddress() {
        System.out.println("In method scanIpAddress, please enter address:");
        Scanner scanner = new Scanner(System.in);
        String host = scanner.nextLine().trim();

        for (int i = 1; i < 65535; i++) {
            try {
                new Socket(host, i).close();
                System.out.println("There is a server on port " + i + " of " + host);
            } catch (UnknownHostException ex) {
                Logger.log(ex);
                Logger.log("Not a valid address entered");
                System.exit(1);
            } catch (IOException ex) {
                // must not be a server on this port
            }
        }
    }

    public static void findLocalIPs() {
        int timeout = 1000;
        String hostPrefix = getMyLocalSubnet() + ".";
        for (int i = 1; i < 255; i++) {
            try {
                InetAddress inetAddress = InetAddress.getByName(hostPrefix + i);
                if (inetAddress.isReachable(timeout)) {
                    System.out.println(inetAddress.getHostName() + " is reachable (" + inetAddress.getHostAddress()+")");
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    private static String getMyLocalSubnet() {
        System.out.println("In method getMyLocalSubnet");
        InetAddress localAddress = getMyLocalAdderss();
        if (localAddress != null) {
            return extractSubnet(localAddress.getHostAddress());
        }

        return null;
    }

    public static void findMyIP() {
        System.out.println("In method findMyIP");
        InetAddress localAddress = getMyLocalAdderss();
        if (localAddress != null) {
            System.out.println("IP: " + localAddress.getHostAddress());
            System.out.println("HostName: " + localAddress.getHostName());
        }
    }

    public static InetAddress getMyLocalAdderss() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
            Logger.log("Can not find my IP address");
            System.exit(1);
        }
        return null;
    }


    private static String extractSubnet(String ip) {
        return ip.substring(0, ip.lastIndexOf("."));
    }
}
