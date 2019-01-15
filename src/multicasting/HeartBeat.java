package multicasting;

import utils.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class HeartBeat extends Thread {

    static final int port = 5_000;
    static final int pulse = 2_000;
    static final String multicastIp = "225.4.5.6";
    public final String myAddress;

    public void run() {
        try {
            MulticastSocket socket = new MulticastSocket();
            DatagramPacket packet;
            InetAddress address = InetAddress.getByName(multicastIp);
            socket.joinGroup(address);

            while (true) {
                packet = new DatagramPacket(myAddress.getBytes(), myAddress.length(), address, port);
                socket.send(packet);
                Thread.sleep(pulse);
            }
        } catch (IOException | InterruptedException e) {
            Logger.log(e.getMessage());
            Logger.log("Unable to send HeartBeat");
            System.exit(1);
        }
    }

    public HeartBeat(InetAddress myAddress) {
        this.myAddress = myAddress.getHostAddress() + " " + myAddress.getHostName();
        setName("HeartBeatThread");
    }

}
