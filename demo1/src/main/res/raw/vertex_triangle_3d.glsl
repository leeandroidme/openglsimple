#version 300 es
uniform mat4 u_Matrix;
layout(location=0) in vec4 a_Position;
layout(location=1) in vec4 u_Color;
out vec4 vColor;
void main()
{
    gl_Position = a_Position * u_Matrix;
    gl_PointSize = 10.0;
    vColor = u_Color;
}
