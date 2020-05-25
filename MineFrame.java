import java.awt.*;
import javax.swing.*;

/**
 * This frame contains the "MinePanel" which serves as the view of the game.
 */
public class MineFrame extends JFrame
{
    private static final long serialVersionUID = 4816731330494815932L;
    
    private MineBoard board;
    private JLabel mineLabel;
    private MinePanel minePanel;
    private JPanel fileBarPanel, mainPanel, labelPanel;
    private FileMenu menu;
    private BorderLayout layout;

    public MineFrame(MineBoard board)
    {
        super("Minesweeper Java");
        this.board = board;
        
        fileBarPanel = new JPanel();
        mainPanel = new JPanel();
        labelPanel = new JPanel();
        menu = new FileMenu(this);
        layout = new BorderLayout();

        mineLabel = new JLabel();
        minePanel = new MinePanel(this);
        labelPanel.add(mineLabel);
        labelPanel.setBackground(Color.WHITE);
        mineLabel.setForeground(Color.RED);
        mineLabel.setFont(new Font("Times", Font.BOLD, 20));
        fileBarPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        fileBarPanel.add(menu);
        minePanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new GridLayout(2, 1));
        mainPanel.add(fileBarPanel);
        mainPanel.add(labelPanel);
        
        setLayout(layout);
        add(mainPanel, BorderLayout.NORTH);
        add(minePanel, BorderLayout.CENTER);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.WHITE);
        setVisible(true);
        setResizable(false);
        dynamicResize();
    }
    
    /**
     * Resizes the frame based on the number of tiles present on the screen
     */
    public final void dynamicResize()
    {
        int determinedHeight = MinePanel.IMAGE_SIZE * board.getRows();
        int determinedWidth = MinePanel.IMAGE_SIZE * board.getColumns();
        setSize(determinedWidth + 25, determinedHeight + 75); 
    }

    /**
     * Gets the model object associated with the frame.
     * @return The model object.
     */
    public MineBoard getModel()
    {
        mineLabel.setText(Integer.toString(board.getFlagCount()));
        return board;
    }

    /**
     * Updates the model object of the frame.
     * @param board The model to change to.
     */
    public void setModel(MineBoard board)
    {
        this.board = board;
        minePanel.refresh();
    }

    /**
     * Returns the MinePanel object displayed in the frame
     * @return the Minepanel object
     */
    public MinePanel getPanel()
    {
        return this.minePanel;
    }

}