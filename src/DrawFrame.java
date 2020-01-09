//package src;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.BevelBorder;
//放置直线、矩形、圆和文字，能选中图形，修改参数，如颜色等，能拖动图形和调整大小
class DrawFrame extends JFrame{
	private ArrayList<Shape> list = new ArrayList<Shape>();
	JFrame frame = new JFrame("MiniCAD");
	JPanel panel = new JPanel();
	MenuBar menubar = new MenuBar(list);
	private String select_msg = "";
	private Graphics2D g;
	private ButtonGroup buttons = new ButtonGroup();
	private DrawShape drawShape;
	
	public DrawFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
        frame.setVisible(true);    
        
        panel.setLayout(null);      
        addbg(); // 添加背景      
        addcanvas(); // 添加画板内容     
        addmenu(); // 菜单
        addpaint(); // 添加右侧画面，包括图标变色，鼠标icons加载，调色盘  
        frame.setBounds(150, 50, 900, 800);    //设置窗口大小和位置     
	}
	private void addmenu() {
		panel.add(menubar);
		panel.setOpaque(false);
		frame.add(panel);
	}
	private void addbg() {
		//背景图片
		ImageIcon icon_bg=new ImageIcon("image/bg.jpg");
		JLabel label_bg=new JLabel(icon_bg);
		label_bg.setBounds(0, 0, 900,800);		
		frame.getLayeredPane().add(label_bg,new Integer(Integer.MIN_VALUE));
		JPanel j=(JPanel)frame.getContentPane();
		j.setOpaque(false);
	}
	private void addcanvas(){
		JPanel canvas = new JPanel() {
			public void paint(Graphics g1) {
				super.paint(g1);
				Graphics2D g = (Graphics2D)g1;
				for(int i=0;i<list.size();i++) {
					Shape shape = (Shape)list.get(i);
					shape.draw(g);
				}
				this.repaint();
			}
		};
		canvas.setLayout(null);
		canvas.setBounds(100, 100, 600, 500);
		canvas.setBackground(Color.white);
		
		JTextField tesTextField = new JTextField();
		tesTextField.setVisible(true);
		tesTextField.setLocation(10, 10);
		tesTextField.setBackground(Color.green);
		canvas.add(tesTextField);
		
		// 设置鼠标icons
		Cursor[] cursors = new Cursor[6];
		Toolkit tk = Toolkit.getDefaultToolkit();
		for(int i=1;i<=3;i++) {
			Image icon_cursor = new ImageIcon("image/cursor"+i+".png").getImage();//.getScaledInstance(10, 10,Image.SCALE_DEFAULT);
			cursors[i-1] = tk.createCustomCursor(icon_cursor, new Point(20,50), "cursor"+i);
		}

		
		frame.setVisible(true);				
		g=(Graphics2D)canvas.getGraphics(); 
		drawShape =new DrawShape (g, buttons, canvas, cursors,list, menubar); 				
		canvas.addMouseListener(drawShape);		
		canvas.addMouseMotionListener(drawShape);		
		
        panel.add(canvas);
        panel.setOpaque(false);
        frame.add(panel);
	}

	private void addpaint() {
		JPanel menu = new JPanel();
		menu.setLayout(null);
		menu.setBounds(700, 100, 50, 500);
		
		
		for(int i=1;i<=6;i++) {
			ImageIcon icon1 = new ImageIcon("image/icon0"+ i +"1.png");
			icon1.setImage(icon1.getImage().getScaledInstance(50, 50,Image.SCALE_DEFAULT));
			ImageIcon icon2 = new ImageIcon("image/icon0"+ i +"2.png");
			icon2.setImage(icon2.getImage().getScaledInstance(50, 50,Image.SCALE_DEFAULT));
			ImageIcon icon3 = new ImageIcon("image/icon0"+ i +"3.png");
			icon3.setImage(icon3.getImage().getScaledInstance(50, 50,Image.SCALE_DEFAULT));
			
			JRadioButton button = new JRadioButton(icon1);
			button.setBorderPainted(false);
			button.setContentAreaFilled(false);
			button.setPressedIcon(icon3);
			button.setSelectedIcon(icon3);
			
			button.setRolloverIcon(icon2);
			button.setActionCommand("icon" + i);
			button.addActionListener(new ActionListener() {
				@Override
	            public void actionPerformed(ActionEvent e) {
	            	String msg = e.getActionCommand();	
	            	if (msg.equals(select_msg)) {
	            		buttons.clearSelection();
	            		select_msg = "";
	            	}else {
	            		select_msg = buttons.getSelection().getActionCommand();
					}
	            }
	        });
			
			button.setBounds(0, 50 * (i-1) , 50, 50);
			buttons.add(button);
			menu.add(button);
			
		}
		// 设置画笔大小
		JPanel brush = new JPanel();
		int brush_x1, brush_x2;
		brush.setLayout(null);
		brush.setBounds(5, 395, 40, 2);
				
		JButton size = new JButton();
		size.setBounds(0, 0, 2, 5);
		size.setEnabled(false);
		brush.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
					
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				int y = size.getY();
				int x = e.getX();
				if(x >= 0 && x <= 70) {
					size.setLocation(x, y);
					drawShape.setStroke(0.1 * x);
				}
			}
		});
		brush.add(size);
		menu.add(brush);
		
		// 调色板设置
		JPanel pallet = new JPanel();
		pallet.setLayout(null);
		pallet.setBounds(5, 400, 40, 80);
		
		Color []colors = {new Color(0,0,0),new Color(255,255,255),new Color(255,0,0)
		,new Color(0,255,0),new Color(0,0,255),new Color(255,255,0)
		,new Color(255,0,255),new Color(0,255,255)};

		for(int i=0;i<8;i++) {
			JButton bt = new JButton();
			bt.setBackground(colors[i]);
			bt.setBounds(20*(i%2),20*(int)(i/2), 20, 20);
			bt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JButton btButton = (JButton)e.getSource();
					Color c = btButton.getBackground();
					brush.setBackground(c);
					drawShape.setColor(c);
				}
			});
			pallet.add(bt);
		}
		drawShape.setColor(colors[0]); // 初始画笔黑色
		brush.setBackground(colors[0]);
		menu.add(pallet);
		
		
		
		panel.add(menu);
		panel.setOpaque(false);
		frame.add(panel);
	}
	
	
}

