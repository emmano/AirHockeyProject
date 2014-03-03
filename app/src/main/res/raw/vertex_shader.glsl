uniform mat4 u_Matrix;

attribute vec4 a_Pos;
attribute vec4 a_Color;

varying vec4 v_Color;

void main(){

gl_Position = u_Matrix * a_Pos;
v_Color = a_Color;
gl_PointSize=10.0;


}