import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
/**
 * The "View"
 */
public class MinePanel extends JPanel
{
    private static final long serialVersionUID = 1422257141027993415L;
    public static final int IMAGE_SIZE = 32;
    private final ImageIcon hidden = new ImageIcon("img/hidden.png");
    private final ImageIcon flagged = new ImageIcon("img/flagged.png");
    private final ImageIcon mine = new ImageIcon("img/mine.png");
    private final ImageIcon openedMine = new ImageIcon("img/openedMine.png");
    private final ImageIcon falselyFlagged = new ImageIcon("img/falselyFlaggedMine.png");
    private final ImageIcon query = new ImageIcon("img/question.png");
    private List<ImageIcon> iconsList;
    private MineFrame frame;
    private GridLayout grid; 
    private int rows, columns;
    private GameButton[][] tiles;
    
    public MinePanel(MineFrame frame)
    {
        super();
        this.frame = frame;
        rows = frame.getModel().getRows();
        columns = frame.getModel().getColumns();
        tiles = new GameButton[rows][columns];
        iconsList = initalizeImageList();
        grid = new GridLayout(rows,columns);
        setLayout(grid);

        for (int x = 0; x < rows; x++)
        {
            for (int y = 0; y < columns; y++)
            {
                GameButton tile = new GameButton(frame, x, y);
                tiles[x][y] = tile;
                tiles[x][y].setImage(hidden, IMAGE_SIZE, IMAGE_SIZE);
                add(tile);
            }
        }
        repaint();
    }

    /**
     * Populates a List with ImageIcons for later use
     * @return The populated List
     */
    public List<ImageIcon> initalizeImageList()
    {
        List<ImageIcon> icons = new ArrayList<>(9);
        icons.add(new ImageIcon("img/opened.png"));
        icons.add(new ImageIcon("img/one.png"));
        icons.add(new ImageIcon("img/two.png"));
        icons.add(new ImageIcon("img/three.png"));
        icons.add(new ImageIcon("img/four.png"));
        icons.add(new ImageIcon("img/five.png"));
        icons.add(new ImageIcon("img/six.png"));
        icons.add(new ImageIcon("img/seven.png"));
        icons.add(new ImageIcon("img/eight.png"));
        return icons;
    }

    /**
     * Reinitializes the panel
     */
    public void refresh()
    {
        removeAll();
        rows = frame.getModel().getRows();
        columns = frame.getModel().getColumns();
        tiles = new GameButton[rows][columns];
        grid = new GridLayout(rows,columns);
        setLayout(grid);

        for (int x = 0; x < rows; x++)
        {
            for (int y = 0; y < columns; y++)
            {
                GameButton tile = new GameButton(frame, x, y);
                tiles[x][y] = tile;
                updateTileImage(x, y, frame.getModel().getState(x, y));
                add(tile);
            }
        }
        repaint();
        validate();
    }

    /**
     * This method is only called when a button is clicked, so 
     * it only updates the tiles when it has to. This improves 
     * efficiency by not updating every time the frame is repainted.
     * Furthermore, it only updates every tile if a recursive reveal happens
     * or a mine is hit, otherwise it singly updates the clicked tile.
     * @param r The row of the current tile.
     * @param c The column of the current tile.
     * @see updateTileImage(int row, int col, int state)
     */
    public void updatePanel(int r, int c)
    {
        int state = 0;

        //In this case, recursive reveal happens so every tile needs to be checked
        if(frame.getModel().determineAdjacent(r, c) == 0)
        {
            for (int row = 0; row < rows; row++)
            {
                for (int column = 0; column < columns; column++)
                {
                    state = frame.getModel().getState(row, column);
                    updateTileImage(row, column, state);      
                }
            }
        }
        //Otherwise, only one tile needs to be updated
        else 
        {
            updateTileImage(r, c, frame.getModel().getState(r, c));
        }

           
        //Display all mines if the player loses
        if (frame.getModel().isGameOver() && frame.getModel().getMineCount() > 0)
        {
            for (int row = 0; row < rows; row++ )
            {
                for (int column = 0; column < columns; column++)
                {
                    //If the tile flagged isn't a mine, display the falsely flagged mine image
                    if (!frame.getModel().isMine(row, column) && frame.getModel().getState(row, column) == MineBoard.FLAGGED)
                    {
                        tiles[row][column].setImage(falselyFlagged, IMAGE_SIZE, IMAGE_SIZE); 
                    }

                    //Otherwise, if the tile is a mine and is not flagged, display the mine
                    else if (frame.getModel().isMine(row, column) && frame.getModel().getState(row, column) != MineBoard.FLAGGED)
                    {
                        tiles[row][column].setImage(mine, IMAGE_SIZE, IMAGE_SIZE); 
                    }
                }
            }

            //Update the clicked tile to display that it was the opened mine
            tiles[r][c].setImage(openedMine, IMAGE_SIZE, IMAGE_SIZE);
            JOptionPane.showMessageDialog(null, "Game Over. Mines Remaining: " + frame.getModel().getMineCount());
        }

        //Otherwise, print out a method indicating the player's success
        else if (frame.getModel().isGameOver() && frame.getModel().getMineCount() == 0)
        {
            JOptionPane.showMessageDialog(null, "You Win!");
        } 
        repaint();
    }

    /**
     * Updates the image of the specified tile based upon its state.
     * @param row The row of the specified tile.
     * @param column The column of the specified tile.
     * @param state The state of the specified tile.
     */
    private void updateTileImage(int row, int column, int state)
    {
        //Perform state lookup and assign the associated image
        switch (state)
        {  
            case MineBoard.QUESTION: tiles[row][column].setImage(query, IMAGE_SIZE, IMAGE_SIZE);
                break;
            case MineBoard.FLAGGED: tiles[row][column].setImage(flagged, IMAGE_SIZE, IMAGE_SIZE);
                break;
            case MineBoard.HIDDEN_TILE: tiles[row][column].setImage(hidden, IMAGE_SIZE, IMAGE_SIZE);
                break;
            //This handles tiles with an adjacency of 0-8
            default: 
                tiles[row][column].setImage(iconsList.get(state), IMAGE_SIZE, IMAGE_SIZE);
                break;          
        }  
    }
}
