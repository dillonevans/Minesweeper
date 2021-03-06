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
                        frame.getModel().reveal(row, col);
                    }
                    //Right-Click
                    else if (e.getButton() == 3)
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