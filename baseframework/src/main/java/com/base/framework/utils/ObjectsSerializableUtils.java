package com.base.framework.utils;


import com.base.framework.contacts.AppContacts;
import com.base.framework.entity.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Administrator on 2016/9/8.
 */
public class ObjectsSerializableUtils {
    /**
     * 将实体类序列化
     * @param t
     * @param fileName
     * @param <T>
     */
    public static <T> void serializeObj(T t, String fileName) {
        File file = new File(App.getContext().getFilesDir().getAbsolutePath() + "/" + AppContacts.USER_SERIALIZE_NAME);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(t);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 实体类反序列化
     * @param fileName
     * @param <T>
     * @return
     */
    public static <T> T getCacheObj(String fileName) {
        File file = new File(App.getContext().getFilesDir().getAbsolutePath() + "/" + AppContacts.USER_SERIALIZE_NAME);
        try {
            if (file.exists()) {
                FileInputStream fileOutputStream = new FileInputStream(file);
                ObjectInputStream inputStream = new ObjectInputStream(fileOutputStream);
                T object = (T) inputStream.readObject();
                inputStream.close();
                return object;
            } else {
                return null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
