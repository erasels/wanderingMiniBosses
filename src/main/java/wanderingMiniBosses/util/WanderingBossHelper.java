package wanderingMiniBosses.util;

import basemod.BaseMod;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wanderingMiniBosses.monsters.eternalPrincess.EternalPrincess;
import wanderingMiniBosses.monsters.immortalflame.ImmortalFlame;
import wanderingMiniBosses.monsters.inkman.InkMan;

import java.util.HashMap;
import java.util.Map;

public class WanderingBossHelper {
    private static AbstractMonster wanderingBoss;
    public static Map<String, BaseMod.GetMonster> monsterMap = new HashMap<>();

    public static void populateMonsterMap() {
        if(monsterMap.isEmpty()) {
            monsterMap.put(ImmortalFlame.ID, ImmortalFlame::new);
            monsterMap.put(EternalPrincess.ID, EternalPrincess::new);
            monsterMap.put(InkMan.ID, InkMan::new);
        }
    }

    public static AbstractMonster getMonster() {
        return wanderingBoss;
    }

    public static void setMonster(AbstractMonster m) {
        wanderingBoss = m;
    }

    public static boolean isMonsterAlive() {
        return wanderingBoss.currentHealth > 0;
    }

    public static AbstractMonster getMonsterFromID(String id) {
        return monsterMap.get(id).get();
    }

    public static AbstractMonster getRandomMonster() {
        if(!monsterMap.isEmpty()) {
            return ((BaseMod.GetMonster) monsterMap.values().toArray()[AbstractDungeon.monsterRng.random(monsterMap.size()-1)]).get();
        }
        return null;
    }
}
