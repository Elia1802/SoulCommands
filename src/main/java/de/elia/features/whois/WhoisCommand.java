package de.elia.features.whois;

import de.elia.Main;
import de.elia.systemclasses.DatabaseManager;
import de.elia.utils.ErrorMessage;
import de.elia.utils.Message;
import de.elia.utils.MessageUtils;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WhoisCommand extends Command {

    private static final String BYPASS_PERMISSION = "soulsmp.whois";

    public WhoisCommand() {
      this("whois");
      if (Bukkit.getPluginManager().getPermission(BYPASS_PERMISSION) == null) {
        Bukkit.getPluginManager().addPermission(
          new Permission(BYPASS_PERMISSION, "", PermissionDefault.OP)
        );
      }
    }

    protected WhoisCommand(@NotNull String name) {
        super(name);
    }

    LuckPerms luckPerms = LuckPermsProvider.get();

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            ErrorMessage.noPlayer(sender);
            return false;
        }
        if (!player.hasPermission(BYPASS_PERMISSION)) {
            ErrorMessage.noPermission(player);
            return false;
        }
        if (args.length != 1) {
            ErrorMessage.usage("/whois [player]", player);
            return false;
        }
        Player targetPlayer = Bukkit.getPlayerExact(args[0]);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);

        if (targetPlayer == null) {
            //only for offline players
            Message.mainPrefix("<#FF9BDF>" + offlinePlayer.getName() + "<grey>'s info:", player);
            if (offlinePlayer.hasPlayedBefore()) {

                String ip = Main.getDatabaseManager().getIP(offlinePlayer.getName());
                if (ip == null) {
                    Message.standard("<#FF9BDF>IP<grey>: nicht gefunden", player);
                } else {
                    Message.standard("<#FF9BDF>IP<grey>: " + ip, player);
                }

                //send Coords
                int x = offlinePlayer.getLocation().getBlockX();
                int y = offlinePlayer.getLocation().getBlockZ();
                int z = offlinePlayer.getLocation().getBlockZ();
                Message.standard("<#FF9BDF>Coordinates: <grey>" + x + ", " + y + ", " + z, player);


                //send playtime and punishment type
                int playtime = offlinePlayer.getStatistic(Statistic.PLAY_ONE_MINUTE);
                playtime = playtime / 20;
                Message.standard("<#FF9BDF>Playtime<grey>: " + MessageUtils.shortInteger(playtime), player);

                long firstPlayed = offlinePlayer.getFirstPlayed();
                String date = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date(firstPlayed));
                Message.standard("<#FF9BDF>First Join<grey>: " + date, player);

                try {
                  String punishmentTypes = "";
                    ArrayList<String> punishmentType = Main.getDatabaseManager().getPunishmentType(offlinePlayer.getName());
                    if (punishmentType.isEmpty()) {
                        punishmentTypes = "keine";
                    }
                    Message.standard("<#FF9BDF>Punishment<grey>: " + punishmentType, player);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message.standard("<#FF9BDF>Last Seen<grey>: " + MessageUtils.shortDate(offlinePlayer.getLastSeen()), player);
            } else {
                Message.standard("<#FF9BDF>Last Seen<grey>: nicht zuvor gesehen", player);
            }
            return true;
        }
        Message.mainPrefix("<#FF9BDF>" + targetPlayer.getName() + "<grey>'s info:", player);
        //send ip
        //TODO add region (maybe with GeoLite db)
        Message.standard("<#FF9BDF>IP<grey>: " + targetPlayer.getAddress().getAddress().getHostAddress(), player);

        Message.standard("<#FF9BDF>Ping<grey>: " + targetPlayer.getPing(), player);

        Message.standard("<#FF9BDF>Client Brand<grey>: " + targetPlayer.getClientBrandName(), player);

        // potocol number -> minecraft version
        Map<Integer, String> versionMap = new HashMap<>();
      versionMap.put(47, "1.8");
      versionMap.put(107, "1.9");
      versionMap.put(108, "1.9.1");
      versionMap.put(109, "1.9.2");
      versionMap.put(110, "1.9.4");
      versionMap.put(210, "1.10");
      versionMap.put(315, "1.11");
      versionMap.put(316, "1.11.2");
      versionMap.put(335, "1.12");
      versionMap.put(338, "1.12.1");
      versionMap.put(340, "1.12.2");
      versionMap.put(393, "1.13");
      versionMap.put(401, "1.13.1");
      versionMap.put(404, "1.13.2");
      versionMap.put(477, "1.14");
      versionMap.put(480, "1.14.1");
      versionMap.put(485, "1.14.2");
      versionMap.put(490, "1.14.3");
      versionMap.put(498, "1.14.4");
      versionMap.put(573, "1.15");
      versionMap.put(575, "1.15.1");
      versionMap.put(578, "1.15.2");
      versionMap.put(735, "1.16");
      versionMap.put(736, "1.16.1");
      versionMap.put(751, "1.16.2");
      versionMap.put(753, "1.16.3");
      versionMap.put(754, "1.16.4/1.16.5");
      versionMap.put(755, "1.17");
      versionMap.put(756, "1.17.1");
      versionMap.put(757, "1.18/1.18.1");
      versionMap.put(758, "1.18.2");
      versionMap.put(759, "1.19");
      versionMap.put(760, "1.19.1/1.19.2");
      versionMap.put(761, "1.19.3");
      versionMap.put(762, "1.19.4");
      versionMap.put(763, "1.20");
      versionMap.put(764, "1.20.2");
      versionMap.put(765, "1.20.3/1.20.4");
      versionMap.put(766, "1.20.5");
      versionMap.put(767, "1.21");
      versionMap.put(768, "1.21.2");

        int protocol = targetPlayer.getProtocolVersion();
        String version = versionMap.getOrDefault(protocol, "Unbekannte Version (" + protocol + ")");
        Message.standard("<#FF9BDF>Version<grey>: " + version, player);

        Message.standard("<#FF9BDF>UUID<grey>: " + targetPlayer.getUniqueId(), player);

        //send Coords
        int x = targetPlayer.getLocation().getBlockX();
        int y = targetPlayer.getLocation().getBlockZ();
        int z = targetPlayer.getLocation().getBlockZ();
        Message.standard("<#FF9BDF>Coordinates: <grey>" + x + ", " + y + ", " + z, player);

        //send playtime and punishment type
        int playtime = targetPlayer.getStatistic(Statistic.PLAY_ONE_MINUTE);
        playtime = playtime / 20;

        Message.standard("<#FF9BDF>Playtime<grey>: " + MessageUtils.shortInteger(playtime), player);


        long firstPlayed = targetPlayer.getFirstPlayed();
        String date = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date(firstPlayed));
        Message.standard("<#FF9BDF>First Join<grey>: " + date, player);

        try {
            Message.standard("<#FF9BDF>Punishment<grey>: " + Main.getDatabaseManager().getPunishmentType(targetPlayer.getName()), player);
        } catch (Exception e) {
            e.printStackTrace();

        }
        Message.standard("<#FF9BDF>Rank<grey>: <reset>" + getPrefix(targetPlayer), player);
        return true;
    }

    public String getPrefix(Player player) {
        //grab user from luckperms
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user != null) {
            //grab prefix from user
            CachedMetaData metaData = user.getCachedData().getMetaData();
            return metaData.getPrefix();
        }
        //empty string if no prefix
        return "";
    }
}
