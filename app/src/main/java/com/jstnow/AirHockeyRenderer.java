package com.jstnow;

import com.jstnow.util.GLSLSourceLoader;
import com.jstnow.util.LoggerHelper;
import com.jstnow.util.MatrixHelper;
import com.jstnow.util.ShaderHelper;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.orthoM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;
import static android.opengl.Matrix.*;

/**
 * Created by emmanuelortiguela on 2/28/14.
 */
public class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private static final int BYTES_PER_FLOAT = 4;

    private static final int POSITION_ATTR_COUNT =4;

    private static final int COLOR_ATTR_COUNT = 3;

    private static final int STRIDE = (POSITION_ATTR_COUNT + COLOR_ATTR_COUNT) * BYTES_PER_FLOAT;

    private static final String A_POS = "a_Pos";

    private static final String A_COL = "a_Color";

    private static final String U_MAT = "u_Matrix";

    private int uMatrixLocation;

    float[] matrix = new float[16];

    private final float[] projectionMatrix = new float[16];

    private final float[] modelMatrix = new float[16];

    final private Context mContext;

    final private FloatBuffer vertexData;

    final private float[] vertexDataArray = {
            // Order of coordinates: X, Y, Z, W, R, G, B

            // Triangle Fan
            0f,    0f, 0f, 1.5f,   1f,   1f,   1f,
            -0.5f, -0.8f, 0f,   1f, 0.7f, 0.7f, 0.7f,
            0.5f, -0.8f, 0f,   1f, 0.7f, 0.7f, 0.7f,
            0.5f,  0.8f, 0f,   2f, 0.7f, 0.7f, 0.7f,
            -0.5f,  0.8f, 0f,   2f, 0.7f, 0.7f, 0.7f,
            -0.5f, -0.8f, 0f,   1f, 0.7f, 0.7f, 0.7f,

            // Line 1
            -0.5f, 0f, 0f, 1.5f, 1f, 0f, 0f,
            0.5f, 0f, 0f, 1.5f, 1f, 0f, 0f,

            // Mallets
            0f, -0.4f, 0f, 1.25f, 0f, 0f, 1f,
            0f,  0.4f, 0f, 1.75f, 1f, 0f, 0f
    };


    public AirHockeyRenderer(Context context) {

        mContext = context;
        vertexData = ByteBuffer.allocateDirect(vertexDataArray.length * BYTES_PER_FLOAT).order(
                ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(vertexDataArray);

    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(0f, 0f, 0f, 0f);
        final int vertexShaderId = ShaderHelper.compileVertexShader(
                GLSLSourceLoader.shaderSourceLoder(mContext, R.raw.vertex_shader));
        final int fragmentShaderId = ShaderHelper.compileFragmentShader(
                GLSLSourceLoader.shaderSourceLoder(mContext, R.raw.fragment_shader));
        final int programId = ShaderHelper.linkProgram(vertexShaderId, fragmentShaderId);
        glUseProgram(programId);
        if (LoggerHelper.ON) {

            ShaderHelper.validateProgram(programId);
        }

        vertexData.position(0);
        int positionLocation = glGetAttribLocation(programId, A_POS);
        int colorLocation = glGetAttribLocation(programId, A_COL);
        glVertexAttribPointer(positionLocation, POSITION_ATTR_COUNT, GL_FLOAT, false, STRIDE,
                vertexData);
        glEnableVertexAttribArray(positionLocation);
        vertexData.position(POSITION_ATTR_COUNT);
        glVertexAttribPointer(colorLocation, COLOR_ATTR_COUNT, GL_FLOAT, false, STRIDE,
                vertexData);
        glEnableVertexAttribArray(colorLocation);
        uMatrixLocation = glGetUniformLocation(programId, U_MAT);


    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {

        // Set the OpenGL viewport to fill the entire surface.
        glViewport(0, 0, width, height);


        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width
                / (float) height, 1f, 10f);



        setIdentityM(modelMatrix, 0);

        translateM(modelMatrix, 0, 0f, 0f, -2.5f);
        rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);

        final float[] temp = new float[16];
        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);



    }

    @Override
    public void onDrawFrame(GL10 gl10) {

        glClear(GL_COLOR_BUFFER_BIT);
        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

        glDrawArrays(GL_LINES, 6, 2);

        glDrawArrays(GL_POINTS, 8, 1);

        glDrawArrays(GL_POINTS, 9, 1);


    }
}
