#version 300 es
layout (location=0) in vec4 vPosition;
layout (location=2) in vec4 aColor;
out vec4 vColor;
void main() {
    gl_Position=vPosition;
    gl_PointSize=20.0;
    vColor=aColor;
}
