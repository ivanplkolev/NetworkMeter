package server;


import utils.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer extends Thread {
    public static final int PORT = 3332;
    public static final int BUFFER_SIZE = 100;

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);

            while (true) {
                Socket s = serverSocket.accept();
                saveFile(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveFile(Socket socket) throws Exception {

        long start = System.currentTimeMillis();
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

        byte[] buffer;
        Integer bytesRead;
        String fileName = (String) ois.readObject();

        File tmpFile = new File(fileName);
        FileOutputStream fos = new FileOutputStream(tmpFile);

        do {
            bytesRead = (Integer) ois.readObject();
            buffer = (byte[]) ois.readObject();
            fos.write(buffer, 0, bytesRead);
        } while (bytesRead == BUFFER_SIZE);

        fos.close();
        long totalTime = System.currentTimeMillis() - start;
        ois.close();
        oos.close();

        Logger.log("Time to download the file: " + totalTime);

        tmpFile.delete();
    }

    public FileServer() {
        setName("FileServerThread");
    }

}