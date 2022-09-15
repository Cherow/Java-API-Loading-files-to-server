package com.kcbgroup.main.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * @ AUTHOR MKOECH
 **/

public class ImageUtils {

    public static byte[] compressImage(byte[] data){
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*102];
        while (!deflater.finished()){
            int size = deflater.deflate(tmp);
            outputStream.write(tmp,0,size);
        }
        try {
            outputStream.close();
        }catch (Exception e){
            e.getMessage();
        }
        return  outputStream.toByteArray();
    }

    public static byte[] decompressImage(byte[] data){
        Inflater inflater = new Inflater();

        inflater.setInput(data);


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*102];
        try {
        while (!inflater.finished()){
            int count = inflater.inflate(tmp);
            outputStream.write(tmp,0,count);
        }

            outputStream.close();
        }catch (Exception e){
            e.getMessage();
        }
        return  outputStream.toByteArray();
    }

}
