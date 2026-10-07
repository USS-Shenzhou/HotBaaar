package cn.ussshenzhou.hotbaaaar;

import cn.ussshenzhou.t88.config.ConfigHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

/**
 * @author USS_Shenzhou
 */
// The value here should match an entry in the META-INF/mods.toml file
@Mod(Hotbaaaar.MOD_ID)
public class Hotbaaaar
{
    public static final String MOD_ID = "hotbaaaar";

    //TODO 配置 数字键选择当前快捷栏
    //TODO 创造模式物品栏显示全部4格
    public Hotbaaaar(IEventBus modEventBus)
    {
        ConfigHelper.loadConfig(new HotbaaaarConfig());
    }
}
