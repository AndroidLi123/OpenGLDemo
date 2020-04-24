precision mediump float;
varying vec2 textureCoordinate;
uniform sampler2D inputTexture;
void main() {

    vec4 color = texture2D(inputTexture, textureCoordinate);
    gl_FragColor = vec4(color.g, color.g, color.g, 1.0);
}