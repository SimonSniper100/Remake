package carrier.Main;

import arc.func.Cons;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.FloatSeq;
import arc.struct.IntSet;
import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import carrier.Main.Content.Ability.Skill;
import carrier.Main.Content.Block.Type.PlanetaryWeaponTurret;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformEntity;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformFlying;
import mindustry.core.World;
import mindustry.entities.Units;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Healthc;
import mindustry.gen.Unit;
import mindustry.type.Weapon;
import mindustry.ui.Styles;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatValue;

import static mindustry.Vars.*;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2DFormat;
import org.apache.commons.math3.linear.AnyMatrix;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class CarrierVars {
    public static Stat SkillStat= new Stat("Skills");
    public static final int MaxBlockSize = 160;
    @SuppressWarnings("unchecked")
    public static final ObjectSet<PlanetaryWeaponTurret.PlanetaryWeaponTurretBulid>[] placedPlanetaryWeapons = new ObjectSet[Team.all.length];
    public static Color thurmixRed = Color.valueOf("#FF9492"),
		thurmixRedLight = Color.valueOf("#FFCED0"),
		thurmixRedDark = thurmixRed.cpy().lerp(Color.black, 0.9f);
    public boolean isTransformType,isTransform;
    static{
		for(int i = 0; i < Team.all.length; i++){
			CarrierVars.placedPlanetaryWeapons[i] = new ObjectSet<>(i < 6 ? 20 : 1);
		}
	}
    public static Weapon copyMove(Weapon w,float x,float y){
        return copyMove(w,x,y,true);
    }
    public static Weapon copyMove(Weapon w,float x,float y,boolean display){
        Weapon n = w.copy();
        n.x=x;
        n.y=y;
        n.display = display;
        return n;
    }
    public static Weapon MoveModifed(Weapon w, float x,float y,Cons<Weapon> mod){
        Weapon n = w.copy();
        n.x=x;
        n.y=y;
        mod.get(n);
        return n;
    }
    public static Seq<Weapon> WeaponChainAdd(Weapon weapon,FloatSeq postions,boolean display){
        Seq<Weapon> chainAdd = new Seq<>();
        for(int i = 0;i<(int)(postions.size/2);i++){
            float x = postions.get(2*i),y = postions.get(2*i+1);
            Weapon c = weapon.copy();
            c.display = i==0;
            c.x = x;
            c.y = y;
            c.display = false;
            chainAdd.addAll(c);
        }
        chainAdd.get(0).display = display;
        return chainAdd;
    }
    //Convert Float[] to Seq<Float>
    public static Seq<Weapon> WeaponChainAdd(Weapon weapon, float[] postions,boolean display){
        FloatSeq pos = new FloatSeq();
        pos.addAll(postions);
        return WeaponChainAdd(weapon,pos,display);
    }
    public static float MinFloatArray(float[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Array is null or empty");
        }
        float min = arr[0]; 
        for (int i = 1; i < arr.length; i++) {
            min = Math.min(min, arr[i]);
        }
        return min;
    }
    public static Seq<Weapon> CombineWeaponArray(@SuppressWarnings("unchecked") Seq<Weapon> ... weaponsadd){
        Seq<Weapon> something = new Seq<Weapon>();
        for(Seq<Weapon> w:weaponsadd){
            something.add(w);
        }
        return something;
    }
    public static float MaxFloatArray(float[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Array is null or empty");
        }
        float max = arr[0]; 
        for (int i = 1; i < arr.length; i++) {
            max = Math.min(max, arr[i]);
        }
        return max;
    }
    public static boolean IsTransformUnitAndTransforming(Unit u){
        if(u instanceof TransformEntity s){
            return s.TransformNow;
        }
        else if (u instanceof TransformFlying f){
            return f.TransformNow;
        }
        //Not Transform Type
        else return false;
    }
    public static Seq<Weapon> WeaponChainAddAndChangeType(Seq<Weapon> weaponType,Seq<Float> postions,Seq<Integer> changeVaule,Seq<Boolean> display){
        int v = Math.min(changeVaule.size,(int)(postions.size/2)); 
        Seq<Weapon> chainAdd = new Seq<>();
        for(int i = 0 ; i<v-1;i++){
            Weapon w = weaponType.get(changeVaule.get(i)).copy();
            float x = postions.get(2*i),y = postions.get(2*i+1);
            w.x=x;
            w.y=y;
            w.display = display.get(i);
            chainAdd.addAll(w);
        }
        return chainAdd;
    }
    public static Seq<Weapon> WeaponChainAddAndChangeType(Weapon[] weaponType,Float[] postions,Integer[] changeVaule,Boolean[] display){
        Seq<Float> pos = new Seq<>();
        Seq<Weapon> wep = new Seq<>();
        Seq<Integer> value = new Seq<>();
        Seq<Boolean> dis = new Seq<>();
        return WeaponChainAddAndChangeType(wep.add(weaponType),pos.add(postions),value.add(changeVaule),dis.add(display));
    }
    public static Seq<Weapon> WeaponChainAddAndMirror(Weapon weapon, Seq<Float> postions,boolean display){
        Seq<Weapon> chainAdd = new Seq<>();
        for(int i = 0;i<(int)(postions.size/2);i++){
            float x = postions.get(2*i),y = postions.get(2*i+1);
            float xFlip = -postions.get(2*i);
            Weapon c = weapon.copy();
            c.display = i==0;
            c.x = x;
            c.y = y;
            c.display = false;
            chainAdd.add(c);
            c.x=xFlip;
            chainAdd.add(c);
        }
        chainAdd.get(0).display = display;
        return chainAdd;
    }
    public static Seq<Weapon> WeaponChainAddAndMirror(Weapon weapon, Float[] postions,boolean display){
        Seq<Float> pos = new Seq<>();
        pos.add(postions);
        return WeaponChainAddAndMirror(weapon,pos,display);
    }
    public static void completeDamage(Team team, float x, float y, float radius, float damage){
        completeDamage(team, x, y, radius, damage, 1f, true, true);
    }
    public static void completeDamage(Team team, float x, float y, float radius, float damage, float buildDmbMult, boolean air, boolean ground){
        allNearbyEnemies(team, x, y, radius, t -> {
            if(t instanceof Unit u){
                if(u.isFlying() && air || u.isGrounded() && ground){
                    u.damage(damage);
                }
            }else if(t instanceof Building b){
                if(ground){
                    b.damage(team, damage * buildDmbMult);
                }
            }
        });
    }
    public static void allNearbyEnemies(Team team, float x, float y, float radius, Cons<Healthc> cons){
        Units.nearbyEnemies(team, x - radius, y - radius, radius * 2f, radius * 2f, unit -> {
            if(unit.within(x, y, radius + unit.hitSize / 2f) && !unit.dead){
                cons.get(unit);
            }
        });

        trueEachBlock(x, y, radius, build -> {
            if(build.team != team && !build.dead && build.block != null){
                cons.get(build);
            }
        });
    }
    private static final IntSet collidedBlocks = new IntSet();
    public static void trueEachBlock(float wx, float wy, float range, Cons<Building> cons){
        collidedBlocks.clear();
        int tx = World.toTile(wx);
        int ty = World.toTile(wy);

        int tileRange = Mathf.floorPositive(range / tilesize);

        for(int x = tx - tileRange - 2; x <= tx + tileRange + 2; x++){
            for(int y = ty - tileRange - 2; y <= ty + tileRange + 2; y++){
                if(Mathf.within(x * tilesize, y * tilesize, wx, wy, range)){
                    Building other = world.build(x, y);
                    if(other != null && !collidedBlocks.contains(other.pos())){
                        cons.get(other);
                        collidedBlocks.add(other.pos());
                    }
                }
            }
        }
    }
    public static boolean isTransformType(Unit u){
        return u instanceof TransformEntity || u instanceof TransformFlying;
    }
    public static boolean AlreadyTransform(Unit u){
        return (u instanceof TransformEntity t&&t.TransformNow)||(u instanceof TransformEntity f&&f.TransformNow);
    }
    //IO thing
    public static Skill[] ReadSkill(Reads r,Skill[] skills){
        byte l = r.b();
        for(int i = 0; i < l; i++){
            float data = r.f();
            if(skills.length>i){
                skills[i].data = data;
            }
        }
        return skills;
    }
    static final Skill[] NO_SKILLS = {};
    public static void WriteSkill(Writes w,Skill[] skills){
        w.b(skills.length);
        for(var s:skills){
            w.f(s.data);
        }
    }
    //Nothing
    public static Skill[] readSkill(Reads r){
        r.skip(r.b());
        return NO_SKILLS;
    }
    public static StatValue SkillsVaule(Seq<Skill> skills) {
        return table -> {
            table.row();
            table.table(t -> skills.each(skill -> {
                if (skill.display) {
                    t.row();
                    t.table(Styles.grayPanel, a -> {
                        a.add("[accent]" + skill.localized()).padBottom(4);
                        a.row();
                        a.left().top().defaults().left();
                        skill.addStats(a);
                    }).pad(5).margin(10).growX();
                }
            }));
        };
    }
}
