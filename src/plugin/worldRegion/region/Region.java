package plugin.worldRegion.region;

import cn.nukkit.level.Location;
import plugin.worldRegion.flags.Flag;

import java.util.*;

public class Region {
    public String name;
    public String ownerNick;
    public UUID owner;

    public List<Flag> flags = new ArrayList<>();
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
                  int posX1, int posY1, int posZ1,
                  int posX2, int posY2, int posZ2,
                  UUID creator, String creatorNick,
                  boolean creatorIsOwner,
                  Flag[] defaultFlag,
                  String world) {
        this.name = name;
        this.position1 = new int[]{posX1, posY1, posZ1};
        this.position2 = new int[]{posX2, posY2, posZ2};
        this.size = addSize();
        this.creator = creator;
        this.creatorNick = creatorNick;
        if (creatorIsOwner) {
            this.owner = creator;
            this.ownerNick = creatorNick;
        }
        this.flags = List.of(defaultFlag);
        this.world = world;
        this.regionIsOp = false;
        this.children = new ArrayList<>();
        this.parent = null;
        this.priority = 0;
    }

    public Region(String name,
                  int[] pos1, int[] pos2,
                  UUID creator, String creatorNick,
                  boolean creatorIsOwner,
                  Flag[] defaultFlag,
                  String world) {
        this.name = name;
        this.position1 = pos1;
        this.position2 = pos2;
        this.creator = creator;
        this.creatorNick = creatorNick;
        this.size = addSize();
        if (creatorIsOwner) {
            this.owner = creator;
            this.ownerNick = creatorNick;
        }
        this.flags = List.of(defaultFlag);
        this.world = world;
        this.regionIsOp = false;
        this.children = new ArrayList<>();
        this.parent = null;
        this.priority = 0;
    }

    public Region(String name,
                  int[] pos1, int[] pos2,
                  UUID creator, String creatorNick,
                  boolean creatorIsOwner,
                  Flag[] defaultFlag,
                  String world, boolean creatorIsOp) {
        this.name = name;
        this.position1 = pos1;
        this.position2 = pos2;
        this.creator = creator;
        this.creatorNick = creatorNick;
        this.size = addSize();
        if (creatorIsOwner) {
            this.owner = creator;
            this.ownerNick = creatorNick;
        }
        this.flags = List.of(defaultFlag);
        this.world = world;
        this.regionIsOp = creatorIsOp;
        this.children = new ArrayList<>();
        this.parent = null;
        this.priority = 0;
    }

    private int addSize() {
        return Math.abs(this.position1[0] - this.position2[0]) *
                Math.abs(this.position1[1] - this.position2[1]) *
                Math.abs(this.position1[2] - this.position2[2]);
    }

    public Map<String, UUID> getCreator() {
        Map<String, UUID> creatorStruct = new HashMap<>();
        creatorStruct.put(this.creatorNick, this.creator);
        return creatorStruct;
    }
    public int[] getPosition2() {
        return position2;
    }

    public int[] getPosition1() {
        return position1;
    }

    public String getWorldName() {
        return this.world;
    }

    public boolean contains(int x, int y, int z) {

        int minX = Math.min(position1[0], position2[0]);
        int maxX = Math.max(position1[0], position2[0]);

        int minY = Math.min(position1[1], position2[1]);
        int maxY = Math.max(position1[1], position2[1]);

        int minZ = Math.min(position1[2], position2[2]);
        int maxZ = Math.max(position1[2], position2[2]);

        return x >= minX && x <= maxX &&
                y >= minY && y <= maxY &&
                z >= minZ && z <= maxZ;
    }

    public boolean contains(Location location) {
        return contains(location.getFloorX(),
                location.getFloorY(),
                location.getFloorZ());
    }

    public Location getCenter(Location template) {
        double centerX = (position1[0] + position2[0]) / 2.0;
        double centerY = (position1[1] + position2[1]) / 2.0;
        double centerZ = (position1[2] + position2[2]) / 2.0;

        return new Location(centerX, centerY, centerZ,
                template.getLevel());
    }

    public boolean addChild(Region child) {
        if (!this.contains(child.position1[0], child.position1[1], child.position1[2]) ||
                !this.contains(child.position2[0], child.position2[1], child.position2[2])) {
            return false;
        }

        if (!this.world.equals(child.world)) {
            return false;
        }

        if (child.parent != null) {
            child.parent.removeChild(child);
        }

        child.parent = this;
        this.children.add(child);
        child.priority = this.priority + 1;

        return true;
    }

    public void removeChild(Region child) {
        this.children.remove(child);
        if (child.parent == this) {
            child.parent = null;
            child.priority = 0;
        }
    }

    public Region getParent() { return parent; }
    public List<Region> getChildren() { return new ArrayList<>(children); }
    public int getPriority() { return priority; }
}
