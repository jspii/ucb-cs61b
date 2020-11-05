package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.algs4.ST;

import java.util.HashMap;
import java.util.Map;

public class Room {
    // these values can be adjusted to change the room generation
    private static final int ROOM_WIDTH = 8;
    private static final int ROOM_HEIGHT = 8;

    private Map<String, Coordinate> blob;
    private Coordinate origin;
    private SeedGenerator seedGen;
    private TETile[][] internalGrid;
    private int height;
    private int width;

    public Room(SeedGenerator seedGen, Coordinate origin) {
        this.seedGen = seedGen;
        this.height = this.seedGen.genDimensionNumber(ROOM_HEIGHT);
        this.width = this.seedGen.genDimensionNumber(ROOM_WIDTH);
        this.internalGrid = new TETile[this.width][this.width];
        this.blob = new HashMap<>();
    }

    public Coordinate getCoordinate() {
        return origin;
    }

    public void setCoordinate(int x, int y) {
        origin = new Coordinate(x, y);
    }

    /** creates blobs and determines the largest room by using the helper methods below */
    private void generateCA() {
        // start room generation
        initiateCA();
        // smooth CA 5 times; this is indeed a triple nested loop, but problem set is small
        for (int smooth = 0; smooth < 5; smooth += 1) {
            smoothCA();
        }
        // TODO - DFS to find largest blob that was made
    }

    /**
     * first generation of CA blobs using the following rule:
     * i) 55% random chance of a tile being floor and 45% random chance of a tile being wall
     */
    private void initiateCA() {
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                internalGrid[x][y] = seedGen.genTile();
            }
        }
    }

    /**
     * "smoothing" of the cellular automata using the following rules:
     * i) if a floor cell has fewer than four adjacent floor cells, it becomes a wall (8 direction)
     * ii) if a wall cell has six or more adjacent floor cells, it becomes a floor (8 direction)
     */
    private void smoothCA() {
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                checkNeighbors(internalGrid[x][y], x, y);
            }
        }
    }

    /** check neighbor in all 8 directions to see if the tile in question has to change based off the rules of life */
    private void checkNeighbors(TETile tile, int x, int y) {
        if (tile.character() == '#') {
            int cardinalValues = checkCardinal('·', x, y);
            if (cardinalValues > 5) {
                internalGrid[x][y] = Tileset.FLOOR;
            }
            return;
        }
        int cardinalValues = checkCardinal('·', x, y);
        if (cardinalValues < 4) {
            internalGrid[x][y] = Tileset.WALL;
        }
    }

    /** this is going to look awful, I'm sorry */
    private int checkCardinal(char tiletype, int x, int y) {
        int count = 0;
        // diagonal top left
        if (internalGrid[wrapX(x - 1)][wrapY(y - 1)].character() == tiletype) {
            count += 1;
        }
        // directly above
        if (internalGrid[x][wrapY(y - 1)].character() == tiletype) {
            count += 1;
        }
        // diagonal top right
        if (internalGrid[wrapX(x + 1)][wrapY(y - 1)].character() == tiletype) {
            count += 1;
        }
        // directly left
        if (internalGrid[wrapX( x - 1)][y].character() == tiletype) {
            count += 1;
        }
        // directly right
        if (internalGrid[wrapX(x + 1)][y].character() == tiletype) {
            count += 1;
        }
        // diagonal bottom left
        if (internalGrid[wrapX(x - 1)][wrapY(y + 1)].character() == tiletype) {
            count += 1;
        }
        // directly below
        if (internalGrid[x][wrapY(y + 1)].character() == tiletype) {
            count += 1;
        }
        // diagonal bottom right
        if (internalGrid[x][wrapY(y + 1)].character() == tiletype) {
            count += 1;
        }
        return count;
    }

    private int wrapX(int inc) {
        return inc % width;
    }

    private int wrapY(int inc) {
        return inc % height;
    }

    /**
     * DFS recursive function to determine the largest blob created by CA
     */
    private void fill() {
        // TODO - implement
    }
}
