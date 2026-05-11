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

    public static boolean isIntersectingWithOtherRegions(Location pos1, Location pos2,
                                                        Region excludeRegion) {
        int newMinX = Math.min(pos1.getFloorX(), pos2.getFloorX());
        int newMaxX = Math.max(pos1.getFloorX(), pos2.getFloorX());
        int newMinY = Math.min(pos1.getFloorY(), pos2.getFloorY());
        int newMaxY = Math.max(pos1.getFloorY(), pos2.getFloorY());
        int newMinZ = Math.min(pos1.getFloorZ(), pos2.getFloorZ());
        int newMaxZ = Math.max(pos1.getFloorZ(), pos2.getFloorZ());

        for (Region existingRegion : StorageRegion.getRegions()) {
            if (existingRegion != null && existingRegion == excludeRegion) {
                continue;
            }

            if (pos1.getLevel() != null &&
                    !pos1.getLevel().getName().equals(existingRegion.getWorldName())) {
                continue;
            }
            int[] existPos1 = existingRegion.getPosition1();
            int[] existPos2 = existingRegion.getPosition2();

            int existMinX = Math.min(existPos1[0], existPos2[0]);
            int existMaxX = Math.max(existPos1[0], existPos2[0]);
            int existMinY = Math.min(existPos1[1], existPos2[1]);
            int existMaxY = Math.max(existPos1[1], existPos2[1]);
            int existMinZ = Math.min(existPos1[2], existPos2[2]);
            int existMaxZ = Math.max(existPos1[2], existPos2[2]);

            boolean intersects =
                    newMinX <= existMaxX && newMaxX >= existMinX &&
                            newMinY <= existMaxY && newMaxY >= existMinY &&
                            newMinZ <= existMaxZ && newMaxZ >= existMinZ;

            if (intersects) {
                return true;
            }
        }

        return false;
    }

    public static boolean isIntersectingWithOtherRegions(Location pos1, Location pos2) {
        return isIntersectingWithOtherRegions(pos1,pos2,null);
    }

    public static List<Region> getIntersectingRegions(Location pos1, Location pos2) {
        List<Region> intersectingRegions = new ArrayList<>();

        int newMinX = Math.min(pos1.getFloorX(), pos2.getFloorX());
        int newMaxX = Math.max(pos1.getFloorX(), pos2.getFloorX());
        int newMinY = Math.min(pos1.getFloorY(), pos2.getFloorY());
        int newMaxY = Math.max(pos1.getFloorY(), pos2.getFloorY());
        int newMinZ = Math.min(pos1.getFloorZ(), pos2.getFloorZ());
        int newMaxZ = Math.max(pos1.getFloorZ(), pos2.getFloorZ());

        for (Region existingRegion : StorageRegion.getRegions()) {
            if (pos1.getLevel() != null &&
            !pos1.getLevel().getName().equals(existingRegion.getWorldName())) {
                continue;
            }
            int[] existPos1 = existingRegion.getPosition1();
            int[] existPos2 = existingRegion.getPosition2();

            int existMinX = Math.min(existPos1[0], existPos2[0]);
            int existMaxX = Math.max(existPos1[0], existPos2[0]);
            int existMinY = Math.min(existPos1[1], existPos2[1]);
            int existMaxY = Math.max(existPos1[1], existPos2[1]);
            int existMinZ = Math.min(existPos1[2], existPos2[2]);
            int existMaxZ = Math.max(existPos1[2], existPos2[2]);

            boolean intersects =
                    newMinX <= existMaxX && newMaxX >= existMinX &&
                    newMinY <= existMaxY && newMaxY >= existMinY &&
                    newMinZ <= existMaxZ && newMaxZ >= existMinZ;

            if (intersects) {
                intersectingRegions.add(existingRegion);
            }
        }
        return intersectingRegions;
    }

    private static boolean isLocationInRegion(Location location, Region region) {
        int[] pos1 = region.getPosition1();
        int[] pos2 = region.getPosition2();

        int minX = Math.min(pos1[0], pos2[0]);
        int maxX = Math.max(pos1[0], pos2[0]);

        int minY = Math.min(pos1[1], pos2[1]);
        int maxY = Math.max(pos1[1], pos2[1]);

        int minZ = Math.min(pos1[2], pos2[2]);
        int maxZ = Math.max(pos1[2], pos2[2]);

        int locX = location.getFloorX();
        int locY = location.getFloorY();
        int locZ = location.getFloorZ();

        return locX >= minX && locX <= maxX &&
               locY >= minY && locY <= maxY &&
               locZ >= minZ && locZ <= maxZ;
    }
}
