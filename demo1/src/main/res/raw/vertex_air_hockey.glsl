#version 300 es
layout(location=0) in vec4 a_Position;
uniform vec4  u_Color;
out vec4 vColor;
void main()
{
    gl_Position = a_Position;
    gl_PointSize = 10.0;
    vColor = u_Color;
}
