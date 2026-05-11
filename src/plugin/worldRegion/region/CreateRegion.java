package plugin.worldRegion.region;

import cn.nukkit.Player;
import cn.nukkit.level.Location;
import plugin.worldRegion.flags.Flag;
import plugin.worldRegion.region.reaction.SuccessfulCreation;
import plugin.worldRegion.region.reaction.UnsuccessfulCreation;

import java.util.List;
import java.util.UUID;

public class CreateRegion {
    public static void createRegion(Location locate, Player player, String name) {
        UUID playerUuid = player.getUniqueId();
        Location pos1 = StorageRegion.getPositionOneOnUUID(playerUuid);
        Location pos2 = StorageRegion.getPositionTwoOnUUID(playerUuid);
        if (pos1 == null || pos2 == null) {
            UnsuccessfulCreation.execute(player, "select two points");
            return;
        }

        Region existingRegion = CheckRegion.getRegion(locate);

        if (player.isOp()) {
            if (existingRegion != null) {
                createChildRegion(player, name, pos1, pos2, existingRegion);
            } else {
                if (intersectsWithOtherOpRegions(pos1, pos2, player));
            }
        }
        if (existingRegion != null) {
            if (existingRegion.owner.equals(playerUuid)) {
                createChildRegion(player, name, pos1, pos2, existingRegion);
            } else {
                UnsuccessfulCreation.execute(player, "this place is occupied by another region");
            }
        } else {
            if (CheckRegion.isIntersectingWithOtherRegions(pos1, pos2)) {
                List<Region> intersectingRegions = CheckRegion.getIntersectingRegions(pos1, pos2);
                StringBuilder regionsList = new StringBuilder();
                for (Region r : intersectingRegions) {
                    if (regionsList.length() > 0) regionsList.append(", ");
                    regionsList.append(r.name);
                }
                UnsuccessfulCreation.execute(player, "you region intersects with another");
                return;
            }
            createNormalRegion(player, name, pos1, pos2, false);
        }
    }
    private static boolean intersectsWithOtherOpRegions(Location pos1, Location pos2, Player player) {
        List<Region> intersectingRegion = CheckRegion.getIntersectingRegions(pos1, pos2);

        for (Region region : intersectingRegion) {
            if (region.regionIsOp) {
                UnsuccessfulCreation.execute(player, "cannot create a region on top of an administrative region");
                return true;
            }
        }
        return false;
    }

    private static void createNormalRegion(Player player, String name,
                                           Location pos1, Location pos2,
                                           boolean isOpRegion) {
        Region newRegion = new Region(
                name,
                new int[]{(int) pos1.x, (int) pos1.y, (int) pos1.z},
                new int[]{(int) pos2.x, (int) pos2.y, (int) pos2.z},
                player.getUniqueId(),
                player.getName(),
                true,
                new Flag[]{
                        Flag.DropItems,
                        Flag.InputCommand,
                        Flag.Walk
                },
                player.getLevel().getName(),
                isOpRegion
        );

        StorageRegion.addRegion(newRegion);
        if (isOpRegion) {
            SuccessfulCreation.execute(player, "admin region %" + name + "% creating");
        } else {
            SuccessfulCreation.execute(player, "region %" + name + "% creating");
        }
    }

    private static void createChildRegion(Player player, String name,
                                          Location pos1, Location pos2,
                                          Region parentRegion) {
        int minX = Math.min((int) pos1.x, (int) pos2.x);
        int maxX = Math.max((int) pos1.x, (int) pos2.x);
        int minY = Math.min((int) pos1.y, (int) pos2.y);
        int maxY = Math.max((int) pos1.y, (int) pos2.y);
        int minZ = Math.min((int) pos1.z, (int) pos2.z);
        int maxZ = Math.max((int) pos1.z, (int) pos2.z);

        if (!parentRegion.contains(minX, minY, maxZ) ||
                !parentRegion.contains(maxX, maxY, maxZ)) {
            UnsuccessfulCreation.execute(player, "A child region must be entirely contained within its parent region.");
            return;
        }
        for (Region sibling : parentRegion.getChildren()) {
            int[] sibPos1 = sibling.getPosition1();
            int[] sibPos2 = sibling.getPosition2();

            int sibMinX = Math.min(sibPos1[0], sibPos2[0]);
            int sibMaxX = Math.max(sibPos1[0], sibPos2[0]);
            int sibMinY = Math.min(sibPos1[1], sibPos2[1]);
            int sibMaxY = Math.max(sibPos1[1], sibPos2[1]);
            int sibMinZ = Math.min(sibPos1[2], sibPos2[2]);
            int sibMaxZ = Math.max(sibPos1[2], sibPos2[2]);

            if (minX <= sibMaxX && maxX >= sibMinX &&
                    minY <= sibMaxY && maxY >= sibMinY &&
                    minZ <= sibMaxZ && maxZ >= sibMinZ) {
                UnsuccessfulCreation.execute(player, "intersection with a subsidiary region");
                return;
            }
            if (CheckRegion.isIntersectingWithOtherRegions(pos1, pos2, parentRegion)) {
                List<Region> intersecting = CheckRegion.getIntersectingRegions(pos1, pos2);
                intersecting.remove(parentRegion);
                if (!intersecting.isEmpty()) {
                    StringBuilder names = new StringBuilder();
                    for (Region r : intersecting) {
                        if (!names.isEmpty()) names.append(", ");
                        names.append(r.name);
                    }
                    UnsuccessfulCreation.execute(player, "Intersection with regions outside the parent");
                    return;
                }
            }

            Region childRegion = new Region(
                    name,
                    new int[]{(int) pos1.x, (int) pos1.y, (int) pos1.z},
                    new int[]{(int) pos2.x, (int) pos2.y, (int) pos2.z},
                    player.getUniqueId(),
                    player.getName(),
                    true,
                    (Flag[]) parentRegion.flags.toArray(),
                    player.getLevel().getName(),
                    player.isOp()
            );

            if (parentRegion.addChild(childRegion)) {
                StorageRegion.addRegion(childRegion);
                SuccessfulCreation.execute(player, "Nested region%" + name + "%created inside %" + parentRegion.name + "%!");
            } else {
                UnsuccessfulCreation.execute(player, "Error creating nested region!");
            }
        }
    }
}
