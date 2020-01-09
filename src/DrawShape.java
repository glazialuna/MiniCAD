//package src;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.rmi.activation.ActivationGroupDesc.CommandEnvironment;
import java.util.ArrayList;

import javax.swing.*;

//public class

public class DrawShape implements MouseListener,MouseMotionListener{
	private Graphics2D g;
	private ButtonGroup buttons;
	private JPanel canvas;
	private Color color;
	private Cursor[] cursors;
	private int x1,x2,y1,y2;
	private double stroke = 1;
	private int flag = -1;
	private int isclicked = 0;
	private ArrayList<Shape> list;
	private MenuBar menubar;
	
	private Shape clicked;
	
	
	public DrawShape(Graphics g, ButtonGroup buttons, JPanel canvas, Cursor[] cursors, ArrayList<Shape> list, MenuBar menubar) {
		this.g = (Graphics2D)g;
		this.buttons = buttons;
		this.canvas = canvas;
		this.cursors = cursors;
		this.list = list;
		this.menubar = menubar;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public Color getColor() {
		return this.color;
	}
	public void setStroke(double stroke) {
		this.stroke = stroke;
	}
	public double getStroke() {
		return this.stroke;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// 设置button作用
		if(buttons.getSelection() != null) {
			ButtonModel eButtonModel = buttons.getSelection();
			String command = eButtonModel.getActionCommand();
			flag = command.charAt(4)-'0';
		}else {
			flag = -1;
		}
		
		// 设置进入画布的鼠标图案
		//System.out.print(flag);
		if(flag >= 1 && flag <= 3) {
			canvas.setCursor(cursors[0]);
		}
		else {
			canvas.setCursor(Cursor.getDefaultCursor());
		}
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		x1 = e.getX();
		y1 = e.getY();
		
//		System.out.println("Press X:" + e.getX() + "Y:" + e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		x2 = e.getX();
		y2 = e.getY();
//		System.out.println("Release X:" + e.getX() + "Y:" + e.getY());
		switch (flag) {
		case 1:
			Shape line = new Line(x1, y1, x2, y2, color, stroke);
        	list.add(line);
        	menubar.setUpdated(true);
//        	System.out.println("add line");
			break;
		case 2:
			Shape oval = new Oval(Math.min(x2, x1), Math.min(y2, y1), Math.abs(x2-x1),Math.abs(y1-y2), color, stroke);
        	list.add(oval);
        	menubar.setUpdated(true);
//        	System.out.println("add oval");
			break;
		case 3:
			Shape rec = new Rectangle(Math.min(x2, x1),Math.min(y2, y1), Math.abs(x2-x1),Math.abs(y1-y2), color, stroke);
			list.add(rec);
			menubar.setUpdated(true);
//        	System.out.println("add rec");
			break;
		case 4:
			String str = JOptionPane.showInputDialog("Please input the text: ");
			if(str != null) {
				Text text = new Text(x2, y2, color, stroke, str);
				list.add(text);
				menubar.setUpdated(true);
			}
		case 5:
			if(isclicked == 1) {
				clicked.updateShape();
				menubar.setUpdated(true);
			}
			isclicked = 0;
		case 6:
			if(isclicked == 1) {
				clicked.updateShape();
				menubar.setUpdated(true);
			}
			isclicked = 0;
		default:
			break;
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
		if(isclicked == 1) {
			switch (flag) {
			case 5:
				clicked.moveShape(e.getX()-x1, e.getY()-y1);
				menubar.setUpdated(true);
				break;
			case 6:
				clicked.resizeShape(e.getX()-x1, e.getY()-y1);
				menubar.setUpdated(true);
				break;
			default:
				break;
			}
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		// 设置鼠标移动时的图标
//		System.out.println("isclicke" + isclicked);
		if(flag == 5) {
			for(int i=list.size()-1;i>=0;i--) {
				Shape shape = (Shape)list.get(i);
				if(shape.isContains(e.getX(), e.getY())) {
					canvas.setCursor(cursors[1]);
					isclicked = 1;
					clicked = shape;
					break;
				}	
				else {
					isclicked = 0;
					canvas.setCursor(Cursor.getDefaultCursor());
				}
			}
		}else if(flag == 6) {
			for(int i=list.size()-1;i>=0;i--) {
				Shape shape = (Shape)list.get(i);
				if(shape.isContains(e.getX(), e.getY())) {
					canvas.setCursor(cursors[2]);
					isclicked = 1;
					clicked = shape;
					break;
				}else {
					isclicked = 0;
					canvas.setCursor(Cursor.getDefaultCursor());
				}	
			}
		}
				
	}
	
}
