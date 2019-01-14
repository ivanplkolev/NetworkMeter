package client;


import server.FileServer;
import utils.FileKeeper;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utility {


    public static void findLocalIPs() {
        System.out.println("In method find reachable local IPs");

        List<InetAddress> rechableAddresses = findReacheblaAddresses();
        InetAddress localAddress = getMyLocalAdderss();
        for (InetAddress inetAddress : rechableAddresses) {
            System.out.println(inetAddress.getHostAddress() + " is reachable" + inetAddress.getHostName());
            if (localAddress.getHostAddress().equals(inetAddress.getHostAddress())) {
                scanIpAddress(inetAddress);
            }

        }
    }

    static List<InetAddress> findReacheblaAddresses() {
        System.out.println("In method findReacheblaAddresses");
        int timeout = 1000;
        String hostPrefix = getMyLocalSubnet() + ".";
        List<InetAddress> reachableAddresses = new ArrayList<>();
        for (int i = 1; i < 255; i++) {
            try {
                InetAddress inetAddress = InetAddress.getByName(hostPrefix + i);
                if (inetAddress.isReachable(timeout)) {
                    reachableAddresses.add(inetAddress);
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        return reachableAddresses;
    }

    public static void findMyIP() {
        System.out.println("In method findMyIP");
        InetAddress localAddress = getMyLocalAdderss();
        if (localAddress != null) {
            System.out.println("IP: " + localAddress.getHostAddress());
            System.out.println("HostName: " + localAddress.getHostName());
        }
    }

    static InetAddress getMyLocalAdderss() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    static String getMyLocalSubnet() {
        System.out.println("In method getMyLocalSubnet");
        InetAddress localAddress = getMyLocalAdderss();
        if (localAddress != null) {
            return extractSubnet(localAddress.getHostAddress());
        }

        return null;
    }


    static String extractSubnet(String ip) {
        return ip.substring(0, ip.lastIndexOf("."));
    }


    public static void checkNetworkSpeed() throws Exception {
        List<InetAddress> rechableAddresses = findReacheblaAddresses();
        System.out.println(rechableAddresses.size() > 0 ? "Please choose between this devices" : "There are no devices in the local network");


        for (int i = 0; i < rechableAddresses.size(); i++) {
            System.out.println(i + ") " + rechableAddresses.get(i).getHostName() + " " + rechableAddresses.get(i).getHostAddress());
        }
        Scanner in = new Scanner(System.in);
        int res = in.nextInt();

        checkNetworkSpeed(rechableAddresses.get(res), FileServer.PORT);

    }


    public static void checkNetworkSpeed(InetAddress addrd, int port) throws Exception {

        if (port == 0) {
            port = FileServer.PORT;
        }

        float timeFor1MB = ClientSocket.sendFile(FileKeeper.getFile(FileKeeper.FileSize.MB_1), addrd, port) / 1000f;
        System.out.println("End results Time for transfer 1MB=" + timeFor1MB + "s which means " + (1 / timeFor1MB) + " MB/s");

        float timeFor10MB = ClientSocket.sendFile(FileKeeper.getFile(FileKeeper.FileSize.MB_10), addrd, port) / 1000f;
        System.out.println("End results Time for transfer 10MB=" + timeFor10MB + " which means " + (1 / timeFor10MB) + " MB/s");

        float timeForZipped = ClientSocket.sendFile(FileKeeper.getZippedFile(), addrd, port) / 1000f;
        float zippingTime = FileKeeper.getTimeForZipping() / 1000f;

        System.out.println("End results Time for transfer zipped" + timeForZipped + " +  " + zippingTime + "  s time for zipping");
        System.out.println("Total Time for unzipped 11MB" + (timeFor1MB + timeFor10MB) +
                " time for zipping and sending" + (timeForZipped + zippingTime));
    }


    public static void scanIpAddress(InetAddress inetAddress) {
        System.out.println("In method scanIpAddress");

        String host = inetAddress != null ? inetAddress.getHostName() : "localhost";
        for (int i = 1; i < 1024; i++) {
            try {
                new Socket(host, i).close();
                System.out.println("There is a server on port " + i + " of " + host);
            } catch (UnknownHostException ex) {
                System.err.println(ex);
                break;
            } catch (IOException ex) {
                // must not be a server on this port
            }
        }
    }


}
