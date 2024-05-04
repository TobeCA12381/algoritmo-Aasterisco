import java.util.ArrayList;
import java.util.List;

class Node {
    int x, y;
    Node parent;
    double g, h;

    public Node(int x, int y, Node parent) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.g = 0;
        this.h = 0;
    }

    public double getF() {
        return g + h;
    }
}

public class AStar {
    public static double euclideanDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public static List<Node> findPath(int[][] grid, int startX, int startY, int endX, int endY) {
        int width = grid.length;
        int height = grid[0].length;

        Node startNode = new Node(startX, startY, null);
        Node endNode = new Node(endX, endY, null);

        List<Node> openList = new ArrayList<>();
        List<Node> closedList = new ArrayList<>();

        openList.add(startNode);

        while (!openList.isEmpty()) {
            Node currentNode = openList.stream().min((n1, n2) -> Double.compare(n1.getF(), n2.getF())).get();
            openList.remove(currentNode);
            closedList.add(currentNode);

            if (currentNode.x == endNode.x && currentNode.y == endNode.y) {
                List<Node> path = new ArrayList<>();
                while (currentNode != null) {
                    path.add(currentNode);
                    currentNode = currentNode.parent;
                }
                return path;
            }

            List<Node> successors = new ArrayList<>();
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if ((i == 0 && j == 0) ||
                            currentNode.x + i < 0 || currentNode.x + i >= width ||
                            currentNode.y + j < 0 || currentNode.y + j >= height)
                        continue;

                    if (grid[currentNode.x + i][currentNode.y + j] == 1)
                        continue;

                    Node successor = new Node(currentNode.x + i, currentNode.y + j, currentNode);
                    successors.add(successor);
                }
            }

            for (Node successor : successors) {
                if (closedList.contains(successor))
                    continue;

                double tentativeG = currentNode.g + euclideanDistance(currentNode.x, currentNode.y, successor.x, successor.y);

                Node existingNode = openList.stream()
                        .filter(node -> node.x == successor.x && node.y == successor.y)
                        .findFirst().orElse(null);

                if (existingNode != null && tentativeG < existingNode.g) {
                    existingNode.g = tentativeG;
                    existingNode.parent = currentNode;
                } else if (existingNode == null) {
                    successor.g = tentativeG;
                    successor.h = euclideanDistance(successor.x, successor.y, endNode.x, endNode.y);
                    openList.add(successor);
                }
            }
        }

        return new ArrayList<>();
    }

    public static void printGridWithRoute(int[][] grid, List<Node> path, int startX, int startY, int endX, int endY) {
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                final int finalX = x;
                final int finalY = y;
                if (x == startX && y == startY)
                    System.out.print("A ");
                else if (x == endX && y == endY)
                    System.out.print("B ");
                else if (path.stream().anyMatch(node -> node.x == finalX && node.y == finalY))
                    System.out.print(". ");
                else if (grid[x][y] == 1)
                    System.out.print("1 ");
                else
                    System.out.print("0 ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int[][] grid = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {'A', 0, 0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 'B'},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

        int startX = -1, startY = -1, endX = -1, endY = -1;

        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                if (grid[x][y] == 'A') {
                    startX = x;
                    startY = y;
                } else if (grid[x][y] == 'B') {
                    endX = x;
                    endY = y;
                }
            }
        }

        if (startX != -1 && startY != -1 && endX != -1 && endY != -1) {
            List<Node> path = findPath(grid, startX, startY, endX, endY);
            printGridWithRoute(grid, path, startX, startY, endX, endY);
        } else {
            System.out.println("Error: No se encontraron puntos de inicio y fin en la matriz.");
        }
    }
}
