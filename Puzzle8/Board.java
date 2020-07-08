public class Board implements Comparable<Board>{
    private byte[][] board = new byte[3][3];
    private final byte[][] goal = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
    private int[] pos = new int[2];

    private int Distance;
    private Integer distanceTotal;

    public Board(final byte[][] board) {
        this.board = board;
    }

    public Board(final byte[][] board, final int[] pos) {
        this.board = board;
        this.pos = pos;
        distanceTotal = getDistance();
    }

    public int getDistance() {
        return Distance;
    }

    public void setDistance(final int Distance) {
        this.Distance = Distance;
    }

    public void setDistanceTotal(final int DistanceTotal) {
        this.distanceTotal = DistanceTotal;
    }

    public int Manhattan() {
        int res = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int[] my = search(goal[i][j]);
                res += Math.abs(my[0] - i) + Math.abs(my[1] - j);
            }
        }
        return res;
    }

    public int[] search(final byte val) {
        final int[] res = new int[2];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (this.board[i][j] == val) {
                    res[0] = i;
                    res[1] = j;
                }
            }
        }
        return res;
    }

    public int misplaced() {
        int res = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                res += (goal[i][j] == this.board[i][j]) ? 0 : 1;
            }
        }
        return res;
    }

    public Board move(final int[] newPos) {
        final byte[][] res = { { board[0][0], board[0][1], board[0][2] }, { board[1][0], board[1][1], board[1][2] },
                { board[2][0], board[2][1], board[2][2] } };
        if (newPos[0] == pos[0] && (newPos[1] == pos[1] + 1 || newPos[1] == pos[1] - 1)) {
            res[pos[0]][newPos[1]] = board[pos[0]][pos[1]];
            res[pos[0]][pos[1]] = board[pos[0]][newPos[1]];
        } else if (newPos[1] == pos[1] && (newPos[0] == pos[0] + 1 || newPos[0] == pos[0] - 1)) {
            res[newPos[0]][pos[1]] = board[pos[0]][pos[1]];
            res[pos[0]][pos[1]] = board[newPos[0]][pos[1]];
        } else {
            return this;
        }
        final Board newBoard = new Board(res, newPos);
        newBoard.Distance = this.Distance + 1;
        return newBoard;
    }

    public Board move(final int x, final int y) {
        final int[] newPos = { x, y };
        return move(newPos);
    }

    public String toString() {
        String str = board[0][0] + " " + board[0][1] + " " + board[0][2] + "\n";
        str += board[1][0] + " " + board[1][1] + " " + board[1][2] + "\n";
        str += board[2][0] + " " + board[2][1] + " " + board[2][2] + "\n";
        return str;
    }

    @Override
    public boolean equals(final Object o) {
        final Board actualBoard = (Board) o;
        if (board.length != actualBoard.board.length || board[0].length != actualBoard.board[0].length)
            return false;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != actualBoard.board[i][j])
                    return false;
            }
        }
        return true;
    }

    public int[] getPos() {
        final int[] res = { pos[0], pos[1] };
        return res;
    }
}