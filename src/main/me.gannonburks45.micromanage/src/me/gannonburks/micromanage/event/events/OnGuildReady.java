package src.me.gannonburks.micromanage.event.events;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import src.me.gannonburks.micromanage.Main;
import src.me.gannonburks.micromanage.command.CommandRegistry;
import src.me.gannonburks.micromanage.event.BotEvent;
import src.me.gannonburks.micromanage.module.ModuleRegistry;
import src.me.gannonburks.micromanage.server.Server;
import src.me.gannonburks.micromanage.server.ServerRegistry;

public class OnGuildReady extends BotEvent {

	public OnGuildReady(boolean canStopIn)
	{
		super(canStopIn);
	}

	@Override
	public void onGuildReady(GuildReadyEvent event)
	{
		CommandRegistry reg = new CommandRegistry();
		
		reg.registerAll(ModuleRegistry.getAllCommands());
		
		ServerRegistry.register(new Server(event.getGuild(), Main.DEFAULT_PREFIX, reg));
	}
}
