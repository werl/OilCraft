package me.werl.oilcraft.util;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemStackUtil {

    public static boolean hasCapabilityAnySide(Capability<?> capability, ICapabilityProvider provider) {
        for (EnumFacing f: EnumFacing.VALUES) {
            if(provider.hasCapability(capability, f)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getCapabilityAnySide (Capability<T> cap, ICapabilityProvider provider) {
        for(EnumFacing f: EnumFacing.VALUES) {
            if(provider.getCapability(cap, f) != null) {
                return provider.getCapability(cap, f);
            }
        }
        return null;
    }

}
