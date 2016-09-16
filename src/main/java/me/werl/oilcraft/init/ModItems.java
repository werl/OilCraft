package me.werl.oilcraft.init;

import me.werl.oilcraft.items.ItemWrench;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashSet;
import java.util.Set;

public class ModItems {

    public static final Set<Item> ITEMS = new HashSet<>();

    public static final ItemWrench WRENCH;

    static {
        WRENCH = registerItem(new ItemWrench());
    }

    public static void registerItems() {
        // Dummy method to make sure the static initialiser runs
    }

    /**
     * Register an Item
     *
     * @param item The Item instance
     * @param <T>  The Item type
     * @return The Item instance
     */
    private static <T extends Item> T registerItem(T item) {
        GameRegistry.register(item);
        ITEMS.add(item);

        return item;
    }

}
