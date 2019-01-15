package multicasting;

import utils.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceScanner extends Thread {

    static Map<String, Long> addresses = new HashMap<>();

    public void run() {
        try {
            MulticastSocket socket = new MulticastSocket(HeartBeat.port);
            socket.joinGroup(InetAddress.getByName(HeartBeat.multicastIp));
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            while (true) {
                socket.receive(packet);
                String str = new String(packet.getData());
                addresses.put(str, System.currentTimeMillis());
//                Logger.log("Received from" + packet.getAddress() + " Is : " + str);
            }
        } catch (IOException e) {
            Logger.log(e.getMessage());
            Logger.log("Unable to find devices");
            System.exit(1);
        }
    }

    public DeviceScanner() {
        setName("DeviceScannerThread");
    }

    public static List<String> getActiveDevices() {
        List<String> active = new ArrayList<>();
        long now = System.currentTimeMillis();
        for (Map.Entry<String, Long> addr : addresses.entrySet()) {
            if (now - addr.getValue() > 5_000) {
                continue;
            }
            active.add(addr.getKey());
        }
        return active;
    }
}
