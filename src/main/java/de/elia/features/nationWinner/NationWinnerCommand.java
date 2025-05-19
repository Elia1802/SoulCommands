package de.elia.features.nationWinner;

import de.elia.Main;
import de.elia.utils.ErrorMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NationWinnerCommand extends Command {
  private static String BYPASS_PERMISSION = "soulsmp.nationwinner";

  protected NationWinnerCommand(@NotNull String name) {
    super(name);
  }

  public NationWinnerCommand() {
    super("nationwinner");
    if (Bukkit.getPluginManager().getPermission(BYPASS_PERMISSION) == null) {
      Bukkit.getPluginManager().addPermission(
        new Permission(BYPASS_PERMISSION, "", PermissionDefault.OP)
      );
    }
  }

  @Override
  public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String @NotNull [] args) {
    if(args.length == 0) {
      ErrorMessage.usage("/nationwinner [player]", sender);
      return false;
    }
    if(!sender.hasPermission(BYPASS_PERMISSION)) {
      ErrorMessage.noPermission(sender);
      return false;
    }
    OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
    List<OfflinePlayer> teamPlayers = Main.getDatabaseManager().getTeamPlayerList(Main.getDatabaseManager().getTeamID(player.getUniqueId().toString()));
    teamPlayers.forEach(teamPlayer -> {
      Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + teamPlayer.getName() + " permission settemp suffix.1000.ѹ true 1mo");
    });
    return true;
  }
}
