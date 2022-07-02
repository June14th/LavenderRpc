package com.ws.rpc.common.util;

import java.io.*;

public class CommonUtil {
    public static byte[] getByteArrayByObject(Object o){
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        byte[] bytes = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(o);
            bytes = bos.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(oos!=null){
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(bos!=null){
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    public static Object getObjectByByteArray(byte[] bytes){
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        Object target = null;
        try {
            bis=new ByteArrayInputStream(bytes);
            ois=new ObjectInputStream(bis);
            target = ois.readObject();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (ois!=null){
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bis!=null){
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return target;
    }
}
