package de.elia.features.chat;

import de.elia.Main;
import de.elia.utils.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.net.http.WebSocket;

public class ChatListener implements Listener {
  MiniMessage miniMessage = MiniMessage.miniMessage();
  LuckPerms luckPerms = LuckPermsProvider.get();
  @EventHandler
  public void onChat(AsyncPlayerChatEvent event){
    //grab player
    Player player = event.getPlayer();

    //cancel default chatMessage
    event.setCancelled(true);

    if(Main.getDatabaseManager().getPunishmentType(player.getName()).contains("MUTE") || Main.getDatabaseManager().getPunishmentType(player.getName()).contains("TEMP_MUTE")) {
      System.out.println("Muted");
      return;
    }
    //grab raw message & player prefix
    String rawMessage = event.getMessage();
    String luckpermsPrefix = getPrefix(player);

    //send new message to all players
    for (Player onlinePlayer : Bukkit.getOnlinePlayers()){
      Message.standard(luckpermsPrefix + " <grey>" + player.getName() + " »</grey> " + rawMessage, onlinePlayer);
    }
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
