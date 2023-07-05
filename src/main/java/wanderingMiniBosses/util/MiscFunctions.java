package wanderingMiniBosses.util;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import wanderingMiniBosses.WanderingminibossesMod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.megacrit.cardcrawl.random.Random;

public class MiscFunctions {

	// lets us log output
	// for faster debugging
	public static final Logger logger = LogManager.getLogger(WanderingminibossesMod.class.getName());

	public static void fastLoggerLine(String message) {
    	logger.info(message);
    }
    
    public static void fastLoggerLine(Boolean message) {
    	String converted_message = message.toString();
    	fastLoggerLine(converted_message);
    }
    
    public static void fastLoggerLine(Byte message) {
    	String converted_message = message.toString();
    	fastLoggerLine(converted_message);
    }
    
	public static void fastLoggerLine(int message) {
		String converted_message = message + "";
		fastLoggerLine(converted_message);
	}
    
    public static int headsOrTails(Random random) {
		return random.random(1);
	}

	public static void atb(AbstractGameAction action) {
		AbstractDungeon.actionManager.addToBottom(action);
	}

	public static void applyToSelf(AbstractPower po) {
		atb(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, po, po.amount));
	}
	
}
