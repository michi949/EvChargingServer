package at.fhooe.mc.server.Logging;

import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;


public class ActionLogger {
    static String fileName = "src/main/java/at/fhooe/mc/server/Logging/ExportFile.txt";

    public static void creatNewFile() {
        String str = "Logging File from Session " + new Date() + ".";
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(str);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void writeLineToFile(String msg){
        String finalMessage = new Date() + " -- : " + msg + "\n";
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.append(finalMessage);
            writer.close();

            System.out.println(finalMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
