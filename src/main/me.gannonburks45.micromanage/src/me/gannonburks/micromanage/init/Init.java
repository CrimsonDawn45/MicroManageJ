package src.me.gannonburks.micromanage.init;

import java.util.Scanner;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import src.me.gannonburks.micromanage.Main;
import src.me.gannonburks.micromanage.command.CommandHandler;
import src.me.gannonburks.micromanage.command.commands.DisableCommand;
import src.me.gannonburks.micromanage.command.commands.EchoCommand;
import src.me.gannonburks.micromanage.command.commands.EnableCommand;
import src.me.gannonburks.micromanage.command.commands.HelpCommand;
import src.me.gannonburks.micromanage.command.commands.PrivateMessageCommand;
import src.me.gannonburks.micromanage.command.commands.ServerMessageCommand;
import src.me.gannonburks.micromanage.command.commands.ServersCommand;
import src.me.gannonburks.micromanage.command.commands.ShutdownCommand;
import src.me.gannonburks.micromanage.command.commands.WhereAmICommand;
import src.me.gannonburks.micromanage.event.BotEvent;
import src.me.gannonburks.micromanage.event.events.OnGuildReady;
import src.me.gannonburks.micromanage.event.events.OnGuildUpdate;
import src.me.gannonburks.micromanage.event.events.OnMessageReceivedEvent;
import src.me.gannonburks.micromanage.module.Module;
import src.me.gannonburks.micromanage.module.ModuleRegistry;
import src.me.gannonburks.micromanage.util.Logger;

public class Init {

	/*
	 * Primary initialization method
	 * 
	 * @param args Arguments for bot startup.
	 */
	public static void init(String[] args)
	{
		initDefModule();
		login(args);
		initListeners();
		console();
	}
	
	/*
	 * Login method
	 */
	public static void login(String[] args) {
		
		if(args.length > 0) {	//Check for a argument
			
			try
			{	//Try to log in
				Main.bot = new JDABuilder(AccountType.BOT).setToken(args[0]).build();
			}
			catch (LoginException e)
			{
				Logger.error("Invalid Auth token!");
				Main.shutdown();
			}
			
		} else {
			
			//Ask for valid token
			Logger.error("Please input a authorization token as the first argument!");
			Main.shutdown();
		}
	}
	
	/*
	 * Console Method
	 */
	public static void console() {
		
		Scanner keyboard = new Scanner(System.in);
		String inp = "";
		
		while(!(inp.startsWith(Main.DEFAULT_PREFIX + "shutdown")))
		{
		
			inp = keyboard.nextLine();
			
			//Split input into args
			String[] consoleArgs = inp.split(" ");
			String label = consoleArgs[0].replaceFirst(Main.DEFAULT_PREFIX, "");
			
			if(CommandHandler.isCmd(inp))
			{
				CommandHandler.executeCommand(label, consoleArgs);
			}
			else
			{
				System.out.println("\"" + label + "\" is not a valid command!");
			}
		}
		
		//Exit Program
		keyboard.close();
		Main.shutdown();
	}
	
	/*
	 * Default module init
	 */
	public static void initDefModule()
	{
		//Create Module
		Module def = new Module("default");
		
		//Populate Commands
		def.registerAllCommands
		(
				new HelpCommand("help", false, true, null),
				new WhereAmICommand("whereami", false, true, "Tells you where you are."),
				new ServersCommand("servers", false, true, "Lists all servers in the server registry."),
				new EchoCommand("echo", true, true, "Repeats message."),
				new PrivateMessageCommand("pm", true, true, "Sends a private message to someone."),
				new ServerMessageCommand("sm", true, true, "Sends a message to a specific server in a specific channel."),
				new EnableCommand("enable", false, false, "Enables a command."),
				new DisableCommand("disable", false, false, "Disables a command."),
				new ShutdownCommand("shutdown", false, true, "Shuts down the bot.")
		);
		
		def.registerAllEvents
		(
				new OnMessageReceivedEvent(false),
				new OnGuildReady(false),
				new OnGuildUpdate(false)
		);
		
		//Register It
		ModuleRegistry.registerAll(def);
	}
	
	/*
	 * add event listeners to bot instance
	 */
	public static void initListeners()
	{
		for(BotEvent event : ModuleRegistry.getAllEvents())
		{
			Main.bot.addEventListener(event);
		}
	}
}
