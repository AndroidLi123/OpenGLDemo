package com.demo.opengl.utils;

import android.content.res.Resources;


import com.demo.opengl.DemoApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ResReadUtils {

    /**
     * 读取资源
     *
     * @param resourceId
     * @return
     */
    public static String readResource(int resourceId) {
        StringBuilder builder = new StringBuilder();
        try {
            InputStream inputStream = DemoApplication.getApplication().getResources().openRawResource(resourceId);
            InputStreamReader streamReader = new InputStreamReader(inputStream);

            BufferedReader bufferedReader = new BufferedReader(streamReader);
            String textLine;
            while ((textLine = bufferedReader.readLine()) != null) {
                builder.append(textLine);
                builder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

}