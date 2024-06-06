package carrier.Main.Content.Bullet;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Interp;
import arc.math.Mathf;
import arc.util.Time;
import arc.util.Tmp;
import carrier.Main.MathComplex;
import mindustry.entities.Damage;
import mindustry.entities.bullet.ContinuousLaserBulletType;
import mindustry.gen.Bullet;
import mindustry.graphics.Drawf;

public class ContinousLaserLowerBulletType extends ContinuousLaserBulletType{
    public float shutPercent;
    public Interp shutInterp = Interp.linear;
    @Override
    public void draw(Bullet b){
        float fout = shutInterp.apply(MathComplex.ProgressFadeOutDelay(b.fout(), Mathf.clamp(shutPercent)));
        float realLength = Damage.findLength(b, length, laserAbsorb, pierceCap);
        float rot = b.rotation();

        for(int i = 0; i < colors.length; i++){
            Draw.color(Tmp.c1.set(colors[i]).mul(1f + Mathf.absin(Time.time, 1f, 0.1f)));

            float colorFin = i / (float)(colors.length - 1);
            float baseStroke = Mathf.lerp(strokeFrom, strokeTo, colorFin);
            float stroke = (width + Mathf.absin(Time.time, oscScl, oscMag)) * fout * baseStroke;
            float ellipseLenScl = Mathf.lerp(1 - i / (float)(colors.length), 1f, pointyScaling);

            Lines.stroke(stroke);
            Lines.lineAngle(b.x, b.y, rot, realLength - frontLength, false);

            //back ellipse
            Drawf.flameFront(b.x, b.y, divisions, rot + 180f, backLength, stroke / 2f);

            //front ellipse
            Tmp.v1.trnsExact(rot, realLength - frontLength);
            Drawf.flameFront(b.x + Tmp.v1.x, b.y + Tmp.v1.y, divisions, rot, frontLength * ellipseLenScl, stroke / 2f);
        }

        Tmp.v1.trns(b.rotation(), realLength * 1.1f);

        Drawf.light(b.x, b.y, b.x + Tmp.v1.x, b.y + Tmp.v1.y, lightStroke, lightColor, 0.7f);
        Draw.reset();
    }
}
