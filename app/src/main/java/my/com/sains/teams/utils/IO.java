package my.com.sains.teams.utils;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import my.com.sains.teams.db.MyInspectUpload;

public class IO {

    private static File initDir(){

        File dir = new File(Consts.EXPORT_DIR);

        if (!dir.exists()){

            dir.mkdirs();
        }

        return dir;
    }

    private static File createFIle(File dir){

        DateTime dateTime = new DateTime();
        File file = new File (dir, dateTime.getCurrentDateTime("yyyyMMdd_HHmmss")+ "_export.txt");

        return file;
    }

    public static boolean export(JSONObject jsonObject) throws IOException {

        boolean isExported = false;
        File dir = initDir();
        File file = createFIle(dir);

//        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
//        objectOutputStream.writeObject(jsonObject);
//        objectOutputStream.close();

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(jsonObject.toString());
        bufferedWriter.close();
        isExported = true;

        return isExported;
    }
}
