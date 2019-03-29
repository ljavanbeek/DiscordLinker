package nl.minetopiasdb.discordbot.utils;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import nl.minetopiasdb.discordbot.utils.data.ConfigUtils;

import java.awt.*;

public class MessageUtils {

    public static EmbedBuilder getBuilder(String message, Color c) {
        return new EmbedBuilder()
                .setAuthor(ConfigUtils.getInstance().getHeader(), null, ConfigUtils.getInstance().getLogo())
                .setFooter(ConfigUtils.getInstance().getFooter(), ConfigUtils.getInstance().getLogo())
                .addField("\nInformatie:", "\n" + message, true)
                .setColor(c);
    }

    public static EmbedBuilder getBuilder(Color c) {
        return new EmbedBuilder()
                .setAuthor(ConfigUtils.getInstance().getHeader(), null, ConfigUtils.getInstance().getLogo())
                .setFooter(ConfigUtils.getInstance().getFooter(), ConfigUtils.getInstance().getLogo())
                .setColor(c);
    }

    public static boolean sendPrivateAndCheckIfCanReceive(User user, EmbedBuilder builder) {
        try {
            user.openPrivateChannel().complete().sendMessage(builder.build()).complete().getIdLong();
        } catch (Exception ex) {
            if (ex.getMessage().contains("Cannot send messages to this user")) {
                return false;
            }
        }
        return true;
    }

    public static void sendMessage(MessageChannel channel, EmbedBuilder builder) {
        channel.sendMessage(builder.build()).queue();
    }

    public static void sendMessage(MessageChannel channel, String msg) {
        channel.sendMessage(msg).queue();
    }

    public static void deleteMessage(User usr, Long id) {
        usr.openPrivateChannel().complete().getMessageById(id).complete().delete().queue();
    }
}