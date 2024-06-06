package carrier.Main.Content.Effect;

import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;

public class NDDraw {
    public static void DrawLineTri(float x1,float y1,float x2,float y2,float x3,float y3){
        Lines.line(x1,y1,x2,y2);
        Lines.line(x2,y2,x3,y3);
        Lines.line(x3,y3,x1,y1);
    }
    public static void DrawCenterTriange(float sx,float sy,float lenght,float rotate){
        float[] point = new float[5];
        Vec2 v = new Vec2();
        for(int i = 0;i<3;i++){
            v.trns(rotate+i*120+90, lenght);
            point[2*i]=sx+v.x;
            point[2*i+1]=sy+v.y;
        }
        DrawLineTri(point[0],point[1], point[2], point[3],point[4], point[5]);
    }
    public static void DrawArrowLookCenter(float cx,float cy,float radius,float arrowLenght,float angle,float thick,int arrow){
        final Vec2 v = new Vec2(),v2 = new Vec2();
        Lines.stroke(thick);
        for(int i =0;i<arrow;i++){
            float angleOffSet = i*360f/(float)arrow;
            v.trns(angle + angleOffSet, radius);
            for(int j:Mathf.signs){
                v2.trns(angleOffSet+angle+45f*j, arrowLenght);
                Lines.line(cx+v.x,cy+v.y,cx+v.x+v2.x,cy+v.y+v2.y);
            }
        }
    }
    public static void DrawEllipse(float x, float y,float within,float height,float angle){
        float[] point = new float[]{};
        for (int i = 0; i < 360; i++) {
            double radians1 = Math.toRadians(i);
            double theta1 = Math.toRadians(angle);
            float rx = x + (float)(within * Math.cos(radians1) * Math.cos(theta1) - height * Math.sin(radians1) * Math.sin(theta1));
            float ry = y + (float)(within * Math.cos(radians1) * Math.sin(theta1) + height * Math.sin(radians1) * Math.cos(theta1));
            point[2*i]=rx;
            point[2*i+1]=ry;
        }
        for(int i = 1;i<point.length/2;i++){
            Lines.line(point[2*i],point[2*i+1], point[2*(i+1)],point[2*(i+1)+1]);
        }
    }
    public static void DrawLinesChain(float[] pos){
        int step = (int)(pos.length/2f);
        for(int i=0;i<step-1;i++){
            Lines.line(pos[2*i],pos[2*i+1],pos[2*(i+1)],pos[2*(i+1)+1]);
        }
    }
    /**Argument: x, y, rotation, AngleOffset, stroke, within, height, rotationInside */
    public static void DrawSquareBracketsKeyShape(float[] Arg){
        final Vec2 v1 = new Vec2(),v2 = new Vec2();
        Lines.stroke(Arg[4]);
        for(int i:Mathf.signs){
            v1.trns(Arg[2]+Arg[3]+90*i,Arg[6]);
            v2.trns(Arg[2]+Arg[3]+Arg[7]*i, Arg[5]);
            DrawLinesChain(new float[]{Arg[0],Arg[1],Arg[0]+v1.x,Arg[1]+v1.y,Arg[0]+v1.x+v2.x,Arg[1]+v1.y+v2.y});
        }
    }
    public static void arrow(float x, float y, float width, float length, float backLength, float angle){
        float wx = Angles.trnsx(angle + 90, width), wy = Angles.trnsy(angle + 90, width);
        float ox = Angles.trnsx(angle, backLength), oy = Angles.trnsy(angle, backLength);
        float cx = Angles.trnsx(angle, length) + x, cy = Angles.trnsy(angle, length) + y;
        Fill.tri(x + ox, y + oy, x - wx, y - wy, cx, cy);
        Fill.tri(x + wx, y + wy, x + ox, y + oy, cx, cy);
    }
}
