package main;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;

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
    static int size = 16;
    static boolean hwalls[][] = new boolean[size][size + 1];
    // static int hwallsVal[][] = {{1,1}, {2,0}, {0,2}, {3,2}, {2,3}, {3,4}}; //removed 2,1
    // static int vwallsVal[][] = {{1,3}, {1,4}, {2,1}, {2,2}, {2,3}, {3,0}, {3,2}, {3,3}, {4,1}};
    static int hwallsVal[][] = {{1,1}, {2,1}, {8,1}, {9,1}, {11,1}, {12,1}, {13,1}, {14,1},
                                {1,2}, {3,2}, {8,2}, {10,2}, {13,2},
                                {0,3}, {2,3}, {7,3}, {9,3}, {11,3}, {12,3}, {14,3},
                                {1,4}, {2,4}, {8,4}, {10,4}, {12,4}, {13,4}, {15,4},
                                {2,5}, {3,5}, {7,5}, {9,5}, {13,5}, {14,5},
                                {1,6}, {2,6}, {5,6}, {9,6},
                                {2,7}, {6,7}, {7,7}, {8,7}, {11,7}, {13,7}, {14,7}, {15,7},
                                {3,8}, {5,8}, {9,8}, {10,8}, {11,8}, {12,8}, {14,8},
                                {0,9}, {2,9}, {5,9}, {6,9}, {7,9}, {8,9}, {11,9}, {13,9}, {15,9},
                                {1,10}, {2,10}, {3,10}, {4,10}, {6,10}, {9,10}, {12,10}, {14,10},
                                {1,11}, {2,11}, {4,11}, {6,11}, {7,11}, {8,11}, {12,11}, {13,11}, {14,11},
                                {3,12}, {5,12}, {11,12}, {12,12}, {13,12}, {14,12}, {15,12},
                                {1,13}, {2,13}, {4,13}, {9,13}, {10,13}, {11,13}, {12,13}, {13,13}, {14,13},
                                {3,14}, {5,14}, {9,14}, {11,14}, {12,14}, {13,14}, {14,14}, {15,14},
                                {2,15}, {4,15}, {9,15}, {10,15}, {11,15}, {12,15}, {13,15}, {14,15}};
    static int vwallsVal[][] = {{4,0}, {7,0},
                                {2,1}, {4,1}, {5,1}, {6,1}, {7,1}, {10,1}, {11,1}, {12,1}, {15,1},
                                {2,2}, {4,2}, {5,2}, {7,2}, {8,2}, {10,2}, {13,2},
                                {2,3}, {4,3}, {5,3}, {6,3}, {7,3}, {9,3}, {11,3}, {14,3},
                                {1,4}, {4,4}, {5,4}, {6,4}, {8,4}, {10,4}, {11,4}, {12,4}, {15,4},
                                {1,5}, {4,5}, {5,5}, {7,5}, {8,5}, {11,5}, {12,5}, {13,5}, {14,5}, {15,5},
                                {1,6}, {3,6}, {4,6}, {6,6}, {7,6}, {9,6}, {10,6}, {11,6},
                                {2,7}, {4,7}, {5,7}, {7,7}, {12,7},
                                {1,8}, {2,8}, {4,8}, {7,8}, {9,8}, {10,8}, {13,8},
                                {3,9}, {5,9}, {7,9}, {11,9}, {12,9}, {14,9},
                                {7,10,}, {8,10}, {10,10}, {11,10}, {13,10},
                                {1,11}, {3,11}, {5,11}, {7,11}, {9,11}, {11,11},
                                {2,12}, {4,12}, {6,12}, {7,12}, {8,12}, {9,12}, {10,12},
                                {1,13}, {3,13}, {5,13}, {7,13}, {8,13}, {9,13},
                                {1,14}, {2,14}, {4,14}, {6,14}, {7,14}, {8,14}, {9,14},
                                {1,15}, {8,15}};
    static boolean vwalls[][] = new boolean[size + 1][size];
    static int dist[][] = new int[size][size];
    static boolean hwallsVisit[][] = new boolean[size][size + 1];
    static boolean vwallsVisit[][] = new boolean[size + 1][size];
    static boolean visited[][] = new boolean[size][size];
    static int posx, posy;
    static char dirs[] = {'N', 'E', 'S', 'W'};
    static int dirOff[][] = {{0,-1}, {1,0}, {0,1}, {-1,0}};
    static int dirOffInd;
    static char dir;
    static int targetx, targety, centerx, centery;
    static Cell origin, center, target;

    public static void setup() {
        for(int i = 0; i < hwallsVal.length; i++) {
            hwalls[hwallsVal[i][0]][hwallsVal[i][1]] = true;
        }
        for(int i = 0; i < vwallsVal.length; i++) {
            vwalls[vwallsVal[i][0]][vwallsVal[i][1]] = true;
        }
        for(int i = 0; i <= size; i++) {
            for(int j = 0;  j <= size; j++) {
                if((i == 0 || i == size) && j < size) {
                    vwalls[i][j] = vwallsVisit[i][j] = true;
                }
                if((j == 0 || j == size) && i < size) {
                    hwalls[i][j] = hwallsVisit[i][j] = true;
                }
            }
        }
        // if(size % 2 == 0) {
        //     for(int i = 0; i < size/2; i++) {
        //         for(int j = 0; j < size/2; j++) {
        //             dist[size/2 + i][size/2 + j] = dist[size/2 - 1 - i][size/2 + j] = dist[size/2 + i][size/2 - 1 - j] = dist[size/2 - 1 - i][size/2 - 1 - j] = i + j;
        //         }
        //     }
        // } else {
        //     for(int i = 0; i < size/2 + 1; i++) {
        //         for(int j = 0; j < size/2 + 1; j++) {
        //             dist[size/2 + i][size/2 + j] = dist[size/2 - i][size/2 + j] = dist[size/2 + i][size/2 - j] = dist[size/2 - i][size/2 - j] = i + j;
        //         }
        //     }
        // }

        dir = 'N';
        dirOffInd = 0;

        origin = new Cell(0, size - 1);
        center = new Cell(size / 2, size / 2);

        posx = origin.x;
        posy = origin.y;
    }

    public static void printMaze() {
        for(int i = 0; i <= size; i++) {
            System.out.print("+");
            for(int  j = 0; j < size; j++) {
                System.out.print((hwalls[j][i] ? "---" : "   ") + "+");
            }
            System.out.println();
            if(i < size) {
                for(int  j = 0; j <= size; j++) {
                    System.out.print(vwalls[j][i] ? "| " : "  ");
                    if(j < size) {
                        System.out.print("  ");
                    }
                }
            }
            System.out.println();
        }
    }

    public static void printMatrix() {
        char pointer;
        switch(dir) {
            case 'N':
                pointer = '^';
                break;
            case 'E':
                pointer = '>';
                break;
            case 'S':
                pointer = 'v';
                break;
            case 'W':
                pointer = '<';
                break;
            default:
                pointer = '0';
        }
        for(int i = 0; i <= size; i++) {
            System.out.print("+");
            for(int  j = 0; j < size; j++) {
                System.out.print((hwallsVisit[j][i] ? "---" : "   ") + "+");
            }
            System.out.println();
            if(i < size) {
                for(int  j = 0; j <= size; j++) {
                    System.out.print(vwallsVisit[j][i] ? "|" : " ");
                    if(j < size) {
                        System.out.print(dist[i][j] < 10 ? " "+dist[i][j] : dist[i][j]);
                        if(i == posy && j == posx) {
                            System.out.print(pointer);
                        } else {
                            System.out.print(" ");
                        }
                    }
                }
            }
            System.out.println();
        }
    }

    static boolean checkNorthWall(Cell cell) {
        return hwallsVisit[cell.x][cell.y];
    }

    static boolean checkEastWall(Cell cell) {
        return vwallsVisit[cell.x + 1][cell.y];
    }

    static boolean checkSouthWall(Cell cell) {
        return hwallsVisit[cell.x][cell.y + 1];
    }

    static boolean checkWestWall(Cell cell) {
        return vwallsVisit[cell.x][cell.y];
    }

    static boolean checkLeftWall() {
        switch(dir) {
            case 'N':
                vwallsVisit[posx][posy] = vwalls[posx][posy];
                return vwallsVisit[posx][posy];
            case 'E':
                hwallsVisit[posx][posy] = hwalls[posx][posy];
                return hwallsVisit[posx][posy];
            case 'S':
                vwallsVisit[posx + 1][posy] = vwalls[posx + 1][posy];
                return vwallsVisit[posx + 1][posy];
            case 'W':
                hwallsVisit[posx][posy + 1] = hwalls[posx][posy + 1];
                return hwallsVisit[posx][posy + 1];
            default :
                return false;
        }
    }

    static boolean checkRightWall() {
        switch(dir) {
            case 'N':
                vwallsVisit[posx + 1][posy] = vwalls[posx + 1][posy];
                return vwallsVisit[posx + 1][posy];
            case 'E':
                hwallsVisit[posx][posy + 1] = hwalls[posx][posy + 1];
                return hwallsVisit[posx][posy + 1];
            case 'S':
                vwallsVisit[posx][posy] = vwalls[posx][posy];
                return vwallsVisit[posx][posy];
            case 'W':
                hwallsVisit[posx][posy] = hwalls[posx][posy];
                return hwallsVisit[posx][posy];
            default :
                return false;
        }
    }

    static boolean checkFrontWall() {
        switch(dir) {
            case 'N':
                hwallsVisit[posx][posy] = hwalls[posx][posy];
                return hwallsVisit[posx][posy];
            case 'E':
                vwallsVisit[posx + 1][posy] = vwalls[posx + 1][posy];
                return vwalls[posx + 1][posy];
            case 'S':
                hwallsVisit[posx][posy + 1] = hwalls[posx][posy + 1];
                return hwallsVisit[posx][posy + 1];
            case 'W':
                vwallsVisit[posx][posy] = vwalls[posx][posy];
                return vwalls[posx][posy];
            default :
                return false;
        }
    }

    static boolean checkRearWall() {
        switch(dir) {
            case 'N':
                hwallsVisit[posx][posy + 1] = hwalls[posx][posy + 1];
                return hwallsVisit[posx][posy + 1];
            case 'E':
                vwallsVisit[posx][posy] = vwalls[posx][posy];
                return vwalls[posx][posy];
            case 'S':
                hwallsVisit[posx][posy] = hwalls[posx][posy];
                return hwallsVisit[posx][posy];
            case 'W':
                vwallsVisit[posx + 1][posy] = vwalls[posx + 1][posy];
                return vwalls[posx + 1][posy];
            default :
                return false;
        }
    }

    public static void checkWalls() {
        checkFrontWall();
        checkLeftWall();
        checkRightWall();
    }

    public static Cell lesserAvailableNeighbourCell() {
        Cell retCell = new Cell(-1, -1, false);
        Cell tempCell = getFrontCell();
        int leastDist = dist[posy][posx];
        if(!checkFrontWall() && dist[tempCell.y][tempCell.x] < leastDist) {
            retCell = tempCell;
            leastDist = dist[tempCell.y][tempCell.x];
        }
        //Right
        tempCell = getRightCell();
        if(!checkRightWall() && dist[tempCell.y][tempCell.x] < leastDist) {
            retCell = tempCell;
            leastDist = dist[tempCell.y][tempCell.x];
        }
        //Left
        tempCell = getLeftCell();
        if(!checkLeftWall() && dist[tempCell.y][tempCell.x] < leastDist) {
            retCell = tempCell;
            leastDist = dist[tempCell.y][tempCell.x];
        }
        //Rear
        tempCell = getRearCell();
        if(!checkRearWall() && dist[tempCell.y][tempCell.x] < leastDist) {
            retCell = tempCell;
            leastDist = dist[tempCell.y][tempCell.x];
        }
        return retCell;
    }

    /*
     * return the neighbouring cell whose manhattan dist is less than the current cell if present
     * else return an invalid cell
     */
    public static Cell lesserAvailableNeighbourCell(Cell cell) {
        Cell retCell = new Cell(-1, -1, false);
        int leastDist = Integer.MAX_VALUE;
        if(cell.x < size - 1) { //E
            if(dist[cell.y][cell.x + 1] < dist[cell.y][cell.x] && !vwallsVisit[cell.x + 1][cell.y] && dist[cell.y][cell.x + 1] < leastDist) {
                retCell.res = true;
                retCell.x = cell.x + 1;
                retCell.y = cell.y;
                return retCell;
            }
        }
        if(cell.x > 0) { //W
            if(dist[cell.y][cell.x - 1] < dist[cell.y][cell.x] && !vwallsVisit[cell.x][cell.y] && dist[cell.y][cell.x - 1] < leastDist) {
                retCell.res = true;
                retCell.x = cell.x - 1;
                retCell.y = cell.y;
                return retCell;
            }
        }
        if(cell.y < size - 1) { //S
            if(dist[cell.y + 1][cell.x] < dist[cell.y][cell.x] && !hwallsVisit[cell.x][cell.y + 1] && dist[cell.y + 1][cell.x] < leastDist) {
                retCell.res = true;
                retCell.x = cell.x;
                retCell.y = cell.y + 1;
                return retCell;
            }
        }
        if(cell.y > 0) { //N
            if(dist[cell.y - 1][cell.x] < dist[cell.y][cell.x] && !hwallsVisit[cell.x][cell.y] && dist[cell.y - 1][cell.x] < leastDist) {
                retCell.res = true;
                retCell.x = cell.x;
                retCell.y = cell.y - 1;
                return retCell;
            }
        }
        return retCell;
    }

    public static Cell getFrontCell() {
        int dirInd = dirOffInd;
        return new Cell(posx + dirOff[dirInd][0], posy + dirOff[dirInd][1], true);
    }

    public static Cell getRightCell() {
        int dirInd = (dirOffInd + 1)%4;
        return new Cell(posx + dirOff[dirInd][0], posy + dirOff[dirInd][1], true);
    }

    public static Cell getLeftCell() {
        int dirInd = (dirOffInd - 1 + 4)%4;
        return new Cell(posx + dirOff[dirInd][0], posy + dirOff[dirInd][1], true);
    }

    public static Cell getRearCell() {
        int dirInd = (dirOffInd + 2)%4;
        return new Cell(posx + dirOff[dirInd][0], posy + dirOff[dirInd][1], true);
    }

    public static Cell leastAvailableNeighbourCell(Cell cell) {
        Cell retCell = new Cell(-1, -1, false);
        int leastDist = Integer.MAX_VALUE;
        if(cell.x < size - 1) { //N
            if(!vwallsVisit[cell.x + 1][cell.y] && dist[cell.y][cell.x + 1] < leastDist) {
                retCell.x = cell.x + 1;
                retCell.y = cell.y;
                leastDist = dist[cell.y][cell.x + 1];
            }
        }
        if(cell.x > 0) { //W
            if(!vwallsVisit[cell.x][cell.y] && dist[cell.y][cell.x - 1] < leastDist) {
                retCell.x = cell.x - 1;
                retCell.y = cell.y;
                retCell.res = true;
                leastDist = dist[cell.y][cell.x - 1];
            }
        }
        if(cell.y < size - 1) { //S
            if(!hwallsVisit[cell.x][cell.y + 1] && dist[cell.y + 1][cell.x] < leastDist) {
                retCell.x = cell.x;
                retCell.y = cell.y + 1;
                retCell.res = true;
                leastDist = dist[cell.y + 1][cell.x];
            }
        }
        if(cell.y > 0) { //N
            if(!hwallsVisit[cell.x][cell.y] && dist[cell.y - 1][cell.x] < leastDist) {
                retCell.x = cell.x;
                retCell.y = cell.y - 1;
                retCell.res = true;
                leastDist = dist[cell.y - 1][cell.x];
            }
        }
        return retCell;
    }

    public static void turnRight() {
        dirOffInd = (dirOffInd + 1) % 4;
        dir = dirs[dirOffInd];
    }

    public static void turnLeft() {
        dirOffInd = (dirOffInd - 1 + 4) % 4;
        dir = dirs[dirOffInd];
    }

    public static void turnAround() {
        dirOffInd = (dirOffInd + 2) % 4;
        dir = dirs[dirOffInd];
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

    public static void floodfill(Cell targetCell) {
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                dist[j][i] = size*size - 1;
            }
        }
        Queue<Cell> queue = new ArrayDeque<>();
        queue.add(targetCell);
        dist[targetCell.y][targetCell.x] = 0;
        while(!queue.isEmpty()) {
            Cell currCell = queue.remove();
            int newDist = dist[currCell.y][currCell.x] + 1;
            if(currCell.y > 0) { //N
                if(dist[currCell.y - 1][currCell.x] > newDist) {
                    dist[currCell.y - 1][currCell.x] = newDist;
                    queue.add(new Cell(currCell.x, currCell.y - 1));
                }
            }
            if(currCell.x < size - 1) { //E
                if(dist[currCell.y][currCell.x + 1] > newDist) {
                    dist[currCell.y][currCell.x + 1] = newDist;
                    queue.add(new Cell(currCell.x + 1, currCell.y));
                }
            }
            if(currCell.y < size - 1) { //S
                if(dist[currCell.y + 1][currCell.x] > newDist) {
                    dist[currCell.y + 1][currCell.x] = newDist;
                    queue.add(new Cell(currCell.x, currCell.y + 1));
                }
            }
            if(currCell.x > 0) { //W
                if(dist[currCell.y][currCell.x - 1] > newDist) {
                    dist[currCell.y][currCell.x - 1] = newDist;
                    queue.add(new Cell(currCell.x - 1, currCell.y));
                }
            }
            
        }
    }

    public static void floodFillWithWalls() {
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                dist[j][i] = Integer.MAX_VALUE;
            }
        }
        Queue<Cell> queue = new ArrayDeque<>();
        queue.add(center);
        dist[center.y][center.x] = 0;
        while(!queue.isEmpty()) {
            Cell currCell = queue.remove();
            int newDist = dist[currCell.y][currCell.x] + 1;
            if(!checkNorthWall(currCell)) { //N
                if(dist[currCell.y - 1][currCell.x] > newDist) {
                    dist[currCell.y - 1][currCell.x] = newDist;
                    queue.add(new Cell(currCell.x, currCell.y - 1));
                }
            }
            if(!checkEastWall(currCell)) { //E
                if(dist[currCell.y][currCell.x + 1] > newDist) {
                    dist[currCell.y][currCell.x + 1] = newDist;
                    queue.add(new Cell(currCell.x + 1, currCell.y));
                }
            }
            if(!checkSouthWall(currCell)) { //S
                if(dist[currCell.y + 1][currCell.x] > newDist) {
                    dist[currCell.y + 1][currCell.x] = newDist;
                    queue.add(new Cell(currCell.x, currCell.y + 1));
                }
            }
            if(!checkWestWall(currCell)) { //W
                if(dist[currCell.y][currCell.x - 1] > newDist) {
                    dist[currCell.y][currCell.x - 1] = newDist;
                    queue.add(new Cell(currCell.x - 1, currCell.y));
                }
            }
            
        }

    }

    public static void searchTo(Cell targetCell) {
        
        target = new Cell(targetCell.x, targetCell.y);
        floodfill(targetCell);
        printMatrix();
        // Stack<Cell> stack = new Stack<>();
        boolean goal = false;
        boolean visited[][] = new boolean[size][size];
        while(!goal) {
            System.out.println("At " + posx + "," + posy + "dir:" + dir + " " + (visited[posx][posy] ? "Already visited" : "Not Visited"));
            if(!visited[posx][posy]) {
                visited[posx][posy] = true;
                checkWalls();
                // stack.add(new Cell(posx, posy));
            } else {
                // while(!(stack.peek().x == posx && stack.peek().y == posy)) {
                //     stack.pop();
                // }
            }
            printMatrix();
            Cell lesserCell = lesserAvailableNeighbourCell();
            // System.out.println("lesserCell: "+lesserCell.x+","+lesserCell.y);
            if(lesserCell.res){
                //move to the least available neighbour
                gotoNeighbourCell(lesserCell);
            } else {
                System.out.println("Renumbering...");
                Queue<Cell> queue = new ArrayDeque<>();
                queue.add(new Cell(posx, posy));
                while(!queue.isEmpty()) {
                    Cell currCell = queue.remove();
                    // System.out.println("currCell: "+currCell.x+","+currCell.y);
                    Cell lessCell = lesserAvailableNeighbourCell(currCell);
                    if(!lessCell.res) {
                        Cell leastCell = leastAvailableNeighbourCell(currCell);
                        // System.out.println("leastCell: "+leastCell.x+","+leastCell.y);
                        dist[currCell.y][currCell.x] = dist[leastCell.y][leastCell.x] + 1;
                        if(!checkNorthWall(currCell)) {
                            queue.add(new Cell(currCell.x, currCell.y - 1));
                            // System.out.println("N");
                        }
                        if(!checkEastWall(currCell)) {
                            queue.add(new Cell(currCell.x + 1, currCell.y));
                            // System.out.println("E");
                        }
                        if(!checkSouthWall(currCell)) {
                            queue.add(new Cell(currCell.x, currCell.y + 1));
                            // System.out.println("S");
                        }
                        if(!checkWestWall(currCell)) {
                            queue.add(new Cell(currCell.x - 1, currCell.y));
                            // System.out.println("W");
                        }
                    }
                }
                // printMatrix();
                // Thread.sleep(500);
            }
            // Thread.sleep(100);
            if(posx == target.x && posy == target.y) {
                goal = true;
                // stack.add(new Cell(posx, posy));
            }
        }
        printMatrix();
    }
    
    public static void main(String[] args) throws InterruptedException {
        setup();
        printMaze();
        searchTo(center);
        System.out.println("Reached!, now going back.");
        searchTo(origin);
        floodFillWithWalls();
        printMatrix();
    }

}
