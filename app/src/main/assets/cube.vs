#version 300 es

precision mediump float;

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoord;
layout (location = 2) in vec3 aNormal;
layout (location = 3) in vec3 aColor;

out vec3 fragPos;
out vec2 texCoord;
out vec3 normal;
out vec3 vcolor;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    vcolor = aColor;
    texCoord = aTexCoord;
    fragPos = vec3(model * vec4(aPos, 1.0));
    normal = mat3(transpose(inverse(model))) * aNormal;
    gl_Position = projection * view * vec4(fragPos, 1.0);
}