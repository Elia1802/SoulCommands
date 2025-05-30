package de.elia.features.commandwatcher;

import de.elia.Main;
import de.elia.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

import static de.elia.api.messages.builder.MessageBuilder.*;

public class CommandWatcherListener implements Listener {


  private static final String BYPASS_PERMISSION = "soulsmp.commandwatch";

  public CommandWatcherListener() {
    // Register the bypass permission if it doesn't exist
    if (Bukkit.getPluginManager().getPermission(BYPASS_PERMISSION) == null) {
      Bukkit.getPluginManager().addPermission(
        new Permission(BYPASS_PERMISSION, "Allows watching others", PermissionDefault.OP)
      );
    }
  }

  @EventHandler
  public void onPlayerCommand(PlayerCommandPreprocessEvent event){
    Player sender = event.getPlayer();
    String command = event.getMessage();

    //grab only the command
    String arr[] = command.split(" ", 2);
    String mainCommand = arr[0];
    //grab only the command part after the "/"
    String bareCommand = mainCommand.substring(1);


    //check if anyone has command watch active
    if(CommandWatcherToggle.cwPlayers.isEmpty()){
      return;
    }


    Bukkit.getOnlinePlayers().forEach(watchPlayer -> {
      //only send Command to those who have command watch active and have the permission
      if(!watchPlayer.hasPermission(BYPASS_PERMISSION)) {
        return;
      }

      if (!CommandWatcherToggle.cwPlayers.contains(watchPlayer)) {
        return;
      }
      //check so that admin don't see their own commands
      if(sender == watchPlayer){
        return;
      }

      //list of banned commands to check from
      List<String> bannedCommands = new ArrayList<>();
      bannedCommands.add("ec");
      bannedCommands.add("enderchest");
      bannedCommands.add("craft");
      bannedCommands.add("c");
      bannedCommands.add("playtime");
      bannedCommands.add("discord");
      bannedCommands.add("dc");

      //checks, so that there is not spam from unnecessary commands
      if(bannedCommands.contains(bareCommand)){
        return;
      }
      //checks, so that there is not spam from not existing commands
      if(Main.server.getCommandMap().getCommand(bareCommand) == null){
        return;
      }
      //send Message
      Message.cwPrefix("<dark_aqua>" + sender.getName() + "</dark_aqua> <dark_grey>»</dark_grey> <grey>" + command + "</grey>", watchPlayer);

    });
  }
}
