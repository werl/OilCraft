package me.werl.oilcraft.util;

import net.minecraft.util.IStringSerializable;

public enum  EnumIoMode implements IStringSerializable {

    NONE("none"),                       // does nothing specail by default
    DISABLED("disabled"),               // can't do anything with that side
    INSERT("insert"),                   // can insert but does not pull
    EXTRACT("extract"),                 // can extract but does not push
    INSERT_EXTRACT("insert_extract"),   // can insert and extract but does not do so automatically
    PULL("pull"),                       // pulls from adjacent blocks
    PUSH("push"),                       // pushes to adjacent blocks
    PUSH_PULL("push_pull");             // pushes outputs and pulls in required ingredients

    private final String name;

    EnumIoMode(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getUnlocalizedName() {
        return "gui.machine.ioMode." + name;
    }

     public EnumIoMode getNext() {
         switch (this) {
             case NONE:
                 return DISABLED;
             case DISABLED:
                 return INSERT;
             case INSERT:
                 return EXTRACT;
             case EXTRACT:
                 return INSERT_EXTRACT;
             case PULL:
                 return PUSH;
             case PUSH:
                 return PUSH_PULL;
             case PUSH_PULL:
                 return NONE;
             default:
                 return DISABLED;
         }
     }

    public static EnumIoMode getModeFromString(String mode) {
        switch (mode) {
            case "none":
                return NONE;
            case "disabled":
                return DISABLED;
            case "insert":
                return INSERT;
            case "extract":
                return EXTRACT;
            case "insert_extract":
                return INSERT_EXTRACT;
            case "pull":
                return PULL;
            case "push":
                return PUSH;
            case "push_pull":
                return PUSH_PULL;
            default:
                return NONE;
        }
    }
}
