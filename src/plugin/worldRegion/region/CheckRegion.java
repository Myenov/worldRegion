package plugin.worldRegion.region;

import cn.nukkit.level.Location;

import java.util.ArrayList;
import java.util.List;

public class CheckRegion {
    public static boolean isRegion(Location locate) {
        return getRegion(locate) != null;
    }

    public static Region getRegion(Location locate) {
        for (Region region : StorageRegion.getRegions()) {
            if (isLocationInRegion(locate, region)) {
                return region;
            }
        }
        return null;
    }

    public static List<Region> getRegionAt(Location locate) {
        List<Region> regionsAtLocation = new ArrayList<>();
        for (Region region : StorageRegion.getRegions()) {
            if (isLocationInRegion(locate, region)) {
                regionsAtLocation.add(region);
            }
        }
        return regionsAtLocation;
    }

    private static boolean isLocationInRegion(Location location, Region region) {
        int[] pos1 = region.getPosition1();
        int[] pos2 = region.getPosition2();

        int minX = Math.min(pos1[0], pos2[0]);
        int maxX = Math.min(pos1[0], pos2[0]);

        int minY = Math.min(pos1[1], pos2[1]);
        int maxY = Math.min(pos1[1], pos2[1]);

        int minZ = Math.min(pos1[2], pos2[2]);
        int maxZ = Math.min(pos1[2], pos2[2]);

        int locX = location.getFloorX();
        int locY = location.getFloorY();
        int locZ = location.getFloorZ();

        return locX >= minX && locX <= maxX &&
               locY >= minY && locY <= maxY &&
               locZ >= minZ && locZ <= maxZ;
    }
}
