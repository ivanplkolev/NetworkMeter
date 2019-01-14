package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;

public class Logger {
    static PrintStream p = System.out;

    public static void log(Object o) {
        Thread currentThread = Thread.currentThread();
        StackTraceElement[] stackTraceElements = currentThread.getStackTrace();
        StackTraceElement stackTraceElement = stackTraceElements[stackTraceElements.length - 1];

        p.println(currentThread.getName() + " : " +
                stackTraceElement.getClassName() + " : " +
                stackTraceElement.getMethodName() + " : " + o);
    }


    public static void main(String... args) throws IOException {

//        String pr = ResourceBundle.getBundle("propertes.properties").getString("size1M");

        Properties prop = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("propertes.properties");
        prop.load(stream);


//        utils.Logger.log(pr);
    }


}
