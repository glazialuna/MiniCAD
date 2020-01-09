import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class MenuBar extends JMenuBar{

	private static final long serialVersionUID = 1L;
	private ArrayList<Shape> list;
	private boolean updated = false; // 表示文件被更新
	private boolean saved = false; // 标识save文件加载
	private ObjectInputStream  input;
	private ObjectOutputStream output;
	private File fileName;
	JMenuBar menu = new JMenuBar();
	
	public MenuBar(ArrayList<Shape> list) {
		// TODO Auto-generated constructor stub
		this.list = list;
		this.setLayout(null);
		this.setBounds(100, 75, 650, 25);
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.setFont(new Font("bold",5,15));
		fileMenu.setBounds(5, 5, 50, 15);
		
		JMenuItem newItem = new JMenuItem("New");
		newItem.setFont(new Font("bold",5,15));
		newItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				newfile();
			}
		});
		fileMenu.add(newItem);
		
		JMenuItem loadItem = new JMenuItem("Load");
		loadItem.setFont(new Font("bold",5,15));
		loadItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				loadfile();
			}
		});
		fileMenu.add(loadItem);
		
		JMenuItem saveItem = new JMenuItem("Save");
		saveItem.setFont(new Font("bold",5,15));
		saveItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				savefile();
			}
		});
		fileMenu.add(saveItem);
		
		this.add(fileMenu);

	}
	public void newfile() {
		int yes = 0;
		if(list.size() !=0 && updated) {
			yes = JOptionPane.showConfirmDialog(null, "Current project is not saved","ff",JOptionPane.YES_NO_OPTION);
		}
		if(yes == 0) {
			list.clear();
			saved = false;
			updated = false;
		}
	}
	
	public void loadfile() {
		int yes = 0;
		if(list.size() !=0 && updated) {
			yes = JOptionPane.showConfirmDialog(null, "Current project is not saved","",JOptionPane.YES_NO_OPTION);
		}
		if(yes == 0) {
			JFileChooser fileChooser=new JFileChooser();
		    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		    if(fileChooser.showOpenDialog(this)==JFileChooser.CANCEL_OPTION)
		          return;
		    fileName=fileChooser.getSelectedFile();
		    if (fileName==null||fileName.getName().equals(""))
		       JOptionPane.showMessageDialog(fileChooser,"Invalid File Name","Invalid File Name", JOptionPane.ERROR_MESSAGE);
		    else {
		    	try {
		    		FileInputStream fin=new FileInputStream(fileName);
		    		input=new ObjectInputStream(fin);
		    		list.clear();
		    		Shape inShape;
		    		int count=input.readInt();
		    		for(int i=0 ;i<count ;i++)
		    		{
		    			Shape shape = (Shape)input.readObject();
		    			list.add(shape);
		    		}
		    		input.close();
		    		saved = true;
			    	updated = false;
		    		}catch(EOFException endofFileException){
		    			JOptionPane.showMessageDialog(this,"no more record in file","class not found",JOptionPane.ERROR_MESSAGE );
		    		}catch(ClassNotFoundException classNotFoundException){
		    			JOptionPane.showMessageDialog(this,"Unable to Create Object","end of file",JOptionPane.ERROR_MESSAGE );
		    		}catch (IOException ioException){
		    			JOptionPane.showMessageDialog(this,"error during read from file","read Error",JOptionPane.ERROR_MESSAGE );
		    		}
		    	}
		   }
	}

	public void savefile() {
		if(saved)
		{
			try {
				fileName.delete();
				FileOutputStream fout=new FileOutputStream(fileName);
				output=new ObjectOutputStream(fout);
				
				output.writeInt(list.size());
				for(int i=0;i < list.size();i++){
			    	   Shape shape = (Shape)list.get(i);
			    	   output.writeObject(shape);
			    	   output.flush();
			    }
			       
				output.close();
				fout.close();
				updated = false;
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		else {
			JFileChooser fileChooser=new JFileChooser();
		    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		    if(fileChooser.showSaveDialog(this)==JFileChooser.CANCEL_OPTION)
		             return ;
		    fileName=fileChooser.getSelectedFile();

		    if (fileName==null||fileName.getName().equals(""))
		    JOptionPane.showMessageDialog(fileChooser,"Invalid File Name","Invalid File Name", JOptionPane.ERROR_MESSAGE);
		    else{
		      try {
		    	  fileName.delete();
		    	  FileOutputStream fout=new FileOutputStream(fileName);
		    	  output=new ObjectOutputStream(fout);
		       
		    	  output.writeInt(list.size());
		       
		    	  for(int i=0;i < list.size();i++){
		    		  Shape shape = (Shape)list.get(i);
		    		  output.writeObject(shape);
		    		  output.flush();
		    	  }
		       
		    	  output.close();
		    	  fout.close();
		    	  updated = false;
		    	  saved = true;
		       }catch(IOException e){
		    	   e.printStackTrace();
		       }
		    }
		}
	}
	
	public void setUpdated(boolean set) {
		this.updated = set;
	}
}