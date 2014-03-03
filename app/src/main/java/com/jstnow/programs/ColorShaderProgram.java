package com.jstnow.programs;

import android.content.Context;
import android.graphics.Shader;

import com.jstnow.R;
import static android.opengl.GLES20.*;

/**
 * Created by emmanuel.ortiguela on 3/3/14.
 */
public class ColorShaderProgram extends ShaderProgram  {
    // Uniform locations
    private final int uMatrixLocation;
    // Attribute locations
    private final int aPositionLocation;
    private final int aColorLocation;

    public ColorShaderProgram(Context context) {
        super(context, R.raw.vertex_shader,R.raw.fragment_shader);
        uMatrixLocation = glGetUniformLocation(program,U_MATRIX);
        aPositionLocation = glGetAttribLocation(program,A_POSITION);
        aColorLocation = glGetAttribLocation(program,A_COLOR);



    }


    public void setUniforms(float[] matrix) {
// Pass the matrix into the shader program.
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }
    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
    public int getColorAttributeLocation() {
        return aColorLocation;
    }
}
