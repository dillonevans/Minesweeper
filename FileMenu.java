import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.nio.file.*;
public class FileMenu extends JMenuBar implements ActionListener
{
    private static final long serialVersionUID = 459585961076461945L;
    private Path dir;
    private File file;
    private MineFrame frame;
    private JMenuItem easy, medium, hard,loadItem, exitItem, saveItem;
    private JFileChooser fileChooser;
    private FileNameExtensionFilter filter;
    private JMenu menu, newGameMenu;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public FileMenu(MineFrame frame)
    {
        super();
        this.frame = frame;
        initializeMenu();
    }

    private void initializeMenu()
    {
        exitItem = new JMenuItem("Quit Game");
        saveItem = new JMenuItem("Save Game");
        loadItem = new JMenuItem("Load Game");
        
        easy = new JMenuItem("Beginner: 10 x 10. 10 Mines");
        medium= new JMenuItem("Intermdiate: 16 x 16. 40 Mines");
        hard = new JMenuItem("Expert: 16 x 30. 99 Mines");

        dir = Paths.get("./Saves").toAbsolutePath();
        filter = new FileNameExtensionFilter("Files", "dat");
        fileChooser = new JFileChooser(dir.toString());

        menu = new JMenu("File");
        newGameMenu = new JMenu("New Game");

        add(menu);
        menu.add(newGameMenu);
        menu.add(saveItem);
        menu.add(loadItem);
        menu.add(exitItem);

        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);

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

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        String errorMessage = " ";
        Object source = e.getSource();
        JMenuItem temp = (JMenuItem)source;
        MineBoard board = null;
        int fileVal;
        boolean saved = false;
        
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

        if (!saved) //Any event other than saving requires updating the frame/board
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