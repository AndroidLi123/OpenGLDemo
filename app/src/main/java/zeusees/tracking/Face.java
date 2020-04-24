package zeusees.tracking;


public class Face {
    Face(int x1,int y1,int x2,int y2)
    {
        left= x1;
        top = y1;
        right = x2;
        bottom = y2;
        height = y2-y1;
        width  = x2-x1;
        landmarks = new int[106*2];
    }


    Face(int x1,int y1,int _width,int _height,int[] landmark,int id)
    {
        left= x1;
        top = y1;
        right = x1 + _width;
        bottom = y1 + _height;
        width = _width;
        height  = _height;
        landmarks = landmark;
        ID = id;
    }


    public int ID;

    public int left;
    public int top;
    public int right;
    public int bottom;
    public int height;
    public int width;
    public int[] landmarks;


}
