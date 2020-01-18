package wanderingMiniBosses.util;

import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class WanderingBossHelper {
    private static AbstractMonster wanderingBoss;

    public static AbstractMonster getMonster() {
        return wanderingBoss;
    }

    public static void setMonster(AbstractMonster m) {
        wanderingBoss = m;
    }

    //TODO: Make actual logic
    public static AbstractMonster getMonsterFromID(String id) {
        return null;
    }

    //TODO: Make actual logic
    public static AbstractMonster getRandomMonster() {
        return null;
    }
}
