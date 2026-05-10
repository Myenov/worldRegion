package plugin.worldRegion.region;

import cn.nukkit.level.Location;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StorageRegion {
    private static final Map<UUID, Location> Position1 = new ConcurrentHashMap<>();
    private static final Map<UUID, Location> Position2 = new ConcurrentHashMap<>();
    public static final List<Region> Regions = new ArrayList<>();

    public static void addRegion(Region region) {
        Regions.add(region);
    }

    public static void removeRegion(Region region) {
        Regions.remove(region);
    }

    public static List<Region> getRegions() {
        return new ArrayList<>(Regions);
    }

    public static void addPosition1(UUID player, Location locate) {
        Position1.put(player,locate);
    }

    public static void addPosition2(UUID player, Location locate) {
        Position2.put(player,locate);
    }

    public static Map<UUID, Location> getPosition1() {
        return new ConcurrentHashMap<>(Position1);
    }

    public static Map<UUID, Location> getPosition2() {
        return new ConcurrentHashMap<>(Position2);
    }

    public static Location getPositionOneOnUUID(UUID player) {
        return Position1.get(player);
    }

    public static Location getPositionTwoOnUUID(UUID player) {
        return Position2.get(player);
    }
}