package com.jstnow.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by emmanuelortiguela on 2/28/14.
 */
public class GLSLSourceLoader {

    public static final String shaderSourceLoder(Context ctx, int resourceId){

        StringBuilder body = new StringBuilder();

        InputStream inputStream = ctx.getResources().openRawResource(resourceId);

        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String sourceLine;

        try {
            while((sourceLine=bufferedReader.readLine())!=null){

                body.append(sourceLine);
                body.append("\n");


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body.toString();

    }

}
