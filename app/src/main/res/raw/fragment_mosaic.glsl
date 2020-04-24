precision mediump float;
varying vec2 textureCoordinate;
uniform sampler2D inputTexture;
void main() {
    float rate= 1000.0/1369.0;
    float cellX= 5.0;
    float cellY= 5.0;
    float rowCount=100.0;

    vec2 pos =textureCoordinate;
    pos.x = pos.x*rowCount;
    pos.y = pos.y*rowCount/rate;

    pos = vec2(floor(pos.x/cellX)*cellX/rowCount, floor(pos.y/cellY)*cellY/(rowCount/rate))+ 0.5/rowCount*vec2(cellX, cellY);
    gl_FragColor = texture2D(inputTexture, pos);

}