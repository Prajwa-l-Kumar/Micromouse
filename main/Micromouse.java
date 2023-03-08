package main;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Micromouse
 */
public class Micromouse {

    static int size = 5;
    static boolean hwalls[][] = new boolean[size][size];
    static int hwallsVal[][] = {{0,0}, {0,1}, {0,2}, {0,3}, {0,4}, {1,1}, {1,2}, {2,0}, {2,3}, {3,2}, {4,3}};
    static boolean vwalls[][] = new boolean[size][size];
    static int vwallsVal[][] = {{0,0}, {1,0}, {2,0}, {3,0}, {4,0}, {3,1}, {4,1}, {1,2}, {2,2}, {3,2}, {0,3}, {2,3}, {3,3}, {1,4}};
    static int dist[][] = new int[size][size];
    static boolean visited[][] = new boolean[size][size];
    static int posx, posy;
    static char dirs[] = {'N', 'E', 'S', 'W'};
    static char dir;
    static boolean goal;

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

        posx = size - 1;
        posy = 0;

        goal = false;
    }

    public static void printMatrix() {
        for(int i = 0; i < size; i++) {
            System.out.print("+ ");
            for(int  j = 0; j < size; j++) {
                System.out.print((hwalls[i][j] ? "--" : "  ") + "+ ");
            }
            System.out.println();
            for(int  j = 0; j < size; j++) {
                System.out.print((vwalls[i][j] ? "| " : "  ") + "" + dist[i][j]+" ");
            }
            System.out.println("|");
        }
        System.out.print("+ ");
        for (int i = 0; i < size; i++) {
            System.out.print("--+ ");
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
                return posx == size - 1 ? true : hwalls[posx + 1][posy];
            case 'E':
                return posy == size - 1 ? true : vwalls[posx][posy + 1];
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


    public static Cell LessAvailableNeighbour() {
        Cell cell = new Cell(-1, -1, false);
        if(posx < size - 1) {
            if(dist[posx + 1][posy] < dist[posx][posy] && vwalls[posx + 1][posy]) {
                cell.res = true;
                cell.x = posx;
                cell.y = posy;
            }
        } else if(posx > 0) {
            if(dist[posx - 1][posy] < dist[posx][posy] && hwalls[posx - 1][posy]) {
                cell.res = true;
                cell.x = posx;
                cell.y = posy;
            }
        } else if(posy < size - 1) {
            if(dist[posx][posy + 1] < dist[posx][posy] && vwalls[posx][posy + 1]) {
                cell.res = true;
                cell.x = posx;
                cell.y = posy;
            }
        } else if(posy > 0) {
            if(dist[posx][posy - 1] < dist[posx][posy] && vwalls[posx][posy - 1]) {
                cell.res = true;
                cell.x = posx;
                cell.y = posy;
            }
        }
        return cell;
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
    
    public static void main(String[] args) {
        setup();
        printMatrix();
        while(!goal) {
            System.out.println("At " + posx + "," + posy);
            if(LessAvailableNeighbour().res){
                if(!checkFrontWall()) {
                    goAhead(1);
                } else if(!checkRightWall()) {
                    turnRight();
                    goAhead(1);
                } else if(!checkLeftWall()) {
                    turnLeft();
                    goAhead(1);
                }
            } else {
                System.out.println("Renumbering...");
                //renumber
                Queue<Cell> queue = new ArrayDeque<>();
                queue.add(new Cell(posx, posy));
                while(!queue.isEmpty()) {
                    Cell currCell = queue.remove();
                    Cell lessCell = LessAvailableNeighbour();
                    if(!lessCell.res) {
                        dist[currCell.x][currCell.y] = dist[lessCell.x][lessCell.y] + 1;
                        if(!checkNorthWall(currCell)) {
                            queue.add(new Cell(posx, posy - 1));
                        } else if(!checkEastWall(currCell)) {
                            queue.add(new Cell(posx + 1, posy));
                        } else if(!checkSouthWall(currCell)) {
                            queue.add(new Cell(posx, posy + 1));
                        } else if(!checkWestWall(currCell)) {
                            queue.add(new Cell(posx - 1, posy));
                        }
                    }
                }
                printMatrix();
            }
        }
    }

}