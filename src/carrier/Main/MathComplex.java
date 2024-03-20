
package carrier.Main;

import arc.util.Time;
/**
 * Math use for Calulate faster
 */
public class MathComplex {
    public static int[] sign2 = {-2,-1,1,2};
    protected static float result,lstResult;
    protected float results,lstResults;
    public static final double Factorial(double x){
        long j=1;
        if(x<0)return (long)Float.NaN;
        else if(x==0)return 1;
        else for(int i=1; i<=x;i++){
            j*=i;
        }
        return Math.round(j);
    }
    public static final double SubFactorial(int n){
        float z = 0;
        if(n<0)return Double.NaN;
        else if (n == 0 || n == 2) return 1; 
        else if (n == 1) return 0; 
        else for(int i=0;i<=n;i++){
            z+=(Math.pow(-1,i)/Factorial(i));
        }
        return Math.round(Factorial(n)*z);
    }
    public static final double BinomialCoefficients(int n,int k){
        if(k==0||k==n)return 1;
        else if(0<=k&&k<=n)return Math.round(Factorial(n)/(Factorial(k)*Factorial(n-k)));
        else return 0;
    }
    public static final float[] QuaraticFomula(float a,float b,float c){
        float delta = b*b-4*a*c;
        if(delta >0){
            return new float[]{(-b-(float)Math.sqrt(delta))/(2*a),(-b+(float)Math.sqrt(delta))/(2*a)};
        }
        else if(delta == 0){
            return new float[]{-b/(2*a),-b/(2*a)};
        }
        else return new float[]{Float.NaN,Float.NaN};
    }
    public static final float LogBaseAwithB(float a,float b){
        if(a==1||a<0||b<0)return Float.NaN;
        else return (float)(Math.log(b)/Math.log(a));
    }
    public static final float InverseLogBaseAtoB(int a,int b){  
        return 1/LogBaseAwithB(a,b);
    }
    public static final double Tetration(float b,int exponent){
        if (exponent == 1)return b;
        else return Math.round(Math.pow(b,Tetration(b, exponent - 1)));
    }
    public static final double SecDeg(double degree){
        return 1/Math.cos(degree*(Math.PI/180));
    }
    public static final double CscDeg(double degree){
        return 1/Math.sin(degree*(Math.PI/180));
    }
    public static final double Sec(double radian){
        return 1/Math.cos(radian);
    }
    public static final double Csc(double radian){
        return 1/Math.sin(radian);
    }
    public static final double Cot(double radian){
        return 1/Math.tan(radian);
    }
    public static final double CotDeg(double degree){
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
    public static float posy(float y, float length, float angle){
        return y + length *(float)Math.sin((Math.PI * angle)/180);
    }
    public float Diffenals(float after){
        lstResults = results;
        float c = 0;
        while(c<12){
            results = after;
            c+=Time.delta;
        }
        return (results-lstResults)/Time.delta;
    }
    public static float Diffenal(float after){
        lstResult = result;
        float c = 0;
        while(c<12){
            result = after;
            c+=Time.delta;
        }
        return (result-lstResult)/Time.delta;
    }
    public static boolean InRange(float value,float min,float max){
        return  value >= min &&  value<=max;
    }
    public static boolean InRange(float  value){
        return InRange(value, 0, 1);
    }
}

