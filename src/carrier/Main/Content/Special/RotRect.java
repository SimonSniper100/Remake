package carrier.Main.Content.Special;

import arc.math.geom.Rect;

public class RotRect extends Rect{
    public float rotation;
    public RotRect(float x,float y,float w,float h,float rot){
        super(x, y, w, h);
        this.rotation = rot;
    }
    public float getRotate(){
        return rotation;
    }
    public Rect set(float x, float y, float width, float height,float rotate){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        if(width == height)this.rotation=0;
        else this.rotation = rotate;
        return this;
    }
}
