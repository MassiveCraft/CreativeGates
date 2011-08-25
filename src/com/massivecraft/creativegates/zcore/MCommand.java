package com.massivecraft.creativegates.zcore;

import java.util.*;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.creativegates.zcore.MCommand;
import com.massivecraft.creativegates.zcore.MPlugin;
import com.massivecraft.creativegates.zcore.util.TextUtil;


public abstract class MCommand
{
	public MPlugin p;
	
	// The sub-commands to this command
	public List<MCommand> subCommands;
	
	// The different names this commands will react to  
	public List<String> aliases;
	
	// Information on the args
	public List<String> requiredArgs;
	public List<String> optionalArgs;
	
	// Help info
	public String helpShort;
	public List<String> helpLong;
	
	// Some information on permissions
	public boolean senderMustBePlayer;
	public String permission;
	
	// Information available on execution of the command
	public CommandSender sender; // Will always be set
	public Player player; // Will only be set when the sender is a player
	public List<String> args; // Will contain the arguments, or and empty list if there are none.
	public List<MCommand> commandChain; // The command chain used to execute this command
	
	public MCommand(MPlugin p)
	{
		this.p = p;
		
		this.subCommands = new ArrayList<MCommand>();
		this.aliases = new ArrayList<String>();
		
		this.requiredArgs = new ArrayList<String>();
		this.optionalArgs = new ArrayList<String>();
		
		this.helpLong = new ArrayList<String>();
	}
	
	// The commandChain is a list of the parent command chain used to get to this command.
	public void execute(CommandSender sender, List<String> args, List<MCommand> commandChain)
	{
		// Set the execution-time specific variables
		this.sender = sender;
		if (sender instanceof Player)
		{
			this.player = (Player)sender;
		}
		else
		{
			this.player = null;
		}
		this.args = args;
		this.commandChain = commandChain;

		// Is there a matching sub command?
		if (args.size() > 0 )
		{
			for (MCommand subCommand: this.subCommands)
			{
				if (subCommand.aliases.contains(args.get(0)))
				{
					args.remove(0);
					commandChain.add(this);
					subCommand.execute(sender, args, commandChain);
					return;
				}
			}
		}
		
		if ( ! validateCall())
		{
			return;
		}
		
		perform();
	}
	
	public void execute(CommandSender sender, List<String> args)
	{
		execute(sender, args, new ArrayList<MCommand>());
	}
	
	// This is where the command action is performed.
	public abstract void perform();
	
	
	// -------------------------------------------- //
	// Call Validation
	// -------------------------------------------- //
	
	/**
	 * In this method we validate that all prerequisites to perform this command has been met.
	 */
	public boolean validateCall()
	{
		
		if ( ! validateSender())
		{
			return false;
		}
		
		if ( ! validatePermissions())
		{
			return false;
		}
		
		if ( ! validateParameters())
		{
			return false;
		}
		
		return true;
	}
	
	public boolean validateSender()
	{
		if (this.senderMustBePlayer && ! (sender instanceof Player))
		{
			msg(p.txt.get("command.sender_must_me_player"));
			return false;
		}
		return true;
	}
	
	public boolean validatePermissions()
	{
		return p.perm.test(this.sender, this.permission);
	};
	
	public boolean validateParameters()
	{
		if (this.args.size() < this.requiredArgs.size())
		{
			msg(p.txt.get("command.to_few_args"));
			msg(getUseageTemplate());
			return false;
		}
		return true;
	}
	
	// -------------------------------------------- //
	// Help and Usage information
	// -------------------------------------------- //
	
	public String getUseageTemplate(List<MCommand> commandChain)
	{
		StringBuilder ret = new StringBuilder();
		ret.append(p.txt.tags("<command>"));
		ret.append('/');
		
		for (MCommand mc : commandChain)
		{
			ret.append(TextUtil.implode(mc.aliases, ","));
			ret.append(' ');
		}
		
		ret.append(TextUtil.implode(this.aliases, ","));
		
		List<String> args = new ArrayList<String>();
		
		for (String requiredArg : this.requiredArgs)
		{
			args.add("<"+requiredArg+">");
		}
		
		for (String optionalArg : this.optionalArgs)
		{
			args.add("["+optionalArg+"]");
		}
		
		if (args.size() > 0)
		{
			ret.append(p.txt.tags("<parameter>"));
			ret.append(TextUtil.implode(args, " "));
		}
		
		return ret.toString();
	}
	
	public String getUseageTemplate()
	{
		return getUseageTemplate(this.commandChain);
	}
	
	// -------------------------------------------- //
	// Message Sending Helpers
	// -------------------------------------------- //
	
	public void msg(String msg, boolean parseColors)
	{
		if (parseColors)
		{
			sender.sendMessage(p.txt.tags(msg));
			return;
		}
		sender.sendMessage(msg);
	}
	
	public void msg(String msg)
	{
		this.msg(msg, false);
	}
	
	public void msg(List<String> msgs, boolean parseColors)
	{
		for(String msg : msgs)
		{
			this.msg(msg, parseColors);
		}
	}
	
	public void msg(List<String> msgs)
	{
		msg(msgs, false);
	}
}
