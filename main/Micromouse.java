package main;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Micromouse
 */
public class Micromouse {
/*
example maze:
+---+---+---+---+---+
| 4   3   2 | 3   4 |
+   +---+---+   +   +
| 3   2 | 1   2 | 3 |
+---+   +   +---+   +
| 2   1 | 0 | 1   2 |
+   +   +---+   +   +
| 3 | 2 | 1 | 2   3 |
+   +   +   +---+   +
| 4 | 3   2   3   4 |
+---+---+---+---+---+ 
 */
    static int size = 5;
    static boolean hwalls[][] = new boolean[size][size];
    static int hwallsVal[][] = {{0,0}, {1,0}, {2,0}, {3,0}, {4,0}, {1,1}, {2,0}, {2,1}, {0,2}, {3,2}, {2,3}, {3,4}};
    static boolean vwalls[][] = new boolean[size][size];
    static int vwallsVal[][] = {{0,0}, {0,1}, {0,2}, {0,3}, {0,4}, {1,3}, {1,4}, {2,1}, {2,2}, {2,3}, {3,0}, {3,2}, {3,3}, {4,1}};
    static int dist[][] = new int[size][size];
    static boolean visited[][] = new boolean[size][size];
    static int posx, posy;
    static char dirs[] = {'N', 'E', 'S', 'W'};
    static char dir;
    static boolean goal;
    static int targetx, targety;

    public static void setup() {
        for(int i = 0; i < hwallsVal.length; i++) {
            hwalls[hwallsVal[i][0]][hwallsVal[i][1]] = true;
        }
        for(int i = 0; i < vwallsVal.length; i++) {
            vwalls[vwallsVal[i][0]][vwallsVal[i][1]] = true;
        }
        if(size % 2 == 0) {
            for(int i = 0; i < size/2; i++) {
                for(int j = 0; j < size/2; j++) {
                    dist[size/2 + i][size/2 + j] = dist[size/2 - 1 - i][size/2 + j] = dist[size/2 + i][size/2 - 1 - j] = dist[size/2 - 1 - i][size/2 - 1 - j] = i + j;
                }
            }
        } else {
            for(int i = 0; i < size/2 + 1; i++) {
                for(int j = 0; j < size/2 + 1; j++) {
                    dist[size/2 + i][size/2 + j] = dist[size/2 - i][size/2 + j] = dist[size/2 + i][size/2 - j] = dist[size/2 - i][size/2 - j] = i + j;
                }
            }
        }

        dir = 'N';

        posx = 0;
        posy = size - 1;

        goal = false;

        targetx = size / 2;
        targety = size / 2;
    }

    public static void printMatrix() {
        for(int i = 0; i < size; i++) {
            System.out.print("+");
            for(int  j = 0; j < size; j++) {
                System.out.print((hwalls[j][i] ? "---" : "   ") + "+");
            }
            System.out.println();
            for(int  j = 0; j < size; j++) {
                System.out.print((vwalls[j][i] ? "| " : "  ") + "" + dist[i][j]+" ");
            }
            System.out.println("|");
        }
        System.out.print("+");
        for (int i = 0; i < size; i++) {
            System.out.print("---+");
        }
        System.out.println();
    }

    static boolean checkNorthWall(Cell cell) {
        return hwalls[cell.x][cell.y];
    }

    static boolean checkEastWall(Cell cell) {
        return cell.x == size - 1 ? true : vwalls[cell.x + 1][cell.y];
    }

    static boolean checkSouthWall(Cell cell) {
        return cell.y == size - 1 ? true : hwalls[cell.x][cell.y + 1];
    }

    static boolean checkWestWall(Cell cell) {
        return vwalls[cell.x][cell.y];
    }

    static boolean checkLeftWall() {
        switch(dir) {
            case 'N':
                return vwalls[posx][posy];
            case 'E':
                return hwalls[posx][posy];
            case 'S':
                return posx == size - 1 ? true : vwalls[posx + 1][posy];
            case 'W':
                return posy == size - 1 ? true : hwalls[posx][posy + 1];
            default :
                return false;
        }
    }

