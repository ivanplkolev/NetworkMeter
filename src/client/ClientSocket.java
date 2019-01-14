package client;


import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

import utils.FileKeeper;

public class ClientSocket {

    static int BUFFER_SIZE = 100;

    public static long sendFile(File file, InetAddress serverAddress, int port) throws Exception {

        Socket socket = new Socket(serverAddress, port);
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());


        oos.writeObject(file.getName());

        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[BUFFER_SIZE];
        Integer bytesRead = 0;

        long startTime = System.currentTimeMillis();
        while ((bytesRead = fis.read(buffer)) > 0) {
            oos.writeObject(bytesRead);
            oos.writeObject(Arrays.copyOf(buffer, buffer.length));
        }

        oos.close();
        ois.close();
        long timeToSend = System.currentTimeMillis() - startTime;

        return timeToSend;
    }

    public static void main(String... args) throws Exception {
        sendFile(FileKeeper.getFile(FileKeeper.FileSize.MB_1),
                InetAddress.getByName("localhost"),
                3332);
    }

}
