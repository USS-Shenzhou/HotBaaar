package cn.ussshenzhou.hotbaaaar.util;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author USS_Shenzhou
 */
public class HotBaaaarServerManager {

    public static final ConcurrentHashMap<UUID, Integer> HOTBAR_AMOUNT = new ConcurrentHashMap<>();
}
