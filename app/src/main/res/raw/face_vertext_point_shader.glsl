#version 300 es
layout (location = 0) in vec2 aPosition;
layout (location = 1) in vec4 aColor;

out vec4 color2frag;

void main(){
    gl_Position = vec4(aPosition,0, 1.0);
    color2frag = aColor;
    gl_PointSize=10.0;
}