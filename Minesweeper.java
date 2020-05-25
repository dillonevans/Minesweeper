
public class Minesweeper
{
    public static void main(String[] args)
    {
        MineBoard board = new MineBoard(16,30,99);
        MineFrame mFrame = new MineFrame(board);
    }
}