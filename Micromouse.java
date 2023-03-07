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
    static char dir;

    public static void setup() {
        for(int i = 0; i < hwallsVal.length; i++) {
            hwalls[hwallsVal[i][0]][hwallsVal[i][1]] = true;
        }
        for(int i = 0; i < hwallsVal.length; i++) {
            hwalls[vwallsVal[i][0]][vwallsVal[i][1]] = true;
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

        for(int i = 0; i < dist.length; i++) {
            for(int j = 0; j < dist[i].length; j++) {
                System.out.print(dist[i][j] + " ");
            }
            System.out.println();
        }

        dir = 'N';
    }

    static boolean checkLeft() {
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

    static boolean checkRight() {
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
    
    public static void main(String[] args) {
        setup();
    }

}