    static boolean checkRightWall() {
        switch(dir) {
            case 'N':
                return posx == size - 1 ? true : vwalls[posx + 1][posy];
            case 'E':
                return posy == size - 1 ? true : hwalls[posx][posy + 1];
            case 'S':
                return vwalls[posx][posy];
            case 'W':
                return hwalls[posx][posy];
            default :
                return false;
        }
    }

    static boolean checkFrontWall() {
        switch(dir) {
            case 'N':
                return hwalls[posx][posy];
            case 'E':
                return posx == size - 1 ? true : vwalls[posx + 1][posy];
            case 'S':
                return posy == size - 1 ? true : hwalls[posx][posy + 1];
            case 'W':
                return vwalls[posx][posy];
            default :
                return false;
        }
    }

    /*
     * return the neighbouring neighbour cell whose manhattan dist is less than the current cell if present
     * else return invalid cell
     */
    public static Cell lesserAvailableNeighbourCell(Cell cell) {
        Cell retCell = new Cell(-1, -1, false);
        int leastDist = Integer.MAX_VALUE;
        if(cell.x < size - 1) { //E
            if(dist[cell.y][cell.x + 1] < dist[cell.y][cell.x] && !vwalls[cell.x + 1][cell.y] && dist[cell.y][cell.x + 1] < leastDist) {
                retCell.res = true;
                retCell.x = cell.x + 1;
                retCell.y = cell.y;
                return retCell;
            }
        }
        if(cell.x > 0) { //W
            if(dist[cell.y][cell.x - 1] < dist[cell.y][cell.x] && !vwalls[cell.x][cell.y] && dist[cell.y][cell.x - 1] < leastDist) {
                retCell.res = true;
                retCell.x = cell.x - 1;
                retCell.y = cell.y;
                return retCell;
            }
        }
        if(cell.y < size - 1) { //S
            if(dist[cell.y + 1][cell.x] < dist[cell.y][cell.x] && !hwalls[cell.x][cell.y + 1] && dist[cell.y + 1][cell.x] < leastDist) {
                retCell.res = true;
                retCell.x = cell.x;
                retCell.y = cell.y + 1;
                return retCell;
            }
        }
        if(cell.y > 0) { //N
            if(dist[cell.y - 1][cell.x] < dist[cell.y][cell.x] && !hwalls[cell.x][cell.y] && dist[cell.y - 1][cell.x] < leastDist) {
                retCell.res = true;
                retCell.x = cell.x;
                retCell.y = cell.y - 1;
                return retCell;
            }
        }
        return retCell;
    }

    public static Cell leastAvailableNeighbourCell(Cell cell) {
        Cell retCell = new Cell(-1, -1, false);
        int leastDist = Integer.MAX_VALUE;
        if(cell.x < size - 1) { //E
            if(!vwalls[cell.x + 1][cell.y] && dist[cell.y][cell.x + 1] < leastDist) {
                retCell.x = cell.x + 1;
                retCell.y = cell.y;
                leastDist = dist[cell.y][cell.x + 1];
            }
        }
        if(cell.x > 0) { //W
            if(!vwalls[cell.x][cell.y] && dist[cell.y][cell.x - 1] < leastDist) {
                retCell.x = cell.x - 1;
                retCell.y = cell.y;
                leastDist = dist[cell.y][cell.x - 1];
            }
        }
        if(cell.y < size - 1) { //S
            if(!hwalls[cell.x][cell.y + 1] && dist[cell.y + 1][cell.x] < leastDist) {
                retCell.x = cell.x;
                retCell.y = cell.y + 1;
                leastDist = dist[cell.y + 1][cell.x];
            }
        }
        if(cell.y > 0) { //N
            if(!hwalls[cell.x][cell.y] && dist[cell.y - 1][cell.x] < leastDist) {
                retCell.x = cell.x;
                retCell.y = cell.y - 1;
                leastDist = dist[cell.y - 1][cell.x];
            }
        }
        return retCell;
    }

