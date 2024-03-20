package carrier.Main;

import java.util.function.DoubleFunction;
import java.util.function.ToDoubleFunction;
public class Derivatives extends MathComplex{
    final static double dx=0.000000004;
    final static float dxf=0.000000004f;
    
    public static DoubleFunction<Double> Function;
    public static ToDoubleFunction<Float> Functionf;
    public static DoubleFunction<Double> Derivative(DoubleFunction<Double> f){
        return (x) -> (f.apply(x + dx) - f.apply(x)) / dx; 
    }
    public static ToDoubleFunction<Float> Derivative(ToDoubleFunction<Float> f){
        return (x) -> (f.applyAsDouble(x + dxf) - f.applyAsDouble(x)) / dxf; 
    }
    public static void main(String... args){
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
        Function = (x)->(Math.pow(x, 2));
        System.out.print(FastDerivative(Function,3));
    }
    public static double FastDerivative(DoubleFunction<Double> func,double number){
        DoubleFunction<Double> devi = Derivative(func);
        return devi.apply(number);
    }
    public static DoubleFunction<Double> DerivativeManyTime(DoubleFunction<Double> func,int time){
        DoubleFunction<Double> derivative = func;
        for (int i = 1; i <= time; i++) {
            derivative = Derivative(derivative);
        }
        return derivative;
    }
}
