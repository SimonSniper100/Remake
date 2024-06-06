package carrier.Main.Content.Math;

import arc.math.Mathf;

public class Interpolate {
    public static float Interpolate1(float x) {
        x = Mathf.clamp(x);
        if (x >= 0 && x < 0.1) {
            return (float)(175.16138412329167 * Math.pow(x, 3) + 1.2752137249200349e-56 * Math.pow(x, 2) - 9.751613841232917 * x + 1);
        } else if (x >= 0.1 && x < 0.2) {
            return (float)(-175.80692061645829 * Math.pow(x, 3) + 105.29049142192498 * Math.pow(x, 2) - 20.280662983425415 * x + 1.3509683047397498);
        } else if (x >= 0.2 && x < 0.3) {
            return (float)(28.066298342541437 * Math.pow(x, 3) - 17.033439953474847 * Math.pow(x, 2) + 4.184123291654551 * x - 0.2800174469322477);
        } else if (x >= 0.3 && x < 0.4) {
            return (float)(-36.458272753707476 * Math.pow(x, 3) + 41.03867403314917 * Math.pow(x, 2) - 13.237510904332655 * x + 1.4621459726664727);
        } else if (x >= 0.4 && x < 0.5) {
            return (float)(17.766792672288457 * Math.pow(x, 3) - 24.031404478045943 * Math.pow(x, 2) + 12.79052050014539 * x - 2.0082582145972667);
        } else if (x >= 0.5 && x < 0.6) {
            return (float)(-34.60889793544635 * Math.pow(x, 3) + 54.532131433556266 * Math.pow(x, 2) - 26.491247455655714 * x + 4.538703111369585);
        } else if (x >= 0.6 && x < 0.7) {
            return (float)(20.66879906949695 * Math.pow(x, 3) - 44.96772317534167 * Math.pow(x, 2) + 33.20866530968305 * x - 7.401279441698168);
        } else if (x >= 0.7 && x < 0.8) {
            return (float)(1.9337016574585635 * Math.pow(x, 3) - 5.624018610061064 * Math.pow(x, 2) + 5.668072113986624 * x - 0.9751410293690026);
        } else if (x >= 0.8 && x < 0.9) {
            return (float)(1.596394300668799 * Math.pow(x, 3) - 4.81448095376563 * Math.pow(x, 2) + 5.020441988950276 * x - 0.8024396626926432);
        } else if (x >= 0.9 && x <= 1) {
            return (float)(1.6807211398662403 * Math.pow(x, 3) - 5.042163419598721 * Math.pow(x, 2) + 5.225356208200058 * x - 0.8639139284675778);
        } else {
            return 1;
        }
        
    }
    
}
