package carrier.Main;

import mindustry.gen.Unit;

public class PhysicMath {
    //TODO make later
    public static final float Gravitational = 6.67430e-11f;
    public static float Velocity(Unit unit){
        return unit.vel.len();
    }
    public static float DistanceA(float time, float Accelation){
        return 0.5f*(Accelation*time*time);
    }
    public final static float DistanceV(float time,float Velocity){
        return time*Velocity;
    }
    public final static float KineticEnergy(float speed,float mass){
        return 0.5f*(mass*speed*speed);
    }
    public final static float Force(float distance,float mass1,float mass2){
        return (Gravitational*mass1*mass2)/(distance*distance);
    }
}
