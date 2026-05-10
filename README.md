My first plugin written in pure Java.

Plugin for nukkit-mot 26.20 core

Plugin API:

CREATE REGION METHOD
```java
package plugin.worldRegion.region;

public class CreateRegion {
    public static void createRegion(Location locate, Player player, String name) {}
}
```
Location locate - center creating region.
Player player - the player who creates the region.
String name - name region.

Build region, default flag:
```enum
AllFlagList.DropItems,    // Drop Items in region
AllFlagList.InputCommand, // Out command in region
AllFlagList.Walk          // Walk in region
```

CLASS REGION:
```java
package plugin.worldRegion.region;

public class Region {
    public String name;
    public String ownerNick;
    public UUID owner;

    public AllFlagList[] flags;
    public final int size;
    public final boolean regionIsOp;

    private String creatorNick;
    private UUID creator;

    private int[] position1 = new int[3];
    private int[] position2 = new int[3];
    private Region parent;
    private final List<Region> children;
    private String world;
    private int priority;

    public Region(String name,
                  int[] pos1, int[] pos2,
                  UUID creator, String creatorNick,
                  boolean creatorIsOwner,
                  AllFlagList[] defaultFlag,
                  String world, boolean creatorIsOp) {}
    
    public Map<String, UUID> getCreator() {}
    public int[] getPosition2() {}
    public int[] getPosition1() {}
    public String getWorldName() {}
    public boolean contains(int x, int y, int z) {}
    public boolean contains(Location location) {}
    public Location getCenter(Location template) {}
    public boolean addChild(Region child) {}
    public void removeChild(Region child) {}
    public Region getParent() {}
    public List<Region> getChildren() {}
    public int getPriority() {}

}
```

See the source code for details and methods.

CHECK REGION:
```java
package plugin.worldRegion.region;
public class CheckRegion {
    public static boolean isRegion(Location locate) {}
    public static Region getRegion(Location locate) {}
    public static List<Region> getRegionAt(Location locate) {}
    public static boolean isIntersectingWithOtherRegions(Location pos1, Location pos2,
                                                         Region excludeRegion) {}
    public static boolean isIntersectingWithOtherRegions(Location pos1, Location pos2) {}
    public static List<Region> getIntersectingRegions(Location pos1, Location pos2) {}
    private static boolean isLocationInRegion(Location location, Region region) {}
}
```