    public static void turnRight() {
        for(int i = 0; i < dirs.length; i++) {
            if(dirs[i] == dir) {
                dir = dirs[(i + 1) % dirs.length];
                break;
            }
        }
    }

    public static void turnLeft() {
        for(int i = 0; i < dirs.length; i++) {
            if(dirs[i] == dir) {
                dir = dirs[(i + dirs.length - 1) % dirs.length];
                break;
            }
        }
    }

    public static void turnAround() {
        for(int i = 0; i < dirs.length; i++) {
            if(dirs[i] == dir) {
                dir = dirs[(i + 2) % dirs.length];
                break;
            }
        }
    }

    public static void gotoNeighbourCell(Cell cell) {
        if(cell.x < posx) { //W
            switch(dir) {
                case 'N':
                    turnLeft();
                    break;
                case 'E':
                    turnAround();
                    break;
                case 'S':
                    turnRight();
                    break;
                case 'W':
                    break;
            }
            goAhead(1);
        } else if(cell.x > posx) { //E
            switch(dir) {
                case 'N':
                    turnRight();
                    break;
                case 'E':
                    break;
                case 'S':
                    turnLeft();
                    break;
                case 'W':
                    turnAround();
                    break;
            }
            goAhead(1);
        } else if(cell.y < posy) { //N
            switch(dir) {
                case 'N':
                    break;
                case 'E':
                    turnLeft();
                    break;
                case 'S':
                    turnAround();
                    break;
                case 'W':
                    turnRight();
                    break;
            }
            goAhead(1);

        } else if(cell.y > posy) { //S
            switch(dir) {
                case 'N':
                    turnAround();
                    break;
                case 'E':
                    turnRight();
                    break;
                case 'S':
                    break;
                case 'W':
                    turnLeft();
                    break;
            }
            goAhead(1);
        }
    }

    public static void goAhead(int i) {
        switch(dir) {
            case 'N':
                posy -= i;
                break;
            case 'E':
                posx += i;
                break;
            case 'S':
                posy += i;
                break;
            case 'W':
                posx -= i;
                break;
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        setup();
        printMatrix();
        while(!goal) {
            System.out.println("At " + posx + "," + posy);
            System.out.println("dir: " + dir);
            Cell lesserCell = lesserAvailableNeighbourCell(new Cell(posx, posy));
            System.out.println("lesserCell: "+lesserCell.x+","+lesserCell.y);
            if(lesserCell.res){
                //move to the least available neighbour
                gotoNeighbourCell(lesserCell);
                System.out.println("in lesserAvailableNeighbour().res true");
            } else {
                System.out.println("Renumbering...");
                Queue<Cell> queue = new ArrayDeque<>();
                queue.add(new Cell(posx, posy));
                while(!queue.isEmpty()) {
                    Cell currCell = queue.remove();
                    System.out.println("currCell: "+currCell.x+","+currCell.y);
                    Cell lessCell = lesserAvailableNeighbourCell(currCell);
                    if(!lessCell.res) {
                        Cell leastCell = leastAvailableNeighbourCell(currCell);
                        System.out.println("leastCell: "+leastCell.x+","+leastCell.y);
                        dist[currCell.y][currCell.x] = dist[leastCell.y][leastCell.x] + 1;
                        printMatrix();
                        if(!checkNorthWall(currCell)) {
                            queue.add(new Cell(currCell.x, currCell.y - 1));
                            System.out.println("N");
                        }
                        if(!checkEastWall(currCell)) {
                            queue.add(new Cell(currCell.x + 1, currCell.y));
                            System.out.println("E");
                        }
                        if(!checkSouthWall(currCell)) {
                            queue.add(new Cell(currCell.x, currCell.y + 1));
                            System.out.println("S");
                        }
                        if(!checkWestWall(currCell)) {
                            queue.add(new Cell(currCell.x - 1, currCell.y));
                            System.out.println("W");
                        }
                    }
                }
                printMatrix();
                Thread.sleep(1000);
            }
            if(posx == targetx && posy == targety) {
                goal = true;
            }
        }
    }

}
