import java.io.Serializable;

/**
 * The Model
 */
public class MineBoard implements Serializable
{
    private static final long serialVersionUID = -8818357894631176438L;
    public static final int MINE = -1;
    public static final int HIDDEN = 0;
    public static final int OPENED = 3;
    public static final int FLAGGED = -2;
    public static final int QUESTION = -3;
    public static final int HIDDEN_TILE = -1;
    public static final int ZERO_TILE = 0;
    private int rows, columns, maximumMines, flagCount, mineCount;
    private boolean gameOver = false;
    private int[][] board, gameBoard;
    private boolean firstClick = true;
    

    /**
     * Instantiates the model.
     * @param r The number of rows.
     * @param c The number of columns.
     * @param mines The number of mines.
     */
    public MineBoard(int r, int c, int mines)
    {
        mineCount = mines;
        rows = r;
        columns = c;
        maximumMines = flagCount = mineCount;

        board = new int[rows][columns];
        gameBoard = new int[rows][columns];

        for (int row = 0; row < rows; row++)
        {
            for (int col = 0; col < columns; col++)
            {
                gameBoard[row][col] = HIDDEN_TILE;      
            }
        }
    }  

    /**
     * Fills the grid with randomly generated mines around the player's initial click. This
     * method ensures that the generated mine will never be the initial tile and that the initial tile is 
     * always a zero-tile.
     * @param xInitial The x coordinate of the initial tile.
     * @param yInitial The y coordinate of the initial tile.
     */
    public void generateMines(int xInitial, int yInitial)
    {
        int x, y;
        int count = 0;
        
        //Keep generating mines until the max is reached
        while (count < maximumMines)
        {  
            //Ensures that the first tile cannot be a mine
            do 
            {
                x = randXCoord(); 
                y = randYCoord();
            }
            while (x == xInitial && y == yInitial);

            if (board[x][y] != MINE)
            {
                //Set the current tile to be a mine to test adjacency
                board[x][y] = MINE;

                //If the initial tile has no adjacent mines, proceed as normal
                if (determineAdjacent(xInitial, yInitial) == 0) {count++;}
    
                //Otherwise, set the tile to hidden and try again
                else {board[x][y] = HIDDEN;}
            }
        }
    }
    
    /**
     * Generates a random x coordinate based upon the number of rows in the board.
     * @return The generated x coordinate.
     */
    public int randXCoord() {return 1 + (int)((Math.random() * rows - 1));}

    /**
     * Generates a random y coordinate based upon the number of columns in the board
     * @return The generated y coordinate
     */
    public int randYCoord() {return 1 + (int)((Math.random() * columns - 1));}
   
    /**
     * Determines whether or not the tile at the specified position is a mine or not.
     * @param row The row of the tile to evaluate.
     * @param col The column of the tile to evaluate.
     * @return True if the tile is a mine
     */
    public boolean isMine(int row, int col) {return board[row][col] == MINE;}

    /**
     * Determines the number of adjacent mines in a 3x3 square.
     * @param row Desginates the row of the tile.
     * @param col Designates the column of the tile.
     * @return The number of mines adjacent to the current tile.
     */
    public int determineAdjacent(int row, int col)
    {
        int adjacent = 0;
        
        //Bottom three adjacent
        if (row + 1 < rows)
        {
            //Bottom-Center
            if (board[row + 1][col] == MINE)
            {
                adjacent++;
            }
            if (col + 1 < columns)
            {
                //Bottom-Right
                if (board[row + 1][col+1] == MINE)
                {
                    adjacent++;
                }
            }
            if (col - 1 >= 0)
            {
                //Below-Left
                if (board[row + 1][col-1] == MINE)
                {
                    adjacent++;
                }
            }
         }

         //Top three adjacent
         if (row - 1 >= 0)
         {
             if (col + 1 < columns)
             {
                //Top-Right
                if (board[row - 1][col+1] == MINE)
                {
                    adjacent++;
                }
            }
            if (col - 1 >= 0)
            {
                //Top-Left
                if (board[row - 1][col-1] == MINE)
                {
                    adjacent++;
                }
            }
            //Top-Center
            if (board[row - 1][col] == MINE)
            {
                adjacent++;
            }
         }

         //Left and Right adjacent
         if (col + 1 < columns)
         {
            //Right-Adjacent
            if (board[row][col+1] == MINE)
            {
                adjacent++;
            }
        }
            
        if (col - 1 >= 0)
        {
            //Left-Adjacent
            if (board[row][col-1] == MINE)
            {
                adjacent++;
            }
        }

        return adjacent;
    }

