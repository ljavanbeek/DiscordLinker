package nl.minetopiasdb.discordbot.utils.link;

import nl.minetopiasdb.discordbot.utils.data.UserData;

import java.util.UUID;

public class DataLinkUtils {

    private static DataLinkUtils link = new DataLinkUtils();

    public static DataLinkUtils getInstance() {
        return link;
    }

    public boolean isLinked(long userid) {
        for (String key : UserData.getInstance().getData().getKeys(false)) {
            if (UserData.getInstance().getData().getLong(key + ".DiscordID") == userid) {
                return true;
            }
        }
        return false;
    }

    public UUID getUUIDFromDiscord(long userid) {
        for (String key : UserData.getInstance().getData().getKeys(false)) {
            if (UserData.getInstance().getData().getLong(key + ".DiscordID") == userid) {
                return UUID.fromString(key);
            }
        }
        return null;
    }


    public long getId(UUID mcUUID) {
        return UserData.getInstance().getData().getLong(mcUUID.toString() + ".DiscordID");
    }

    private void removeLink(UUID mcUUID) {
        UserData.getInstance().getData().set(mcUUID.toString() + ".DiscordID", null);
    }

    public void removeLink(long userId) {
        for (String key : UserData.getInstance().getData().getKeys(false)) {
            if (UserData.getInstance().getData().getLong(key + ".DiscordID") == userId) {
                removeLink(UUID.fromString(key));
            }
        }
    }

    public void setLink(UUID mcUUID, long discordId) {
        UserData.getInstance().getData().set(mcUUID + ".DiscordID", discordId);
        UserData.getInstance().saveData();
    }
}