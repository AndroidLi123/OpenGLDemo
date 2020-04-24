precision mediump float;
varying vec2 textureCoordinate;
uniform sampler2D inputTexture;
void main() {

    vec4 color = texture2D(inputTexture, textureCoordinate);
    color.b = 0.393* color.r + 0.769 * color.g + 0.189* color.b;
    color.g = 0.349 * color.r + 0.686 * color.g + 0.168 * color.b;
    color.r = 0.272 * color.r + 0.534 * color.g + 0.131 * color.b;
    gl_FragColor = vec4(color.r, color.g, color.b, 1.0);
}