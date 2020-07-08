import java.util.*;

public class Puzzle8 {
    static final byte[][] goal = {{1, 2, 3},{4, 5, 6},{7, 8, 0 }};
    static final Board answer = new Board(goal);
    static Random nRandom = new Random(System.currentTimeMillis());
    static boolean res = false;

    public static void main(final String[] args) {
        System.out.println("IDFS \t DFS \t Estrella \t");
        for (int i = 0; i < 30; i++) {
            final Board init = generate(14);
            System.out.print(iterativeDFS(init, answer) + "\t");
            System.out.print(DFS(init, answer) + "\t");
            System.out.print(aEstrella(init, answer) + "\t");
        }
    }

    public static Board generate(final int movements) {
        int[] pos = { 2, 2 };
        Board board = new Board(goal, pos);
        for (int i = 0; i < movements; i++) {
            final int newCoord = nRandom.nextInt(3);
            Board newBoard;
            if (i % 2 == 1) {
                final int[] newPos = { pos[0], newCoord };
                newBoard = board.move(newPos);
            } else {
                final int[] newPos = { newCoord, pos[1] };
                newBoard = board.move(newPos);
            }
            if (newBoard.equals(board)) {
                i--;
            } else {
                board = newBoard;
                pos = newBoard.getPos();
            }
        }
        board.setDistance(0);
        return board;
    }

    public static int iterativeDFS(final Board initial, final Board solution) {
        int nodes = 0;
        res = false;
        for (int i = 0; !res; i++) {
            nodes += DFSIteration(initial, solution, i);
        }
        return nodes;
    }

    public static int DFSIteration(final Board initial, final Board solution, final int maxDepth) {
        final LinkedList<Board> list = new LinkedList<>();
        final HashSet<Board> visited = new HashSet<>();
        list.add(initial);
        int nodes = 0;
        while (!list.isEmpty()) {
            nodes++;
            final Board board = list.removeFirst();
            if (!visited.contains(board) && board.getDistance() < maxDepth)
                for (final Board b : nextMovements(board)) {
                    if (b.equals(solution)) {
                        res = true;
                        return nodes;
                    }
                    list.addFirst(b);
                }
            visited.add(board);
        }
        return nodes;
    }

    public static int DFS(final Board initial, final Board solution) {
        final LinkedList<Board> list = new LinkedList<>();
        final HashSet<Board> visited = new HashSet<>();
        list.add(initial);
        int nodes = 0;
        while (!list.isEmpty()) {
            nodes++;
            final Board board = list.removeFirst();
            if (!visited.contains(board))
                for (final Board b : nextMovements(board)) {
                    if (b.equals(solution))
                        return nodes;
                    list.addFirst(b);
                }
            visited.add(board);
        }
        return nodes;
    }

    public static int aEstrella(final Board initial, final Board solution) {
        final PriorityQueue<Board> list = new PriorityQueue<>();
        list.add(initial);
        int nodes = 0;
        while (!list.isEmpty()) {
            nodes++;
            for (final Board b : nextMovements(list.poll())) {
                if (b.equals(solution))
                    return nodes;
                b.setDistanceTotal(b.getDistance() + b.Manhattan());
                list.add(b);
            }
        }
        return nodes;
    }

    public static LinkedList<Board> nextMovements(final Board board) {
        final LinkedList<Board> list = new LinkedList<>();
        Board boardNew;
        final int[] pos = board.getPos();
        if( pos[0] != 0 ){
            boardNew = board.move(pos[0]-1, pos[1]);
            boardNew.setDistanceTotal(boardNew.getDistance()+boardNew.misplaced());
            list.add(boardNew);
        }
        if( pos[0] != 2 ){
            boardNew = board.move(pos[0]+1, pos[1]);
            boardNew.setDistanceTotal(boardNew.getDistance()+boardNew.misplaced());
            list.add(boardNew);
        }
        if( pos[1] != 0 ){
            boardNew = board.move(pos[0], pos[1]-1);
            boardNew.setDistanceTotal(boardNew.getDistance()+boardNew.misplaced());
            list.add(boardNew);
        }
        if( pos[1] != 2 ){
            boardNew = board.move(pos[0], pos[1]+1);
            boardNew.setDistanceTotal(boardNew.getDistance()+boardNew.misplaced());
            list.add(boardNew);
        }
        return list;
    }

}