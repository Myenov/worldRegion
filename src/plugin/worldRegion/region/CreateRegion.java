package plugin.worldRegion.region;

import cn.nukkit.Player;
import cn.nukkit.level.Location;
import cn.nukkit.utils.TextFormat;
import plugin.worldRegion.flags.AllFlagList;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CreateRegion {
    public static void createRegion(Location locate, Player player, String name) {
        UUID playerUuid = player.getUniqueId();
        Location pos1 = StorageRegion.getPositionOneOnUUID(playerUuid);
        Location pos2 = StorageRegion.getPositionTwoOnUUID(playerUuid);
        if (pos1 == null || pos2 == null) {
            player.sendMessage(
                    TextFormat.RED + "" + TextFormat.BOLD +
                            "Сначала выберите две точки!");
            return;
        }

        Region existingRegion = CheckRegion.getRegion(locate);

        if (player.isOp()) {
            if (existingRegion != null) {
                createChildRegion(player, name, pos1, pos2, existingRegion);
            } else {
                if (intersectsWithOtherOpRegions(pos1, pos2, player));
            } /*
                StorageRegion.addRegion(
                        new Region(
                            name,
                                new int[]{(int) pos1.x, (int)pos1.y, (int)pos1.z},
                                new int[]{(int) pos2.x, (int)pos2.y, (int)pos2.z},
                                playerUuid, player.getName(), true,
                                new AllFlagList[]{AllFlagList.DropItems,
                                        AllFlagList.InputCommand, AllFlagList.Walk}, player.getLevel().getName(),
                                true

                        )
                );*/
        }
        if (existingRegion != null) {
            if (existingRegion.owner.equals(playerUuid)) {
                createChildRegion(player, name, pos1, pos2, existingRegion);
            } else {
                player.sendMessage(TextFormat.RED +
                        "Это место занято регионом '" + existingRegion.name + "'! " +
                        "Вы можете создавать вложенные регионы только внутри своих.");
                // переработать
            }
        } else {
            if (CheckRegion.isIntersectingWithOtherRegions(pos1, pos2)) {
                List<Region> intersectingRegions = CheckRegion.getIntersectingRegions(pos1, pos2);
                StringBuilder regionsList = new StringBuilder();
                for (Region r : intersectingRegions) {
                    if (regionsList.length() > 0) regionsList.append(", ");
                    regionsList.append(r.name);
                }
                player.sendMessage(TextFormat.RED +
                        "Ваш регион пересекается с существующими: " + regionsList.toString());
                return; // переписать
            }
            createNormalRegion(player, name, pos1, pos2, false);
        }
    }
    private static boolean intersectsWithOtherOpRegion(Location pos1, Location pos2, Player player) {
        List<Region> intersectingRegion = CheckRegion.getIntersectingRegions(pos1, pos2);

        for (Region region : intersectingRegion) {
            if (region.regionIsOp) {
                player.sendMessage(TextFormat.RED +
                        "Нельзя создать регион поверх административного региона '" +
                        region.name + "'! Используйте вложенный регион.");
                return true;
                //переписать
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
                new AllFlagList[]{
                        AllFlagList.DropItems,
                        AllFlagList.InputCommand,
                        AllFlagList.Walk
                },
                player.getLevel().getName(),
                isOpRegion
        );

        StorageRegion.addRegion(newRegion);
        if (isOpRegion) {
            player.sendMessage(TextFormat.GOLD +
                    "Административный регион '" + name + "' создан!");

            //переписать
        } else {
            player.sendMessage(TextFormat.GREEN +
                    "Регион '" + name + "' успешно создан!");
            //переписать
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
            player.sendMessage(TextFormat.RED +
                    "Дочерний регион должен полностью находиться внутри родительского '" +
                    parentRegion.name + "'!");
            return; //переписать
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
                player.sendMessage(TextFormat.RED +
                        "Пересечение с другим дочерним регионом '" + sibling.name + "'!");
                return; //Переписать
            }
            if (CheckRegion.isIntersectingWithOtherRegions(pos1, pos2, parentRegion)) {
                List<Region> intersecting = CheckRegion.getIntersectingRegions(pos1, pos2);
                intersecting.remove(parentRegion);
                if (!intersecting.isEmpty()) {
                    StringBuilder names = new StringBuilder();
                    for (Region r : intersecting) {
                        if (names.length() > 0) names.append(", ");
                        names.append(r.name);
                    }
                    player.sendMessage(TextFormat.RED +
                            "Пересечение с регионами вне родительского: " + names.toString());
                    return; // переписать
                }
            }

            Region childRegion = new Region(
                    name,
                    new int[]{(int) pos1.x, (int) pos1.y, (int) pos1.z},
                    new int[]{(int) pos2.x, (int) pos2.y, (int) pos2.z},
                    player.getUniqueId(),
                    player.getName(),
                    true,
                    parentRegion.flags.clone(),
                    player.getLevel().getName(),
                    player.isOp()
            );

            if (parentRegion.addChild(childRegion)) {
                StorageRegion.addRegion(childRegion);
                player.sendMessage(TextFormat.GREEN +
                        "Вложенный регион '" + name + "' создан внутри '" +
                        parentRegion.name + "'!"); //переписать
            } else {
                player.sendMessage(TextFormat.RED +
                        "Ошибка при создании вложенного региона!"); //переписать
            }
        }
    }
}
