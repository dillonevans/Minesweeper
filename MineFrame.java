import java.awt.*;
import javax.swing.*;

public class MineFrame extends JFrame
{
    private static final long serialVersionUID = 4816731330494815932L;
    
    private MineBoard board;
    private JLabel mineLabel;
    private JPanel fileBarPanel, mainPanel, labelPanel;
    private MinePanel minePanel;
    private BorderLayout layout;
    private FileMenu fileMenu;

    public MineFrame(MineBoard board)
    {
        super("Minesweeper Java");
        this.board = board;
        initializeFrame();
    }

    /**
     * Initializes the contents of the frame
     */
    private void initializeFrame()
    {
        fileBarPanel = new JPanel();
        mainPanel = new JPanel();
        labelPanel = new JPanel();
        mineLabel = new JLabel("Mines");
        fileMenu = new FileMenu(this); //File Menu
        minePanel = new MinePanel(this); //Houses Mine Tiles
        layout = new BorderLayout();

        labelPanel.add(mineLabel);
        labelPanel.setBackground(Color.WHITE);
        mineLabel.setForeground(Color.RED); 
        mineLabel.setFont( new Font("Times", Font.BOLD, 20));
        fileBarPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        fileBarPanel.add(fileMenu); //Add the menu to the file bar panel
        fileBarPanel.setBackground(Color.WHITE);
        minePanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new GridLayout(2, 1));
        mainPanel.add(fileBarPanel); 
        mainPanel.add(labelPanel);
        mainPanel.setBackground(Color.WHITE);

        dynamicResize();
        setLayout(layout);
        add(mainPanel, BorderLayout.NORTH);
        add(minePanel, BorderLayout.CENTER);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
    }

    /**
     * Resizes the frame based on the number of tiles present on the screen
     */
    public void dynamicResize()
    {
        int determinedHeight = minePanel.getImageSize() * board.getRows();
        int determinedWidth = minePanel.getImageSize() * board.getColumns();
        setSize(determinedWidth, determinedHeight + 50); 
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