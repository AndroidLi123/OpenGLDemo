#version 300 es

precision mediump float;

layout(location = 0) out vec4 fragColor;

in vec2 texCoord;
in vec3 fragPos;
in vec3 normal;
in vec3 vcolor;

uniform sampler2D imageTex;
uniform sampler2D normalTex;
uniform vec3 lightDirection;
uniform vec3 viewPos;
uniform vec3 lightColor;
uniform vec3 objectColor;

float myPow(float x, int r) {
    float result = 1.0;
    for (int i = 0; i < r; i = i + 1) {
        result = result * x;
    }
    return result;
}

void main()
{

    vec3 texColor = vcolor;

    // ambient
    float ambientStrength = 0.5;
    vec3 ambient = ambientStrength * lightColor;


    // diffuse
    vec3 norm = normalize(normal);
    vec3 lightDir = normalize(-lightDirection);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * lightColor;

    // specular
    float specularStrength = 2.0;
    vec3 viewDir = normalize(viewPos - fragPos);
    vec3 reflectDir = reflect(lightDir, norm);
    float spec = myPow(max(dot(viewDir, reflectDir), 0.0), 16);
    vec3 specular = specularStrength * spec * lightColor;

    vec3 result = (ambient +diffuse+specular) * texColor;
    fragColor = vec4(result, 1.0);
}