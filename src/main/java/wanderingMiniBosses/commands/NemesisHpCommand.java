package wanderingMiniBosses.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import wanderingMiniBosses.monsters.AbstractWanderingBoss;
import wanderingMiniBosses.monsters.WanderingMonsterGroup;
import wanderingMiniBosses.util.WanderingBossHelper;

import java.util.ArrayList;

public class NemesisHpCommand extends ConsoleCommand {
    public NemesisHpCommand() {
        this.requiresPlayer = true;
        maxExtraTokens = 1;
        minExtraTokens = 1;
    }

    @Override
    protected void execute(String[] tokens, int depth) {
        try {
            if(WanderingBossHelper.nemesisCheck()) {
                int num = Integer.parseInt(tokens[depth]);
                for(final WanderingMonsterGroup.WanderingBossInfo info : WanderingBossHelper.getMonster().monsterInfo) {
                    info.setCurrentHealth(num);
                }
                if(AbstractDungeon.getCurrRoom().monsters != null) {
                    for (final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                        if(mo instanceof AbstractWanderingBoss) {
                            if(num <= 0) {
                                AbstractDungeon.actionManager.addToBottom(new SuicideAction(mo));
                            } else {
                                mo.currentHealth = num;
                                mo.healthBarUpdatedEvent();
                            }
                        }
                    }
                }
                DevConsole.log("Setting " + WanderingBossHelper.getIdWithoutModId() + " hp to " + num + ".");
            } else {
                SetNemesisCommand.noNemesis();
            }
        } catch(NumberFormatException nfe) {
            DevConsole.log(tokens[depth] + " is not a number, dummy.");
        }
    }

    @Override
    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        return bigNumbers();
    }
}
