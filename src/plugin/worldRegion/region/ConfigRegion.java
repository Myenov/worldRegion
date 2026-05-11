package plugin.worldRegion.region;

import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import plugin.worldRegion.MainPlugin;
import plugin.worldRegion.flags.Flag;
import plugin.worldRegion.utils.GetUserLanguage;

import java.io.File;
import java.util.*;

public class ConfigRegion {

    private static Config config;

    public static void init() {
        File configFile = new File(MainPlugin.getInstance().getDataFolder(), "regions.yml");
        config = new Config(configFile, Config.YAML);
    }

    public static void reload() {
        init();
    }

    public static void save() {
        if (config != null) {
            config.save();
        }
    }

    public static Config getConfig() {
        return config;
    }

    private static List<Integer> arrayToList(int[] array) {
        List<Integer> list = new ArrayList<>();
        for (int i : array) {
            list.add(i);
        }
        return list;
    }

    private static int[] listToArray(List<Integer> list) {
        int[] array = new int[3];
        for (int i = 0; i < 3 && i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    private static String base(String regionName) {
        return "regions." + regionName;
    }

    // ==================== ADD ====================

    public static void addRegion(Region region) {
        String b = base(region.name);
        UUID creatorUUID = region.getCreatorUUID();

        config.set(b + ".name", region.name);
        config.set(b + ".world", region.getWorldName());
        config.set(b + ".position.posOne", arrayToList(region.getPosition1()));
        config.set(b + ".position.posTwo", arrayToList(region.getPosition2()));
        config.set(b + ".position.size", region.size);

        config.set(b + ".users.creator.creatorName", region.getCreatorName());
        config.set(b + ".users.creator.creatorUUID", creatorUUID.toString());
        config.set(b + ".users.creator.isOP", region.regionIsOp && creatorUUID.equals(region.owner));

        if (region.owner != null) {
            config.set(b + ".users.owner.ownerName", region.ownerNick);
            config.set(b + ".users.owner.ownerUUID", region.owner.toString());
            config.set(b + ".users.owner.isOP", region.regionIsOp && creatorUUID.equals(region.owner));
        } else {
            config.set(b + ".users.owner.ownerName", region.getCreatorName());
            config.set(b + ".users.owner.ownerUUID", creatorUUID.toString());
            config.set(b + ".users.owner.isOP", false);
        }

        config.set(b + ".users.residents", new ArrayList<>(region.residents));
        config.set(b + ".users.guests", new ArrayList<>(region.guests));

        List<String> flagNames = new ArrayList<>();
        for (Flag flag : region.flags) {
            flagNames.add(flag.toString());
        }
        config.set(b + ".flags", flagNames);

        config.set(b + ".meta.parent", region.getParent() != null ? region.getParent().name : null);

        List<String> childrenNames = new ArrayList<>();
        for (Region child : region.getChildren()) {
            childrenNames.add(child.name);
        }
        config.set(b + ".meta.children", childrenNames);
        config.set(b + ".meta.priority", region.getPriority());
        config.set(b + ".meta.regionIsOP", region.regionIsOp);
    }

    public static void addRegionRaw(String name, String world, int[] pos1, int[] pos2, int size,
                                    String creatorName, String creatorUUID, boolean creatorIsOp,
                                    String ownerName, String ownerUUID, boolean ownerIsOp,
                                    List<String> residents, List<String> guests,
                                    List<String> flags, String parent, List<String> children,
                                    int priority, boolean regionIsOp) {
        String b = base(name);

        config.set(b + ".name", name);
        config.set(b + ".world", world);
        config.set(b + ".position.posOne", arrayToList(pos1));
        config.set(b + ".position.posTwo", arrayToList(pos2));
        config.set(b + ".position.size", size);

        config.set(b + ".users.creator.creatorName", creatorName);
        config.set(b + ".users.creator.creatorUUID", creatorUUID);
        config.set(b + ".users.creator.isOP", creatorIsOp);

        config.set(b + ".users.owner.ownerName", ownerName);
        config.set(b + ".users.owner.ownerUUID", ownerUUID);
        config.set(b + ".users.owner.isOP", ownerIsOp);

        config.set(b + ".users.residents", new ArrayList<>(residents));
        config.set(b + ".users.guests", new ArrayList<>(guests));
        config.set(b + ".flags", new ArrayList<>(flags));
        config.set(b + ".meta.parent", parent);
        config.set(b + ".meta.children", new ArrayList<>(children));
        config.set(b + ".meta.priority", priority);
        config.set(b + ".meta.regionIsOP", regionIsOp);
    }

    public static void addResident(String regionName, String playerName) {
        String b = base(regionName);
        if (config.exists(b)) {
            List<String> residents = config.getStringList(b + ".users.residents");
            if (!residents.contains(playerName)) {
                residents.add(playerName);
                config.set(b + ".users.residents", residents);
            }
        }
    }

    public static void addGuest(String regionName, String playerName) {
        String b = base(regionName);
        if (config.exists(b)) {
            List<String> guests = config.getStringList(b + ".users.guests");
            if (!guests.contains(playerName)) {
                guests.add(playerName);
                config.set(b + ".users.guests", guests);
            }
        }
    }

    public static void addFlag(String regionName, String flagName) {
        String b = base(regionName);
        if (config.exists(b)) {
            List<String> flags = config.getStringList(b + ".flags");
            if (!flags.contains(flagName)) {
                flags.add(flagName);
                config.set(b + ".flags", flags);
            }
        }
    }

    public static void addChild(String regionName, String childName) {
        String b = base(regionName);
        if (config.exists(b)) {
            List<String> children = config.getStringList(b + ".meta.children");
            if (!children.contains(childName)) {
                children.add(childName);
                config.set(b + ".meta.children", children);
            }
        }
    }

    // ==================== REMOVE ====================

    public static void removeRegion(String regionName) {
        config.remove(base(regionName));
    }

    public static void removeAllRegions() {
        config.remove("regions");
    }

    public static void removeResident(String regionName, String playerName) {
        String b = base(regionName);
        if (config.exists(b)) {
            List<String> residents = config.getStringList(b + ".users.residents");
            residents.remove(playerName);
            config.set(b + ".users.residents", residents);
        }
    }

    public static void removeGuest(String regionName, String playerName) {
        String b = base(regionName);
        if (config.exists(b)) {
            List<String> guests = config.getStringList(b + ".users.guests");
            guests.remove(playerName);
            config.set(b + ".users.guests", guests);
        }
    }

    public static void removeFlag(String regionName, String flagName) {
        String b = base(regionName);
        if (config.exists(b)) {
            List<String> flags = config.getStringList(b + ".flags");
            flags.remove(flagName);
            config.set(b + ".flags", flags);
        }
    }

    public static void removeChild(String regionName, String childName) {
        String b = base(regionName);
        if (config.exists(b)) {
            List<String> children = config.getStringList(b + ".meta.children");
            children.remove(childName);
            config.set(b + ".meta.children", children);
        }
    }

    public static void removeParent(String regionName) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".meta.parent", null);
        }
    }

    public static void removeOwner(String regionName) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".users.owner.ownerName", null);
            config.set(b + ".users.owner.ownerUUID", null);
            config.set(b + ".users.owner.isOP", false);
        }
    }

    public static void clearResidents(String regionName) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".users.residents", new ArrayList<>());
        }
    }

    public static void clearGuests(String regionName) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".users.guests", new ArrayList<>());
        }
    }

    public static void clearFlags(String regionName) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".flags", new ArrayList<>());
        }
    }

    public static void clearChildren(String regionName) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".meta.children", new ArrayList<>());
        }
    }

    // ==================== UPDATE ====================

    public static void updateRegion(Region region) {
        removeRegion(region.name);
        addRegion(region);
    }

    public static void updateRegionName(String oldName, String newName) {
        String oldBase = base(oldName);
        if (config.exists(oldBase)) {
            Map<String, Object> data = new LinkedHashMap<>(((ConfigSection) config.get(oldBase)).getAllMap());
            config.remove(oldBase);
            config.set(base(newName), data);
            config.set(base(newName) + ".name", newName);
        }
    }

    public static void updateRegionWorld(String regionName, String world) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".world", world);
        }
    }

    public static void updateRegionPosition(String regionName, int[] pos1, int[] pos2) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".position.posOne", arrayToList(pos1));
            config.set(b + ".position.posTwo", arrayToList(pos2));
            int size = Math.abs(pos1[0] - pos2[0]) * Math.abs(pos1[1] - pos2[1]) * Math.abs(pos1[2] - pos2[2]);
            config.set(b + ".position.size", size);
        }
    }

    public static void updateRegionPosOne(String regionName, int x, int y, int z) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".position.posOne", Arrays.asList(x, y, z));
        }
    }

    public static void updateRegionPosTwo(String regionName, int x, int y, int z) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".position.posTwo", Arrays.asList(x, y, z));
        }
    }

    public static void updateRegionSize(String regionName, int size) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".position.size", size);
        }
    }

    public static void updateRegionCreator(String regionName, String creatorName, String creatorUUID) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".users.creator.creatorName", creatorName);
            config.set(b + ".users.creator.creatorUUID", creatorUUID);
        }
    }

    public static void updateRegionCreatorName(String regionName, String creatorName) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".users.creator.creatorName", creatorName);
        }
    }

    public static void updateRegionCreatorUUID(String regionName, String creatorUUID) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".users.creator.creatorUUID", creatorUUID);
        }
    }

    public static void updateRegionCreatorIsOp(String regionName, boolean isOp) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".users.creator.isOP", isOp);
        }
    }

    public static void updateRegionOwner(String regionName, String ownerName, String ownerUUID) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".users.owner.ownerName", ownerName);
            config.set(b + ".users.owner.ownerUUID", ownerUUID);
        }
    }

    public static void updateRegionOwnerName(String regionName, String ownerName) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".users.owner.ownerName", ownerName);
        }
    }

    public static void updateRegionOwnerUUID(String regionName, String ownerUUID) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".users.owner.ownerUUID", ownerUUID);
        }
    }

    public static void updateRegionOwnerIsOp(String regionName, boolean isOp) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".users.owner.isOP", isOp);
        }
    }

    public static void updateRegionResidents(String regionName, List<String> residents) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".users.residents", new ArrayList<>(residents));
        }
    }

    public static void updateRegionGuests(String regionName, List<String> guests) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".users.guests", new ArrayList<>(guests));
        }
    }

    public static void updateRegionFlags(String regionName, List<String> flags) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".flags", new ArrayList<>(flags));
        }
    }

    public static void updateRegionParent(String regionName, String parentName) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".meta.parent", parentName);
        }
    }

    public static void updateRegionChildren(String regionName, List<String> childrenNames) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".meta.children", new ArrayList<>(childrenNames));
        }
    }

    public static void updateRegionPriority(String regionName, int priority) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".meta.priority", priority);
        }
    }

    public static void updateRegionIsOp(String regionName, boolean regionIsOp) {
        String b = base(regionName);
        if (config.exists(b)) {
            config.set(b + ".meta.regionIsOP", regionIsOp);
        }
    }

    public static void setResident(String regionName, String playerName, boolean value) {
        if (value) {
            addResident(regionName, playerName);
        } else {
            removeResident(regionName, playerName);
        }
    }

    public static void setGuest(String regionName, String playerName, boolean value) {
        if (value) {
            addGuest(regionName, playerName);
        } else {
            removeGuest(regionName, playerName);
        }
    }

    public static void setFlag(String regionName, String flagName, boolean value) {
        if (value) {
            addFlag(regionName, flagName);
        } else {
            removeFlag(regionName, flagName);
        }
    }

    // ==================== GET ====================

    public static Region getRegion(String regionName) {
        String b = base(regionName);
        if (!config.exists(b)) {
            return null;
        }

        String name = config.getString(b + ".name");
        String world = config.getString(b + ".world");

        List<Integer> posOneList = config.getIntegerList(b + ".position.posOne");
        List<Integer> posTwoList = config.getIntegerList(b + ".position.posTwo");
        int[] pos1 = listToArray(posOneList);
        int[] pos2 = listToArray(posTwoList);

        String creatorName = config.getString(b + ".users.creator.creatorName");
        String creatorUUIDStr = config.getString(b + ".users.creator.creatorUUID");
        UUID creatorUUID = UUID.fromString(creatorUUIDStr);
        boolean creatorIsOp = config.getBoolean(b + ".users.creator.isOP");

        List<String> flagNames = config.getStringList(b + ".flags");
        Flag[] flags = new Flag[flagNames.size()];
        for (int i = 0; i < flagNames.size(); i++) {
            flags[i] = Flag.values()[Integer.parseInt(flagNames.get(i))];
        }

        Region region = new Region(name, pos1, pos2, creatorUUID, creatorName, true, flags, world, creatorIsOp);

        if (config.exists(b + ".users.owner")) {
            String ownerName = config.getString(b + ".users.owner.ownerName");
            String ownerUUIDStr = config.getString(b + ".users.owner.ownerUUID");
            if (ownerUUIDStr != null && !ownerUUIDStr.isEmpty()) {
                region.owner = UUID.fromString(ownerUUIDStr);
                region.ownerNick = ownerName;
            }
        }

        List<String> residents = config.getStringList(b + ".users.residents");
        region.residents.clear();
        region.residents.addAll(residents);

        List<String> guests = config.getStringList(b + ".users.guests");
        region.guests.clear();
        region.guests.addAll(guests);

        return region;
    }

    public static boolean regionExists(String regionName) {
        return config.exists(base(regionName));
    }

    public static String getRegionName(String regionName) {
        return config.getString(base(regionName) + ".name");
    }

    public static String getRegionWorld(String regionName) {
        return config.getString(base(regionName) + ".world");
    }

    public static int[] getRegionPosOne(String regionName) {
        List<Integer> list = config.getIntegerList(base(regionName) + ".position.posOne");
        return listToArray(list);
    }

    public static int[] getRegionPosTwo(String regionName) {
        List<Integer> list = config.getIntegerList(base(regionName) + ".position.posTwo");
        return listToArray(list);
    }

    public static int getRegionPosOneX(String regionName) {
        return config.getInt(base(regionName) + ".position.posOne.0");
    }

    public static int getRegionPosOneY(String regionName) {
        return config.getInt(base(regionName) + ".position.posOne.1");
    }

    public static int getRegionPosOneZ(String regionName) {
        return config.getInt(base(regionName) + ".position.posOne.2");
    }

    public static int getRegionPosTwoX(String regionName) {
        return config.getInt(base(regionName) + ".position.posTwo.0");
    }

    public static int getRegionPosTwoY(String regionName) {
        return config.getInt(base(regionName) + ".position.posTwo.1");
    }

    public static int getRegionPosTwoZ(String regionName) {
        return config.getInt(base(regionName) + ".position.posTwo.2");
    }

    public static int getRegionSize(String regionName) {
        return config.getInt(base(regionName) + ".position.size");
    }

    public static String getRegionCreatorName(String regionName) {
        return config.getString(base(regionName) + ".users.creator.creatorName");
    }

    public static String getRegionCreatorUUID(String regionName) {
        return config.getString(base(regionName) + ".users.creator.creatorUUID");
    }

    public static boolean getRegionCreatorIsOp(String regionName) {
        return config.getBoolean(base(regionName) + ".users.creator.isOP");
    }

    public static String getRegionOwnerName(String regionName) {
        return config.getString(base(regionName) + ".users.owner.ownerName");
    }

    public static String getRegionOwnerUUID(String regionName) {
        return config.getString(base(regionName) + ".users.owner.ownerUUID");
    }

    public static boolean getRegionOwnerIsOp(String regionName) {
        return config.getBoolean(base(regionName) + ".users.owner.isOP");
    }

    public static List<String> getRegionResidents(String regionName) {
        return config.getStringList(base(regionName) + ".users.residents");
    }

    public static List<String> getRegionGuests(String regionName) {
        return config.getStringList(base(regionName) + ".users.guests");
    }

    public static List<String> getRegionFlags(String regionName) {
        return config.getStringList(base(regionName) + ".flags");
    }

    public static String getRegionParent(String regionName) {
        return config.getString(base(regionName) + ".meta.parent");
    }

    public static List<String> getRegionChildren(String regionName) {
        return config.getStringList(base(regionName) + ".meta.children");
    }

    public static int getRegionPriority(String regionName) {
        return config.getInt(base(regionName) + ".meta.priority");
    }

    public static boolean getRegionIsOp(String regionName) {
        return config.getBoolean(base(regionName) + ".meta.regionIsOP");
    }

    public static boolean isResident(String regionName, String playerName) {
        return getRegionResidents(regionName).contains(playerName);
    }

    public static boolean isGuest(String regionName, String playerName) {
        return getRegionGuests(regionName).contains(playerName);
    }

    public static boolean hasFlag(String regionName, String flagName) {
        return getRegionFlags(regionName).contains(flagName);
    }

    public static boolean isOwner(String regionName, String playerUUID) {
        String ownerUUID = getRegionOwnerUUID(regionName);
        return ownerUUID != null && ownerUUID.equals(playerUUID);
    }

    public static boolean isCreator(String regionName, String playerUUID) {
        String creatorUUID = getRegionCreatorUUID(regionName);
        return creatorUUID != null && creatorUUID.equals(playerUUID);
    }

    // ==================== LIST ====================

    public static Set<String> getAllRegionNames() {
        ConfigSection regions = config.getSection("regions");
        if (regions == null) {
            return new HashSet<>();
        }
        return new HashSet<>(regions.getKeys(false));
    }

    public static List<String> getRegionNamesList() {
        return new ArrayList<>(getAllRegionNames());
    }

    public static int getRegionCount() {
        return getAllRegionNames().size();
    }

    public static List<String> getRegionsByWorld(String world) {
        List<String> result = new ArrayList<>();
        for (String name : getAllRegionNames()) {
            if (world.equals(getRegionWorld(name))) {
                result.add(name);
            }
        }
        return result;
    }

    public static List<String> getRegionsByOwner(String ownerUUID) {
        List<String> result = new ArrayList<>();
        for (String name : getAllRegionNames()) {
            if (ownerUUID.equals(getRegionOwnerUUID(name))) {
                result.add(name);
            }
        }
        return result;
    }

    public static List<String> getRegionsByCreator(String creatorUUID) {
        List<String> result = new ArrayList<>();
        for (String name : getAllRegionNames()) {
            if (creatorUUID.equals(getRegionCreatorUUID(name))) {
                result.add(name);
            }
        }
        return result;
    }

    public static List<String> getRegionsByPlayer(String playerName) {
        List<String> result = new ArrayList<>();
        for (String name : getAllRegionNames()) {
            if (isResident(name, playerName) || isGuest(name, playerName) ||
                    playerName.equals(getRegionOwnerName(name)) || playerName.equals(getRegionCreatorName(name))) {
                result.add(name);
            }
        }
        return result;
    }

    public static List<String> getRegionsByFlag(String flagName) {
        List<String> result = new ArrayList<>();
        for (String name : getAllRegionNames()) {
            if (hasFlag(name, flagName)) {
                result.add(name);
            }
        }
        return result;
    }

    public static List<String> getRegionsByParent(String parentName) {
        List<String> result = new ArrayList<>();
        for (String name : getAllRegionNames()) {
            if (parentName.equals(getRegionParent(name))) {
                result.add(name);
            }
        }
        return result;
    }

    public static List<String> getRegionsByPriority(int priority) {
        List<String> result = new ArrayList<>();
        for (String name : getAllRegionNames()) {
            if (getRegionPriority(name) == priority) {
                result.add(name);
            }
        }
        return result;
    }

    public static List<String> getRegionsByIsOp(boolean isOp) {
        List<String> result = new ArrayList<>();
        for (String name : getAllRegionNames()) {
            if (getRegionIsOp(name) == isOp) {
                result.add(name);
            }
        }
        return result;
    }

    public static Map<String, Object> getRegionRawData(String regionName) {
        ConfigSection section = config.getSection(base(regionName));
        if (section != null) {
            return new LinkedHashMap<>(section.getAllMap());
        }
        return null;
    }

    public static boolean isRegionEmpty() {
        return getRegionCount() == 0;
    }
}