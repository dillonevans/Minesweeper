import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

/**
 * This class acts as a menu for the player. Here they can change the difficult,
 * save and load the game, or exit.
 */
public class FileMenu extends JMenuBar implements ActionListener
{
    private static final long serialVersionUID = 459585961076461945L;
    private JFileChooser fileChooser;
    private File file;
    private MineFrame frame;
    private JMenuItem easy, medium, hard, loadItem, exitItem, saveItem;
    private FileNameExtensionFilter filter;
    private JMenu menu, newGameMenu;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public FileMenu(MineFrame frame)
    {
        super();
        this.frame = frame;
        exitItem = new JMenuItem("Quit Game");
        saveItem = new JMenuItem("Save Game");
        loadItem = new JMenuItem("Load Game");
        
        easy = new JMenuItem("Beginner: 10 x 10. 10 Mines");
        medium = new JMenuItem("Intermdiate: 16 x 16. 40 Mines");
        hard = new JMenuItem("Expert: 16 x 30. 99 Mines");
        filter = new FileNameExtensionFilter("Saved Game Data", "dat");
        menu = new JMenu("File");
        newGameMenu = new JMenu("New Game");

        add(menu);
        menu.add(newGameMenu);
        menu.add(saveItem);
        menu.add(loadItem);

        menu.add(exitItem);
        newGameMenu.add(easy);
        newGameMenu.add(medium);
        newGameMenu.add(hard);

        easy.addActionListener(this);
        medium.addActionListener(this);
        hard.addActionListener(this);
        saveItem.addActionListener(this);  
        loadItem.addActionListener(this);
        exitItem.addActionListener(this);  
    }

    /**
     * This event occurs when the player clicks a menu item.
     */
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        fileChooser = new JFileChooser(".");
        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        String errorMessage = " ";
        Object source = e.getSource();
        JMenuItem temp = (JMenuItem)source;
        MineBoard board = null;
        int fileVal;
        boolean saved = false;

        //This block of if-else statements handles player decisions.
        if (temp.equals(easy))
        {
            board = new MineBoard(10,10,10);
        }
        else if (temp.equals(medium))
        {
            board = new MineBoard(16,16,40);
        }
        else if (temp.equals(hard))
        {
            board = new MineBoard(16,30,99);
        }
        else if (temp.equals(exitItem))
        {
            System.exit(0);
        }
        else if (temp.equals(loadItem))
        {
            //Error handling for any possible issue(s) while loading a file. 
            try
            {  
              
                fileVal = fileChooser.showOpenDialog(temp);
                if (fileVal == JFileChooser.APPROVE_OPTION)
                {
                    file = fileChooser.getSelectedFile();
                    if (!file.exists()) 
                    {
                        throw new Exception("The File Specified Does Not Exist");
                    }     
                    in = new ObjectInputStream(new FileInputStream(file.getPath()));
                    board = (MineBoard)in.readObject();
                    in.close();
                }
                else
                {
                    throw new Exception("No File Was Selected");
                }
            }         
            catch (ClassNotFoundException ex)
            {
                errorMessage = "Valid Game Data Not Found";
                JOptionPane.showMessageDialog(null, errorMessage,"Error", JOptionPane.ERROR_MESSAGE);
                board = frame.getModel();
            }
            catch (ObjectStreamException ex)
            {
                errorMessage = "This File Cannot Be Loaded";
                JOptionPane.showMessageDialog(null, errorMessage,"Error", JOptionPane.ERROR_MESSAGE);
                board = frame.getModel();
            }      
            catch (Exception ex)
            {
                errorMessage = ex.getMessage();
                JOptionPane.showMessageDialog(null, errorMessage,"Error", JOptionPane.ERROR_MESSAGE);
                board = frame.getModel();
            }
        }

        else if (temp.equals(saveItem))
        {
            saved = true;
            //Error handling for any possible issue(s) while saving a file.
            try
            {
                fileVal = fileChooser.showSaveDialog(temp);
                if (fileVal == JFileChooser.APPROVE_OPTION)
                {
                    file = fileChooser.getSelectedFile();
                    
                    if (!file.exists())
                    {
                        file.createNewFile();
                    }
                    out = new ObjectOutputStream(new FileOutputStream(file.getPath()));
                    out.writeObject(frame.getModel());
                    out.close();
                }
            }               
            catch (Exception ex)
            {
                board = frame.getModel();
            }
        }
        
        //Any event other than saving requires updating the frame/board
        if (!saved) 
        {
            frame.setModel(board);
            updateFrame();
        }
    }

    /**
     * Updates the contents of the frame
     */
    private void updateFrame()
    {
        frame.dynamicResize();
        frame.getPanel().refresh();
    }
}