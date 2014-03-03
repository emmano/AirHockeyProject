package com.jstnow;

import com.jstnow.objects.Mallet;
import com.jstnow.objects.Table;
import com.jstnow.programs.ColorShaderProgram;
import com.jstnow.programs.TextureShaderProgram;
import com.jstnow.util.GLSLSourceLoader;
import com.jstnow.util.LoggerHelper;
import com.jstnow.util.MatrixHelper;
import com.jstnow.util.ShaderHelper;
import com.jstnow.util.TextureHelper;

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



    final private Context mContext;



    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    private Table table;
    private Mallet mallet;
    private TextureShaderProgram textureProgram;
    private ColorShaderProgram colorProgram;
    private int texture;


    public AirHockeyRenderer(Context context) {

        mContext = context;

    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(0f, 0f, 0f, 0f);

        table = new Table();
        mallet = new Mallet();
        textureProgram = new TextureShaderProgram(mContext);
        colorProgram = new ColorShaderProgram(mContext);
        texture = TextureHelper.loadTexture(mContext, R.drawable.air_hockey_surface);
        



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
        // Draw the table.
        textureProgram.useProgram();
        textureProgram.setUniforms(projectionMatrix, texture);
        table.bindData(textureProgram);
        table.draw();
// Draw the mallets.
        colorProgram.useProgram();
        colorProgram.setUniforms(projectionMatrix);
        mallet.bindData(colorProgram);
        mallet.draw();

    }
}
