precision mediump float;
varying vec2 textureCoordinate;
uniform sampler2D inputTexture;
void main() {

    vec4 color = texture2D(inputTexture, textureCoordinate);
    color.r = 1.0-color.r;
    color.g = 1.0-color.g;
    color. b = 1.0-color.b;
    gl_FragColor = vec4(color.r, color.g, color.b, 1.0);
}