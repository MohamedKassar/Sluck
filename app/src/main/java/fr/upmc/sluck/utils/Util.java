package fr.upmc.sluck.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import fr.upmc.sluck.Application;
import fr.upmc.sluck.utils.exceptions.UtilException;

/**
 * Created by ktare on 18/03/2018.
 */

public class Util {

    private static final String APPLICATION_PATH = Application.getContext().getFilesDir().getAbsolutePath();
    private static final String CHANNELS_FOLDER_NAME = "Sluck_Channels";
    public static final String CHANNEL_INFO_FILE_NAME = "infos";
    public static final String CHANNELS_FOLDER_PATH = APPLICATION_PATH + File.separator + CHANNELS_FOLDER_NAME;

    static {
        try {
            createFolder(CHANNELS_FOLDER_NAME);
        } catch (UtilException e) {
            e.printStackTrace();
        }
    }

    public static void createChannelFolder(String channelName, List<String> users, String owner) throws UtilException {
        createFolder(CHANNELS_FOLDER_NAME + File.separator + channelName);
        try {
            JSONArray jUsers = new JSONArray();
            users.forEach(jUsers::put);
            eraseAndWriteInFile(channelName, CHANNEL_INFO_FILE_NAME, new JSONObject().put("users", jUsers).put("owner", owner).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void deleteFileIfExists(String channelName, String fileName) throws UtilException {
        File file = new File(CHANNELS_FOLDER_PATH + File.separator + channelName + File.separator + fileName);
        if (!file.exists()) {
            file.delete();
        }
    }

    public static void eraseAndWriteInFile(String channelName, String fileName, String text) throws UtilException {
        try {
            deleteFileIfExists(channelName, fileName);
            File file = createOrGetFile(channelName, fileName);
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(text.getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            throw new UtilException(UtilException.ExceptionType.FILE_WRITE, channelName + "/" + fileName);
        }
    }


    public static String readFile(String channelName, String fileName) throws UtilException {
        try {
            FileInputStream fis = new FileInputStream(new File(CHANNELS_FOLDER_PATH + File.separator + channelName + File.separator + fileName));
            String data = "";
            int temp;
            while ((temp = fis.read()) != -1) {
                data += (char) temp;
            }
            return data;
        } catch (IOException e) {
            throw new UtilException(UtilException.ExceptionType.FILE_READ, channelName + "/" + fileName);
        }
    }

    public static File createOrGetFile(String channelName, String fileName) throws UtilException {
        File file = new File(CHANNELS_FOLDER_PATH + File.separator + channelName + File.separator + fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new UtilException(UtilException.ExceptionType.FILE_CREATION, channelName + "/" + fileName);
            }
        }
        return file;
    }

    private static void createFolder(String name) throws UtilException {
        File folder = new File(APPLICATION_PATH + File.separator + name);
        System.out.println(APPLICATION_PATH + File.separator + name);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        } else {
        }
        if (success) {
        } else {
            throw new UtilException(UtilException.ExceptionType.FOLDER_CREATION, name);
        }
    }

    public static void purgeData() {
        File file = new File(CHANNELS_FOLDER_PATH);
        if (file.exists()) {
            deleteFileOrDirectory(file);
        }
    }

    private static void deleteFileOrDirectory(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteFileOrDirectory(child);

        fileOrDirectory.delete();
    }
}
