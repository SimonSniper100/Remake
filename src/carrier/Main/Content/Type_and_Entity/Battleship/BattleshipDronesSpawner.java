package carrier.Main.Content.Type_and_Entity.Battleship;

import arc.Events;
import arc.graphics.Color;
import arc.math.Angles;
import arc.struct.FloatSeq;
import arc.struct.IntSeq;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.game.EventType.UnitCreateEvent;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class BattleshipDronesSpawner extends BattleshipTransformer{
    public int DroneSpawn = 20;
    public UnitType droneType;
    public float buildSpeedIfTransform;
    /**spawn X,spawn Y, rotation */
    public FloatSeq postions = new FloatSeq();
    Seq<Unit> drones = new Seq<>(DroneSpawn);
    IntSeq idUnit = new IntSeq(DroneSpawn);
    public float SpawnTime = 20,radius;
    private float Times;
    @Override
    public void update(){
        super.update();
        float mutli = Time.delta*Vars.state.rules.unitBuildSpeedMultiplier*(TransformNow&&buildSpeedIfTransform>0 ? buildSpeedIfTransform : 1);
        if(TransformNow){
            for(int i =0;i<Math.min(idUnit.size,DroneSpawn);){
                int id = idUnit.get(i);
                if(id != -1){
                    Unit u = Groups.unit.getByID(id);
                    drones.add(u);
                    idUnit.set(i,-1);
                }
            }
            int s = 0, prev = drones.size;
            while(s < drones.size){
                Unit u = drones.get(s);
                if(u == null || u.dead || !u.isAdded()){
                    drones.remove(s);
                }else{
                    s++;
                }
            }
            if(drones.size != prev){
                for(int i = 0; i < drones.size; i++){
                    drones.get(i);
                }
            }
            if(drones.size < DroneSpawn){
                Times+=mutli;
                if(Times >= SpawnTime){
                    for(int i =0;i<(int)(postions.size/3);i++){
                        var du= droneType.create(team);
                        float sx=x+Angles.trnsx(rotation,postions.get(3*i+1),postions.get(3*i)),
                            sy=y+Angles.trnsy(rotation,postions.get(3*i+1),postions.get(3*i));
                        du.set(sx,sy);
                        du.rotation=rotation()+postions.get(3*i+2);
                        Events.fire(new UnitCreateEvent(du,null,this));
                        du.add();
                        Fx.spawn.at(du.x,du.y,rotation, Color.white,this);
                        drones.add(du);
                        Times=0f;
                    }
                    
                }
            }
        }
        else{
            Times = 0;
            for(Unit u:drones){
                u.remove();
                u=null;
            }
        }
    }
}
