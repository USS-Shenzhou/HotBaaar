package cn.ussshenzhou.hotbaaaar.util;

import java.security.InvalidParameterException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author USS_Shenzhou
 */
public class HotbarServerManager {
    private static final ConcurrentHashMap<UUID, Integer> CLIENT_PREFERRED_HOTBAR_AMOUNT = new ConcurrentHashMap<>();

    public static int getHotbaaaarAmount(UUID uuid) {
        if (uuid == null) {
            throw new InvalidParameterException("uuid is null on server");
        }
        return CLIENT_PREFERRED_HOTBAR_AMOUNT.getOrDefault(uuid, 4);
    }

    public static void setHotbaaaarAmount(UUID uuid, int amount) {
        CLIENT_PREFERRED_HOTBAR_AMOUNT.put(uuid, Math.clamp(amount, 1, 4));
    }
}
