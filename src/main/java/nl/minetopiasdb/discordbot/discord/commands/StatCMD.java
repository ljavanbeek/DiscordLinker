package nl.minetopiasdb.discordbot.discord.commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import nl.minetopiasdb.api.API;
import nl.minetopiasdb.api.SDBPlayer;
import nl.minetopiasdb.api.enums.TimeType;
import nl.minetopiasdb.discordbot.Main;
import nl.minetopiasdb.discordbot.utils.MessageUtils;
import nl.minetopiasdb.discordbot.utils.data.ConfigUtils;
import nl.minetopiasdb.discordbot.utils.link.DataLinkUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class StatCMD extends ListenerAdapter {

    private static String format(double number) {
        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.GERMAN);
        return df.format(number);
    }

    public void onMessageReceived(MessageReceivedEvent e) {
        String msg = e.getMessage().getContentRaw().replaceAll("  ", " ");
        if (msg.startsWith(ConfigUtils.getInstance().getPrefix() + "stats")) {
            long userId = e.getAuthor().getIdLong();
            if (e.getMessage().getMentions().size() > 0
                    && e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                userId = e.getMessage().getMentionedMembers().get(0).getUser().getIdLong();
            }
            if (!DataLinkUtils.getInstance().isLinked(userId)) {
                if (userId == e.getAuthor().getIdLong()) {
                    MessageUtils.sendMessage(e.getChannel(), MessageUtils.getBuilder(
                            e.getAuthor().getAsMention() + ", je hebt jouw discord account niet gelinked!", Color.RED));
                } else {
                    MessageUtils.sendMessage(e.getChannel(),
                            MessageUtils.getBuilder(Main.getBot().getUserById(userId).getName() + "#"
                                    + Main.getBot().getUserById(userId).getDiscriminator()
                                    + " heeft zijn discord account niet gelinked!", Color.RED));
                }

                return;
            }
            OfflinePlayer p = Bukkit.getOfflinePlayer(DataLinkUtils.getInstance().getUUIDFromDiscord(userId));
            SDBPlayer sdb = SDBPlayer.createSDBPlayer(p);
            Plugin pl = Bukkit.getPluginManager().getPlugin("MinetopiaSDB");

            EmbedBuilder embed = MessageUtils.getBuilder(Color.GREEN);
            embed.addField("Speler", p.getName(), false);


            ConfigUtils cu = ConfigUtils.getInstance();

            if (cu.getShowOption(ConfigUtils.ShowOption.FITHEID)) {
                embed.addField(cu.getShowTitle(ConfigUtils.ShowOption.FITHEID), sdb.getFitheid() + "/" + pl.getConfig().getString("Fitheid.Max"), false);
            }
            if (cu.getShowOption(ConfigUtils.ShowOption.PREFIX)) {
                embed.addField(cu.getShowTitle(ConfigUtils.ShowOption.PREFIX), sdb.getPrefix(), false);
            }
            if (cu.getShowOption(ConfigUtils.ShowOption.RANK)) {
                embed.addField(cu.getShowTitle(ConfigUtils.ShowOption.RANK), sdb.getRank(), false);
            }
            if (cu.getShowOption(ConfigUtils.ShowOption.LEVEL)) {
                embed.addField(cu.getShowTitle(ConfigUtils.ShowOption.LEVEL), "" + sdb.getLevel(), false);
            }
            if (cu.getShowOption(ConfigUtils.ShowOption.MONEY)) {
                embed.addField(cu.getShowTitle(ConfigUtils.ShowOption.MONEY), "€ " + format(API.getEcon().getBalance(p)), false);
            }
            if (cu.getShowOption(ConfigUtils.ShowOption.ONLINETIME)) {
                embed.addField(cu.getShowTitle(ConfigUtils.ShowOption.ONLINETIME), "" + sdb.getTime(TimeType.DAYS) + " dagen, "
                        + sdb.getTime(TimeType.HOURS) + " uur, " + sdb.getTime(TimeType.MINUTES) + " minuten", false);
            }
            if (cu.getShowOption(ConfigUtils.ShowOption.ONLINESTATUS)) {
                embed.addField(cu.getShowTitle(ConfigUtils.ShowOption.ONLINESTATUS), p.isOnline() ? "Ja" : "Nee", false);
            }
            if (cu.getShowOption(ConfigUtils.ShowOption.GRAYCOIN)) {
                embed.addField(cu.getShowTitle(ConfigUtils.ShowOption.GRAYCOIN), "" + sdb.getGrayCoins(), false);
            }
            MessageUtils.sendMessage(e.getChannel(), embed);
        }
    }
}