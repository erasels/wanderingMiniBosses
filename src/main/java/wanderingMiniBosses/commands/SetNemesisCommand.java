package wanderingMiniBosses.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.util.WanderingBossHelper;

import java.util.ArrayList;

public class SetNemesisCommand extends ConsoleCommand {
    public SetNemesisCommand() {
        this.requiresPlayer = true;
        followup.put("hp", NemesisHpCommand.class);
        followup.put("list", NemesisListCommand.class);
        maxExtraTokens = 1;
        minExtraTokens = 0;
        this.simpleCheck = true;
    }

    @Override
    protected void execute(String[] tokens, int depth) {
        if(tokens.length == 1) {
            if(WanderingBossHelper.nemesisCheck()) {
                DevConsole.log("Nemesis ID: \"" + WanderingBossHelper.getIdWithoutModId() + "\".");
            } else {
                noNemesis();
            }
        } else {
            String key = WanderingminibossesMod.makeID(tokens[depth]);
            if (WanderingBossHelper.monsterMap.containsKey(key)) {
                WanderingBossHelper.setCurrentMonsterID(key);
                WanderingBossHelper.HAS_NEMESIS = true;
                DevConsole.log("Nemesis set to \"" + tokens[depth] + "\".");
            } else if(tokens[depth].equalsIgnoreCase("remove")) {
                if(WanderingBossHelper.HAS_NEMESIS) {
                    WanderingBossHelper.HAS_NEMESIS = false;
                    DevConsole.log("Removing Nemesis.");
                } else {
                    noNemesis();
                }
            } else if(tokens[depth].equalsIgnoreCase("list")) {
                WanderingminibossesMod.logger.info("Dumping Wandering Miniboss IDs.");
                for(final String id : WanderingBossHelper.monsterMap.keySet()) {
                    WanderingminibossesMod.logger.info(WanderingBossHelper.getIdWithoutModId(id));
                }
                DevConsole.log("Dumping IDs in ModTheSpire window.");
            } else {
                DevConsole.log("Nemesis ID \"" + tokens[depth] + "\" does not exist.");
            }
        }
    }

    @Override
    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList(WanderingBossHelper.monsterMap.keySet());
        for(int i = result.size() - 1; i >= 0; i--) {
            String id = result.get(i);
            result.set(i, id.substring(id.indexOf(":") + 1));
        }
        result.add("remove");
        return result;
    }

    public static void noNemesis() {
        DevConsole.log("There is no Nemesis.");
    }
}
