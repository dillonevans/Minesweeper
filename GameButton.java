import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * The "Controller"
 */
public class GameButton extends JButton
{
    private static final long serialVersionUID = 3942095583293438553L;
    
    public GameButton(MineFrame frame, int row, int col)
    {
        super();
        this.addMouseListener(new MouseAdapter() 
        { 
            public void mouseClicked(MouseEvent e)
            {
                if (!frame.getModel().isGameOver())
                {
                    //Left-Click
                    if (e.getButton() == 1)
                    {
                        //If it's not flagged, reveal
                        if (frame.getModel().getState(row, col) != MineBoard.FLAGGED)
                        {
                            //If the revealed tile is a mine, end the game
                            if (frame.getModel().isMine(row, col))
                            {
                                frame.getModel().setGameOver(true);
                            }
                            //Otherwise, perform the reveal method as normal
                            else
                            {
                                frame.getModel().reveal(row, col);
                            }
                        }
                    }
                    //Right-Click
                    if (e.getButton() == 3 && frame.getModel().getState(row, col) != MineBoard.OPENED)
                    {
                        switch (frame.getModel().getState(row, col))
                        {
                            case MineBoard.HIDDEN_TILE:
                                frame.getModel().flag(row, col);
                                break;
                            case MineBoard.FLAGGED:
                                frame.getModel().question(row, col);
                                break;
                            case MineBoard.QUESTION:
                                frame.getModel().hide(row, col);
                                break;
                        }
                    }
                    
                    //Update the contents of the panel to reflect changes
                    frame.getPanel().updatePanel(row, col); 
                }
            }
        });
    }

    /**
     * Update the Image Icon of the GameButton
     * @param icon The Specified Icon
     * @param width The Specified Width for the Icon
     * @param height The Specified Height for the Icon
     */
    public void setImage(ImageIcon icon, int width, int height)
    {
        Image img = icon.getImage();             
        Image newImage = img.getScaledInstance(width, height, Image.SCALE_FAST);
        icon = new ImageIcon(newImage);
        this.setIcon(icon);
    }
}