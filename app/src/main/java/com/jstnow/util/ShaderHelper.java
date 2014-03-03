package com.jstnow.util;

/**
 * Created by emmanuelortiguela on 2/28/14.
 */

import android.util.Log;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

public class ShaderHelper {


    private static final String TAG = "ShaderHelper";

    private static final int compileVertexShader(String shaderSourceCode) {

        return compileShader(GL_VERTEX_SHADER, shaderSourceCode);
    }

    private static final int compileFragmentShader(String shaderSourceCode) {

        return compileShader(GL_FRAGMENT_SHADER, shaderSourceCode);
    }


    private static final int compileShader(int type, String shaderSoruceCode) {

        final int shaderId = glCreateShader(type);

        final int[] shaderCompileStatus = new int[1];


        glShaderSource(shaderId, shaderSoruceCode);
        glCompileShader(shaderId);
        glGetShaderiv(shaderId, GL_COMPILE_STATUS, shaderCompileStatus, 0);
        if (shaderCompileStatus[0] == 0) {

            if (LoggerHelper.ON) {
                glDeleteShader(shaderId);
                Log.e(TAG,
                        "Error while comping shader. Shader source code: " + "\n" + shaderSoruceCode
                                + "\n" + "::::" + glGetShaderInfoLog(shaderId));


            }

            return 0;

        }

        return shaderId;


    }

    private static final int linkProgram(int vertexShaderId, int fragmentShaderId) {

        int programId = glCreateProgram();

        glAttachShader(programId, vertexShaderId);
        glAttachShader(programId, fragmentShaderId);

        glLinkProgram(programId);

        int[] linkStatus = new int[1];
        glGetProgramiv(programId, GL_LINK_STATUS, linkStatus, 0);

        if (linkStatus[0] == 0) {

            if (LoggerHelper.ON) {

                Log.e(TAG, "Error linking program" + glGetProgramInfoLog(programId));
            }

            return 0;

        }

        return programId;
    }

    public static final boolean validateProgram(int programId) {

        glValidateProgram(programId);
        int[] validateStatus = new int[1];

        glGetProgramiv(programId, GL_VALIDATE_STATUS, validateStatus, 0);

        if (validateStatus[0] == 0) {

            Log.e(TAG, "Validation Error: " + glGetProgramInfoLog(programId));
        }


        return validateStatus[0] != 0;
    }

    public static final int buildProgram(String vertexShaderSource,
                                   String fragmentShaderSource) {
        int program;
    // Compile the shaders.
        int vertexShader = compileVertexShader(vertexShaderSource);
        int fragmentShader = compileFragmentShader(fragmentShaderSource);
    // Link them into a shader program.
        program = linkProgram(vertexShader, fragmentShader);
        if (LoggerHelper.ON) {
            validateProgram(program);
        }
        return program;
    }


}
