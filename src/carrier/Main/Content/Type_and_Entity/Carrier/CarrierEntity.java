package carrier.Main.Content.Type_and_Entity.Carrier;

import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Angles;
import arc.struct.IntSeq;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import carrier.Main.Content.AI.DockController;
import carrier.Main.Content.AI.Hangar;
import carrier.Main.Content.Special.EntityRegister;
import carrier.Main.Content.Type_and_Entity.Drone.DroneEntity;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformEntity;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.game.EventType.UnitCreateEvent;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.io.TypeIO;
import mindustry.type.UnitType;

public class CarrierEntity extends TransformEntity{
    public UnitType droneType;
    public float SpawnTime,DefendsTime=7*60;
    public float buildSpeedIfTransform,healSpeedWhenInCarrier;
    public float spawnX=0f,spawnY=0f;
    public float timer=0f,sx,sy,tx,ty,timer2=0f;
    public float ReturnX,ReturnY;
    public int unitsSpawned;
    public Seq<Float> postion = new Seq<>(unitsSpawned*3);
        public Seq<Unit> drone = new Seq<>(unitsSpawned);
        public IntSeq readUnits = new IntSeq(unitsSpawned);
        public CarrierEntity(){
        }
        @Override
        public int classId(){
            return EntityRegister.getID(CarrierEntity.class);
        }
        @Override
        public void update(){
            super.update();
            float mutli = Time.delta*Vars.state.rules.unitBuildSpeedMultiplier*(TransformNow&&buildSpeedIfTransform>0 ? buildSpeedIfTransform : 1);
            unitsSpawned = (int)postion.size/3;
            for(int i = 0;i<Math.min(readUnits.size,unitsSpawned);i++){
                int id = readUnits.get(i);
                if(id != -1){
                    Unit u = Groups.unit.getByID(id);
                    drone.add(u);
                    readUnits.set(i,-1);
                }
            }
            int s = 0, prev = drone.size;
            while(s < drone.size){
                Unit u = drone.get(s);
                if(u == null || u.dead || !u.isAdded()){
                    drone.remove(s);
                }else{
                    s++;
                }
            }
            if(drone.size != prev){
                for(int i = 0; i < drone.size; i++){
                    drone.get(i);
                }
            }
            if(DroneCount()<unitsSpawned){
                timer+=mutli;
                if(timer >= SpawnTime){
                    var du= droneType.create(team);
                    sx=x+Angles.trnsx(rotation,spawnY,spawnX);
                    sy=y+Angles.trnsy(rotation,spawnY,spawnX);
                    du.set(sx,sy);
                    du.rotation=rotation();
                    Events.fire(new UnitCreateEvent(du,null,this));
                    du.add();
                    du.controller(new Hangar());
                    Fx.spawn.at(du.x,du.y,rotation, Color.white,this);
                    drone.add(du);
                    timer=0f;
                    
                }
            }
            for(var du :drone){
                if(du.controller() instanceof DockController d){
                    d.carrier = this;
                }
            }
            
            if(isShooting){
                for(var du : drone){
                    if(du.controller() instanceof Hangar h){
                        h.InCarrier = false;
                    }
                    if(!(du.controller() instanceof DockController)){
                        du.controller(new DockController());
                    }
                    else if(du.controller() instanceof DockController e) {
                        e.carrier = this;
                    }
                    if(du instanceof DroneEntity de){
                        de.invis = false;
                    }
                }
            }
            else {
                for(int k =0;k<drone.size;k++){
                    var unit = drone.get(k);
                    unit.team = team;
                    if(!(unit.controller() instanceof Hangar)){
                        unit.controller(new Hangar());
                    }
                    if(unit.controller() instanceof Hangar hangar){
                        hangar.carrier = this;
                        hangar.x = postion.get(3*k);
                        hangar.y = postion.get(3*k+1);
                        hangar.rotate = postion.get(3*k+2);
                        hangar.rx = ReturnX;
                        hangar.ry = ReturnY;
                        if(hangar.InCarrier){
                            if(unit.healthf()<1){
                                unit.health+=Time.delta*healSpeedWhenInCarrier;
                            }
                        }
                    }
                }
            }
            if(dead && !isValid()){
                for(var unit: drone){
                    unit.kill();
                }
            }
        }
        public int DroneCount(){
            return drone.size;
        }
        public boolean Full(){
            return DroneCount()>unitsSpawned ;
        }
        public boolean IsFit(float x,float y,Unit unit,float fix){
            return (Math.round((x*(fix/100))/(fix+ this.vel.len()))==Math.round((unit.x*(fix/100))/(fix+ this.vel.len()))&&Math.round((y*(fix/100))/(fix+ this.vel.len()))==Math.round((unit.y*(fix/100))/(fix+ this.vel.len())));
        }
        public boolean IsFit(float x,float y,float x2,float y2,float fix){
            return (Math.round((x*(fix/100))/(fix+ this.vel.len()))==Math.round(x2/(fix+ this.vel.len()))&&Math.round((y*(fix/100))/(fix+ this.vel.len()))==Math.round(y2/(fix+ this.vel.len())));
        }
        @Override
        public void draw(){
            super.draw();
            if(!Full()){
                Draw.draw(Layer.groundUnit+0.1f,()->{
                    float
                    cx=x+Angles.trnsx(rotation,spawnY,spawnX),
                    cy=y+Angles.trnsy(rotation,spawnY,spawnX);
                    Drawf.construct(cx,cy,droneType.fullIcon, rotation-90f,timer/SpawnTime , 1f, timer);
                });
            }
            if(isShooting){
                Draw.reset();
            }
            else
            for(var drawUnit : drone){
                if(drawUnit.controller() instanceof Hangar e){
                    if(e.InCarrier){
                        Draw.rect(droneType.fullIcon,e.xp,e.yp,rotation-90-e.rotate);
                        droneType.drawOutline(drawUnit);
                        droneType.drawWeapons(drawUnit);
                        droneType.drawWeaponOutlines(drawUnit);
                    }
                    else Draw.reset();
                }
            }
            
        }
        @Override
        public void writeSync(Writes write){
            super.writeSync(write);
            TypeIO.writeUnitType(write, droneType);
            write.f(healSpeedWhenInCarrier);
            write.f(buildSpeedIfTransform);
            write.l(postion.size);
            for(float s:postion){
                write.f(s);
            }
            write.f(spawnX);
            write.f(spawnY);
            write.b(unitsSpawned);
            write.f(timer);
            write.f(timer2);
            write.f(SpawnTime);
            write.i(drone.size);
            for(var du :drone){
                write.i(du == null ? -1:du.id());
            }
            
        }
        @Override
        public void write(Writes write){
            super.write(write);
            TypeIO.writeUnitType(write, droneType);
            write.f(healSpeedWhenInCarrier);
            write.f(buildSpeedIfTransform);
            write.l(postion.size);
            for(float s:postion){
                write.f(s);
            }
            write.f(spawnX);
            write.f(spawnY);
            write.b(unitsSpawned);
            write.f(timer);
            write.f(timer2);
            write.f(SpawnTime);
            write.i(drone.size);
            for(var du :drone){
                write.i(du == null ? -1:du.id());
            }
            
        }
        @Override
        public void read(Reads read){
            super.read(read);
            droneType=TypeIO.readUnitType(read);
            healSpeedWhenInCarrier = read.f();
            buildSpeedIfTransform =read.f();
            long sizes = read.l();
            for(long e=0l;e<sizes;e++){
                postion.add(read.f());
            }
            spawnX = read.f();
            spawnY = read.f();
            unitsSpawned = read.b();
            timer=read.f();
            timer2=read.f();
            SpawnTime = read.f();
            int size = read.i();
            for(int i =0;i<size;i++){
                readUnits.add(read.i());
            }
        }
        @Override
        public void readSync(Reads read){
            super.readSync(read);
            if(!isLocal()){
                droneType=TypeIO.readUnitType(read);
                healSpeedWhenInCarrier = read.f();
                buildSpeedIfTransform =read.f();
                long sizes = read.l();
                for(long e=0l;e<sizes;e++){
                    postion.add(read.f());
                }
                spawnX = read.f();
                spawnY = read.f();
                droneType=TypeIO.readUnitType(read);
                unitsSpawned = read.b();
                timer=read.f();
                timer2=read.f();
                SpawnTime = read.f();
                int size = read.i();
                for(int i =0;i<size;i++){
                    readUnits.add(read.i());
                }
            }
            else{
                read.i();
                read.f();
                read.b();
            }
        }{
    }
    
}