    /**
     * Reveals the current tile
     * @param row Designates the row
     * @param col Designates the column
     * @see revealAll
     */
    public void reveal(int row, int col)
    {
        //If the tile is already opened, nothing else needs to occur.
        if (board[row][col] == OPENED) {return;}

        //If it's the first move of the game, generate mines and reveal nearby tiles
        if (firstClick)
        {
            generateMines(row, col);
            revealAll(row, col);
            firstClick = false;
        }
        else //Otherwise, reveal as normal
        {
            int adjacent = determineAdjacent(row, col);

            if (isMine(row, col))
            {
                setGameOver(true);
            }
            else
            {
                if (adjacent == 0) {revealAll(row, col);}
                gameBoard[row][col] = adjacent;
                board[row][col] = OPENED;
            }
        }
    }

     /**
     * Recursively reveals all nearby tiles with 0 adjacent mines. 
     * @param row Designates the row of the tile.
     * @param col Designates the column of the tile.
     * @see reveal
     */
    private void revealAll(int row, int col)
    {
        //If the row and/or column is out of bounds, return control to calling instance.
        if (col < 0 || row < 0 || row > rows - 1|| col > columns - 1) {return;}
        //If a mine or visited tile is encountered, return control to calling instance.
        if (isMine(row, col) || board[row][col] == OPENED) {return;}
        //If the tile is flagged, increment the number of available flags
        if (gameBoard[row][col] == FLAGGED) {flagCount++;}

        //Mark the visited tile as opened, and note the number of adjacent mines.
        int adjacent = determineAdjacent(row, col);
        board[row][col] = OPENED;
        gameBoard[row][col] = adjacent;

        //If the tile is not a zero-tile, return control to the calling instance.
        if (adjacent > 0) {return;} 

        //Recursively reveal nearby zero-tiles
        revealAll(row - 1, col + 1);      
        revealAll(row + 1, col + 1);
        revealAll(row, col - 1);
        revealAll(row, col + 1);
        revealAll(row - 1, col);
        revealAll(row + 1, col);
        revealAll(row - 1, col - 1);
        revealAll(row + 1, col - 1);
    }

    /**
     * Cycles through the states of a tile based on the tile's current state.
     * @param row The row of the specified tile.
     * @param col THe column of the specified tile.
     */
    public void setState(int row , int col)
    {
        int state = gameBoard[row][col];
        switch(state)
        {
            case HIDDEN_TILE: //Transition from Hidden to Flagged
                if (flagCount > 0) 
                {
                    state = FLAGGED; flagCount--; 
                    if (board[row][col] == MINE) {mineCount--;}
                    if (flagCount == 0 && mineCount == 0) {setGameOver(true);}
                }
                else {state = QUESTION;}
                break;

            case FLAGGED: //Transition from Flagged to Questioned
                state = QUESTION; 
                if (flagCount >= 0)
                flagCount++;
                if (board[row][col] == MINE) {mineCount++;}
                break;

            case QUESTION: //Transition from Questioned back to Hidden
                state = HIDDEN_TILE;
                break;        
        }
        gameBoard[row][col] = state;
    }

    /**
     * Returns the state of the specified tile.
     * @param row Designates the row.
     * @param col Desginates the column.
     * @return
     */
    public int getState(int row, int col) {return gameBoard[row][col];}

    /**
     * Declares whether or not the game should end.
     * @param gameOver boolean value representing the game's state.
     */
    public void setGameOver(boolean gameOver) {this.gameOver = gameOver;}

    /**
     * Returns True if the game is over.
     * @return True if the game is over.
     */
    public boolean isGameOver() {return gameOver;}

    public int getMineCount() {return mineCount;}

    public int getRows() {return rows;}

    public int getColumns() {return columns;}

    public int getFlagCount() {return flagCount;}
} 