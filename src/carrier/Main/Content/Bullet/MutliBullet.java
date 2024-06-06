package carrier.Main.Content.Bullet;

import arc.struct.Seq;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;

public class MutliBullet extends DataBulletType{
    Seq<BulletType> mutli = new Seq<>();
    public MutliBullet(BulletType... b){
        mutli.add(b);
    }
    public MutliBullet(Seq<BulletType> b){
        mutli.add(b);
    }
    @Override
    public void init(Bullet b){
        super.init(b);
        if(mutli != null){
            for(var bt:mutli){
                bt.create(b,b.team, b.x, b.y, b.rotation(),0,0);
            }
        }
    }
}
