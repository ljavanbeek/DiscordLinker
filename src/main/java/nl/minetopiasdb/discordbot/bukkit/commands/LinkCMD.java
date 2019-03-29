package nl.minetopiasdb.discordbot.bukkit.commands;

import nl.minetopiasdb.discordbot.Main;
import nl.minetopiasdb.discordbot.utils.MessageUtils;
import nl.minetopiasdb.discordbot.utils.data.ConfigUtils;
import nl.minetopiasdb.discordbot.utils.link.DataLinkUtils;
import nl.minetopiasdb.discordbot.utils.link.LinkUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.UUID;

public class LinkCMD implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ConfigUtils.cc("&4ERROR: &cAlleen spelers kunnen hun Discord account linken."));
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ConfigUtils.cc("&3Gebruik &b/link <Code>&3."));
            sender.sendMessage(ConfigUtils.cc("&3Deze code kan je opvragen door &b" + ConfigUtils.getInstance().getPrefix() + "link &3uit te voeren in de discord server."));
            return true;
        }
        String strUuid = args[0];
        UUID uuid = null;
        try {
            uuid = UUID.fromString(strUuid);
        } catch (Exception ex) {
            sender.sendMessage(ConfigUtils.cc("&4ERROR: &cDat is geen geldige ID!"));
            return true;
        }
        if (!LinkUtils.getInstance().isValidLink(uuid)) {
            sender.sendMessage(ConfigUtils.cc("&4ERROR: &cDat is geen bekende link-token!"));
            return true;
        }
        long userId = LinkUtils.getInstance().getUser(uuid);
        DataLinkUtils.getInstance().setLink(((Player) sender).getUniqueId(), userId);
        String name = null;
        LinkUtils.getInstance().removeLink(uuid);
        if (Main.getBot().getUserById(userId) != null) {
            name = Main.getBot().getUserById(userId).getName() + "#" + Main.getBot().getUserById(userId).getDiscriminator();
            sender.sendMessage(ConfigUtils.cc("&3Succesvol jouw &bDiscord&3 account &b(&3" + name + "&b)&3 gelinked!"));
            Main.getBot().getUserById(userId).openPrivateChannel().complete().sendMessage(MessageUtils.getBuilder(Color.GREEN).addField("Informatie", "Jouw account is succesvol gelinked!", false).build()).queue();
            return true;
        }
        sender.sendMessage(ConfigUtils.cc("&3Succesvol jouw &bDiscord&3 account gelinked!"));
        return true;
    }
}