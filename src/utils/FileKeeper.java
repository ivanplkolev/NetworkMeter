package utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileKeeper {

    private static final String textPrefix = "Hello I am file with size- ";
    private static final String outputZipFileName = "outZipFile.zip";
    public static final String outnputDir = "outnputDir";


//    public static void main(String... args) {
//
//
//        prepareEnviroment();
//
////
//        getFile(FileSize.MB_10);
//        Logger.log("... Successfully completed");
//    }

    static void prepareEnviroment() {

        File outnputDirFile = new File(outnputDir);

        //clear input and ouput dirs if existing from previous runs
        clearDir(outnputDirFile);

        //creates the destination directories
        outnputDirFile.mkdirs();

        //create the text files
        for (FileSize fileSize : FileSize.values()) {
            createTextFile(fileSize, outnputDir);
        }

        //zips the 3 text files in 1 archive
        zipAllFilesInDir(outnputDir, outputZipFileName, outnputDir);
    }

    public static void clearDir(File file) {
        if (!file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            if (file.list().length == 0) {
                file.delete();
            } else {
                String files[] = file.list();
                for (String temp : files) {
                    File fileDelete = new File(file, temp);
                    clearDir(fileDelete);
                }
                if (file.list().length == 0) {
                    file.delete();
                }
            }
        } else {
            file.delete();
        }
    }

    private static void createTextFile(FileSize fileSize, String dir) {
        String textSource = textPrefix + fileSize.name();
        File file = new File(dir, fileSize.getFileName());

        int fileSizeInBytes = fileSize.getBytes();

        while (file.length() < fileSizeInBytes) {
            try (Writer w = new BufferedWriter(new FileWriter(file, true))) {
                w.write(textSource);
                w.write("\n");
            } catch (FileNotFoundException e) {
                Logger.log("Couldn't create " + fileSize.getFileName() + ".");
                System.exit(1);
            } catch (IOException xe) {
                Logger.log("IO Exception " + fileSize.getFileName() + ".");
                System.exit(1);
            }
        }
        Logger.log("Created file " + fileSize.getFileName());
    }

    private static long zipAllFilesInDir(String dir, String archiveName, String destination) {
        long startZippingTime = System.currentTimeMillis();

        try {
            byte[] b;
            FileOutputStream fout = new FileOutputStream(new File(destination, archiveName));
            ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(fout));

            File[] files = new File(dir).listFiles();
            for (int i = 0; i < files.length; i++) {
                if (!files[i].getName().replaceAll("^.*\\.(.*)$", "$1").equals("txt")) {
                    Logger.log("Skipped file " + files[i].getName());
                    continue;
                }
                b = new byte[1024];
                FileInputStream fin = new FileInputStream(files[i]);
                zout.putNextEntry(new ZipEntry(files[i].getName()));
                int length;
                while ((length = fin.read(b, 0, 1024)) > 0) {
                    zout.write(b, 0, length);
                }
                zout.closeEntry();
                fin.close();
            }
            zout.close();
        } catch (IOException e) {
            Logger.log("Exception in Zipping all files in dir " + e.getMessage());
            System.exit(0);
        }

        return System.currentTimeMillis() - startZippingTime;
    }


    public static File getFile(FileSize fileSize) {
        File file = new File(outnputDir, fileSize.getFileName());
        if (!file.exists()) {
            prepareEnviroment();
        }
        int fileSizeInBytes = fileSize.getBytes();
        while (file.length() < fileSizeInBytes) {
            Logger.log("File is still not readyto be sent");
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        return file;
    }

    public static File getZippedFile() {
        File file = new File(outnputDir, outputZipFileName);
        if (!file.exists()) {
            prepareEnviroment();
        }
        return file;
    }


    public static int getFileSize(File file) {
        return (int) (file.length() / (1024 * 1024));
    }

    public static long getTimeForZipping() {
        return zipAllFilesInDir(outnputDir, outputZipFileName, outnputDir);
    }


}

