package wanderingMiniBosses.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import wanderingMiniBosses.WanderingminibossesMod;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractWanderingBoss extends CustomMonster {

    protected Map<Byte, EnemyMoveInfo> moves;

    public AbstractWanderingBoss(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl);

        this.type = EnemyType.BOSS;
        this.moves = new HashMap<>();
    }

    public String getUID() {
        return WanderingminibossesMod.getModID() + ":" + name;
    }

    //TODO: Add automated running here since every monster should have this.
}
