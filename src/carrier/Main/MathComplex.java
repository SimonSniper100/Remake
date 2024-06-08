
package carrier.Main;

import org.apache.commons.math3.stat.StatUtils;

import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.math.geom.Vec3;
import arc.struct.Seq;
import arc.util.Time;
/**
 * Math use for Calulate faster
 */
public class MathComplex {
    public static final int[] SignZero = {-1,0,1};
    private static transient float lstResult=0;

    public static float Factorial(float x){
        long j=1;
        if(x<0)return Float.NaN;
        else if(x==0)return 1;
        else for(int i=1; i<=x;i++){
            j*=i;
        }
        return Math.round(j);
    }
    public static double Factorial(double x){
        long j=1;
        if(x<0)return Double.NaN;
        else if(x==0)return 1;
        else for(int i=1; i<=x;i++){
            j*=i;
        }
        return Math.round(j);
    }
    public static double SubFactorial(int n){
        double z = 0;
        if(n<0)return Double.NaN;
        else if (n == 0 || n == 2) return 1; 
        else if (n == 1) return 0; 
        else for(int i=0;i<=n;i++){
            z+=(Math.pow(-1,i)/Factorial(i));
        }
        return Math.round(Factorial(n)*z);
    }
    public static double BinomialCoefficients(int n, int k){
        if(k==0||k==n)return 1;
        else if(InRange(k, 0, n))return Math.round(Factorial(n)/(Factorial(k)*Factorial(n-k)));
        else return 0;
    }
    public static float[] QuaraticFomula(float a, float b, float c){
        float delta = b*b-4*a*c;
        if(delta >0){
            return new float[]{(-b-(float)Math.sqrt(delta))/(2*a),(-b+(float)Math.sqrt(delta))/(2*a)};
        }
        else if(delta == 0){
            return new float[]{-b/(2*a),-b/(2*a)};
        }
        else return new float[]{Float.NaN,Float.NaN};
    }
    public static float LogBaseAwithB(float a, float b){
        if(a==1||a<0||b<0)return Float.NaN;
        else return (float)(Math.log(b)/Math.log(a));
    }
    public static float InverseLogBaseAtoB(float a, float b){
        return 1/LogBaseAwithB(a,b);
    }
    public static double SecDeg(double degree){
        return 1/Math.cos(degree*(Math.PI/180));
    }
    public static double CscDeg(double degree){
        return 1/Math.sin(degree*(Math.PI/180));
    }
    public static double Sec(double radian){
        return 1/Math.cos(radian);
    }
    public static double Csc(double radian){
        return 1/Math.sin(radian);
    }
    public static double Cot(double radian){
        return 1/Math.tan(radian);
    }
    public static double CotDeg(double degree){
        return 1/Math.tan(degree *(Math.PI / 180));
    }
    public static float dx(float px, float r, float angle){
        return px + r * (float) Math.cos(angle * Math.PI/180);
    }
    public static float dy(float py, float r, float angle){
        return py + r * (float) Math.sin(angle * Math.PI/180);
    }
    public static float posx(float x, float length, float angle){
        return x + length * (float)Math.cos((Math.PI * angle)/180);
    }
    public static float posy(float y, float length, float angle) {
        return y + length * (float) Math.sin((Math.PI * angle) / 180);
    }
    public static float DeltaVaule(float after){
        if(Time.delta == 0) return 0;
        final float d = (after - lstResult)/Time.delta;
        lstResult = after;
        return d;
    }
    public static boolean InRange(float value,float min,float max){
        if(min == max) return false;//Nghĩ sao cho nó bằng :V
        return value >= min && value<=max;
    }
    public static boolean InRange(float  value){
        return InRange(value, 0, 1);
    }
    public static Vec2 chainPos(float x,float y,Seq<Float> radius,Seq<Float> angle){
        float size = Math.min(radius.size,angle.size);
        float xp=0,yp=0;
        Vec2 v = new Vec2();
        v.setZero();
        for(int i = 0 ; i<size-1;i++){
            xp += radius.get(i)*Mathf.cos(angle.get(i));
            yp += radius.get(i)*Mathf.sin(angle.get(i));
        }
        v.set(x+xp,y+yp);
        return v;
    }
    public static Vec2 chainPos(float x,float y,Float[] radius,Float[] angle){
        Seq<Float> r = new Seq<>(),ang = new Seq<>();
        return chainPos(x,y,r.add(radius),ang.add(angle));
    }
    public static float PolygoneAngle(int i,int Max){
        return (float) (i * 360) /Max;
    }
    public static float dotProduct(Vec2 v1,Vec2 v2){
        return v1.x*v2.x+v1.y*v2.y;
    }
    public static float dotProduct(Vec3 v1,Vec3 v2){
        return v1.x*v2.x+v1.y*v2.y+v1.z+v2.z;
    }
    public static Vec3 crossProduct(Vec3 v1, Vec3 v2) {
        return new Vec3(v1.y*v2.z-v1.z*v2.y,v1.z*v2.x-v1.x*v2.z,v1.x*v2.y-v1.y*v2.x);
    }
    public static Vec3 crossProduct(Vec2 v1, Vec2 v2){
        return crossProduct(new Vec3(v1,0), new Vec3(v2,0));
    }
    public static float maxAngleDist(float a, float b){
        a = Mathf.mod(a, 360f);
        b = Mathf.mod(b, 360f);
        return Math.max((a - b) < 0 ? a - b + 360 : a - b, (b - a) < 0 ? b - a + 360 : b - a);
    }
    public static float SignedAngleFloat(Vec2 from, Vec2 to)
    {
        float unsigned_angle = Mathf.atan2(to.y - from.y, to.x - from.x) * Mathf.radDeg*2;
        float sign = Mathf.sign(from.x * to.y - from.y * to.x);
        return unsigned_angle * sign;   
    }
    public static int SignedAngle(float angle1, float angle2) {
        angle1 = Mathf.mod(angle1, 360f);
        angle2 = Mathf.mod(angle2, 360f);
        float diff = Mathf.mod(angle2 - angle1, 360f);
        return diff > 180 ? -1 : (diff < -180 ? 1 : Integer.signum((int)diff));
    }
    public static int SignedAngle(Vec2 v1, Vec2 v2) {
        double crossProduct = v1.x * v2.y - v1.y * v2.x;
        if (crossProduct > 0) {
            return 1;
        } else if (crossProduct < 0) {
            return -1;
        } else {
            return 0;
        }
    }
    public static boolean isSameDirection(Vec2 vec1, Vec2 vec2) {
        vec1.nor();
        vec2.nor();
        float dotProduct = vec1.dot(vec2);
        return dotProduct>0;
    }
    public static boolean isOppositeDirection(Vec2 vec1, Vec2 vec2){return !isSameDirection(vec1,vec2);}
    public static float clampAngle(float angle, float min, float max) {
        min = Mathf.mod(min, 360f);
        max = Mathf.mod(max, 360f);
        angle = Mathf.mod(angle, 360f);
        if (min > max) {
            float temp = min;
            min = max;
            max = temp;
        }
        if (angle < min) {
            return min;
        } else if (angle > max) {
            return max;
        } else {
            return angle;
        }
    }
    static public float MoveStaightTowards(float current, float target, float maxDelta){
        if (Math.abs(target - current) <= maxDelta)
            return target;
        return current + Mathf.sign(target - current) * maxDelta;
    }
    public static float Max(float[] values){
        float len = values.length;
        if (len == 0)
            return 0;
        float m = values[0];
        for (int i = 1; i < len; i++){
            if (values[i] > m){
                m = values[i];
           }
        }
        return m;
    }
    public static int Min(int[] values){
        int len = values.length;
        if (len == 0)
            return 0;
        int m = values[0];
        for (int i = 1; i < len; i++){
            if (values[i] < m){
                m = values[i];
           }
        }
        return m;
    }
    public static float Min(float[] values){
        float len = values.length;
        if (len == 0)
            return 0;
        float m = values[0];
        for (int i = 1; i < len; i++){
            if (values[i] < m){
                m = values[i];
           }
        }
        return m;
    }
    public static int Max(int[] values){
        int len = values.length;
        if (len == 0)
            return 0;
        int m = values[0];
        for (int i = 1; i < len; i++){
            if (values[i] > m){
                 m = values[i];
            }
        }
        return m;
    }
    public static float Repeat(float t, float length){
        return Mathf.clamp(t - (float)Math.floor(t / length) * length, 0.0f, length);
    }
    public static float ProgressFadeOutDelay(float main,float percent){
        return main>=percent ? 1f: 1f-((percent-main)/percent);
    }
    public static float ProgressFadeInDelay(float main, float percent) {
        return main>=percent ? 0f: (percent - main) / percent;
    }
    public static float ProgressFadeInDelayReverse(float main, float percent){
        return main<=percent ? (main/percent):1f;
    }
    public static float ProgressFadeOutDelayReverse(float main,float percent){
        return main<=percent ? 1f-(main/percent):0f;
    }
    public static float lerp(float a, float b, float p) {
        return a + Mathf.clamp(p) * (b - a);
    }
    public static int continues(boolean con){
        return con ? 1:0;
    }
    public static float NonNegative(float num){
        return Math.max(num, 0);
    }
    public static int NonNegative(int num){
        return Math.max(num, 0);
    }
    public static String SignDirection(int i){
        return i==-1 ? "Left":"Right";
    }
    public static boolean isPointInEllipse(float px, float py, float ex, float ey, float a, float b, float angle) {
        float cosAngle = Mathf.cosDeg(angle);
        float sinAngle = Mathf.sinDeg(angle);
        float dx = px - ex;
        float dy = py - ey;
        float tx = cosAngle * dx + sinAngle * dy;
        float ty = -sinAngle * dx + cosAngle * dy;
        return ((tx * tx) / (a * a)) + ((ty * ty) / (b * b)) <= 1;
    } 
}

