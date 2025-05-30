package de.elia.features.teleport.tpa;

import de.elia.utils.ErrorMessage;
import de.elia.utils.Message;
import de.elia.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class TpaAcceptCommand extends Command {
  protected TpaAcceptCommand(@NotNull String name) {
    super(name);
  }

  public TpaAcceptCommand() {
    this("tpaaccept");
  }

  @Override
  public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
    if(!(sender instanceof Player player)){
      ErrorMessage.noPlayer(sender);
      return false;
    }

    switch (args.length){
      case 0:
        // Handle TPA requests
        if(TpaCommand.getPending().isEmpty()){
          ErrorMessage.standard("Niemand möchte sich zu dir teleportieren oder dich zu sich teleportieren", player);
          return false;
        }
        for (Map.Entry<Player, Player> entry : TpaCommand.getPending().entrySet()) {
          if (entry.getValue().equals(player)) {
            entry.getKey().teleport(player);
            TpaCommand.getPending().remove(entry.getKey(), player);
            Message.standard("<grey>teleportiere...", entry.getKey());
            return true;
          }
        }
        if(TpaCommand.getPendingHere().isEmpty()){
          ErrorMessage.standard("Niemand möchte sich zu dir teleportieren oder dich zu sich teleportieren", player);
          return false;
        }
        // Handle TPAHere requests
        for (Map.Entry<Player, Player> entry : TpaCommand.getPendingHere().entrySet()) {
          if (entry.getValue().equals(player)) {
            player.teleport(entry.getKey());
            TpaCommand.getPendingHere().remove(entry.getKey(), player);
            Message.standard("<grey>teleportiere...", player);
            return true;
          }
        }
        break;
      case 1:
        Player targetPlayer = Bukkit.getPlayerExact(args[0]);
        if (targetPlayer == null) {
          ErrorMessage.standard("Dieser Spieler existiert nicht", player);
          return false;
        }

        // Check for TPA request
        if(TpaCommand.getPending().get(targetPlayer) != null && TpaCommand.getPending().get(targetPlayer).equals(player)){
          TpaCommand.getPending().remove(targetPlayer, player);
          targetPlayer.teleport(player);
          Message.standard("<grey>teleportiere...", targetPlayer);
          return true;
        }
        // Check for TPAHere request
        if(TpaCommand.getPendingHere().get(targetPlayer) != null && TpaCommand.getPendingHere().get(targetPlayer).equals(player)){
          TpaCommand.getPendingHere().remove(targetPlayer, player);
          player.teleport(targetPlayer);
          Message.standard("<grey>teleportiere...", player);
          return true;
        }

        ErrorMessage.standard("Dieser Spieler möchte sich nicht zu dir teleportieren oder dich zu sich teleportieren", player);
        break;
      default:
        ErrorMessage.usage("/tpaaccept [player]", player);
        return false;
    }
    return true;
  }
}
