import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.HashSet;

public abstract class Shape implements Serializable{
	private static final long serialVersionUID = 1L;
	int x1, x2, y1, y2;
	int _x1, _x2, _y1, _y2;
	Color color;
	double stroke;
	boolean flag;
	private HashSet <Point> points = new HashSet<Point>(); // 用一组points
	
	public abstract void draw(Graphics2D g);
	public abstract void moveShape(int x, int y);
	public abstract void resizeShape(int x, int y);
	public void getClick(boolean flag) {
		this.flag = flag;
	}
	
	public void updateShape() {
		this._x1 = this.x1;
		this._x2 = this.x2;
		this._y1 = this.y1;
		this._y2 = this.y2;
		points.clear();
	}
	public void addPoint(Point p)
	{
		points.add(p);
	}
	public boolean isContains(int x, int y) {
		return points.contains(new Point(x,y));
	}
}

class Line extends Shape{
	public Line(int x1, Integer y1, int x2, int y2, Color color, double stroke) {
		// TODO Auto-generated constructor stub
		this.x1 = x1;this._x1 = x1;
		this.x2 = x2;this._x2 = x2;
		this.y1 = y1;this._y1 = y1;
		this.y2 = y2;this._y2 = y2;
		this.color = color;
		this.stroke = stroke;
	}
	
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.setStroke(new BasicStroke((float)stroke));
		g.drawLine(x1, y1, x2, y2);
		initPoints();
	}

	private void initPoints(){
		float k = (this.y2-this.y1)/(float)(this.x2-this.x1);
		float k2 = (this.x2-this.x1)/(float)(this.y2-this.y1);
		int s = this.stroke>5?(int)(this.stroke/2):2;
		s = s<20?s:20;
		for (int j = -s;j<=s;j++)
		{
			if (k<=1&&k>=-1)
			{
				for (int i = Math.min(this.x1, this.x2)+j;
						i<=Math.max(this.x1, this.x2)+j;i++)
				{
					int x = i;
					int y = (int)(k*(x-(this.x1+j))+this.y1);
					addPoint(new Point(x,y));
				}
				for (int i = Math.min(this.x1, this.x2);
						i<=Math.max(this.x1, this.x2);i++)
				{
					int x = i;
					int y = (int)(k*(x-this.x1)+this.y1+j);
					addPoint(new Point(x,y));
				}
			}
			else if(k2<=1&&k2>=-1)
			{
				for (int i = Math.min(this.y1, this.y2)+j;
						i<=Math.max(this.y1, this.y2)+j;i++)
				{
					int y = i;
					int x = (int)(k2*(y-(this.y1+j))+this.x1);
					addPoint(new Point(x,y));
				}
				for (int i = Math.min(this.y1, this.y2);
						i<=Math.max(this.y1, this.y2);i++)
				{
					int y = i;
					int x = (int)(k2*(y-this.y1)+this.x1+j);
					addPoint(new Point(x,y));
				}
			}
			else 
			{
				for(int i = -s;i<=s;i++)
					addPoint(new Point(this.x1+i,this.y1+j));
			}
		}
	}
	public void moveShape(int x, int y) {
		this.x1 = this._x1 + x;
		this.x2 = this._x2 + x;
		this.y1 = this._y1 + y;
		this.y2 = this._y2 + y;
	}
	public void resizeShape(int x, int y) {
		this.x1 = this._x1 - x;
		this.x2 = this._x2 + x;
		this.y1 = this._y1 - y;
		this.y2 = this._y2 + y;
	}
}

class Oval extends Shape{
	public Oval(int x1, Integer y1, int x2, int y2, Color color, double stroke) {
		// TODO Auto-generated constructor stub
		this.x1 = x1;this._x1 = this.x1;
		this.x2 = x2;this._x2 = this.x2;
		this.y1 = y1;this._y1 = this.y1;
		this.y2 = y2;this._y2 = this.y2;
		this.color = color;
		this.stroke = stroke;
	}
	
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.setStroke(new BasicStroke((float)stroke));
		g.drawOval(x1, y1, x2, y2);
		initPoint();
	}
	
	private void initPoint(){
		for (int i =0;i<360;i++)
		{
			int s = this.stroke>5?(int)(this.stroke/2):2;
			s = s<20?s:20;
			for(int j =-s;j<=s;j++)
			{
				for(int k =-s;k<=s;k++)
				{
					double a = (double)Math.abs(this.x2)/2;
					double b = (double)Math.abs(this.y2)/2;
					int x = (int)((double)Math.min(this.x1,this.x1 + this.x2)+a+j
							+a*Math.cos((double)i/360*Math.PI*2));
					int y = (int)((double)Math.min(this.y1,this.y1 + this.y2)+b+k
							+b*Math.sin((double)i/360*Math.PI*2));
					addPoint(new Point(x,y));
				}
			}
		}
	}
	public void moveShape(int x, int y) {
		this.x1 = this._x1 + x;
		this.y1 = this._y1 + y;
	}
	public void resizeShape(int x, int y) {
		this.x1 = this._x1 - x;
		this.y1 = this._y1 - y;
		this.x2 = this._x2 + 2*x;
		this.y2 = this._y2 + 2*y;
	}

}

class Rectangle extends Shape{
	private static final long serialVersionUID = 1L;

	public Rectangle(int x1, int y1, int x2, int y2, Color color, double stroke) {
		// TODO Auto-generated constructor stub
		this.x1 = x1;this._x1 = this.x1;
		this.x2 = x2;this._x2 = this.x2;
		this.y1 = y1;this._y1 = this.y1;
		this.y2 = y2;this._y2 = this.y2;
		this.color = color;
		this.stroke = stroke;
	}
	
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.setStroke(new BasicStroke((float)stroke));
		g.drawRect(x1, y1, x2, y2);
		initPoint();
	}

	private void initPoint(){
		int s = this.stroke>5?(int)(this.stroke/2):2;
		s = s<20?s:20;
		for (int k = -s;k<=s;k++)
		{
			for(int l = -s;l<=s;l++)
			{
				for (int i =Math.min(this.x1,this.x1 + this.x2);
						i<=Math.max(this.x1,this.x1 + this.x2);i++)
				{
					addPoint(new Point(i+k,this.y1+l));
					addPoint(new Point(i+k,this.y1 + this.y2+l));
				}
				for (int i =Math.min(this.y1,this.y1 + this.y2);
						i<=Math.max(this.y1,this.y1 + this.y2);i++)
				{
					addPoint(new Point(this.x1+k,i+l));
					addPoint(new Point(this.x1 + this.x2+k,i+l));
				}
			}
		}
	}
	public void moveShape(int x, int y) {
		this.x1 = this._x1 + x;
		this.y1 = this._y1 + y;
	}
	public void resizeShape(int x, int y) {
		this.x1 = this._x1 - x;
		this.y1 = this._y1 - y;
		this.x2 = this._x2 + 2*x;
		this.y2 = this._y2 + 2*y;
	}
}
class Text extends Shape{
	private static final long serialVersionUID = 1L;
	private double _stroke;
	private String str;
	public Text(int x1, int y1, Color color, double stroke, String str) {
		this.x1 = x1;this._x1 = x1;
		this.y1 = y1;this._y2 = y2;
		this.color = color;
		this.stroke = stroke;this._stroke=stroke;
		this.str = str;
	}
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.setStroke(new BasicStroke((float)stroke));
		g.setFont(new Font("宋体",Font.BOLD,(int)stroke * 10));
		g.drawString(str, x1, y1);
		initPoint(g);
	}
	private void initPoint(Graphics2D g){
		double fontheight = g.getFont().getStringBounds(str, g.getFontRenderContext()).getHeight();
		double fontwidth = g.getFont().getStringBounds(str, g.getFontRenderContext()).getWidth();
		for (int i = (int)(this.x1);
				i<=this.x1+fontwidth;i++)
			for (int j = (int) (this.y1-fontheight);
					j<=this.y1;j++)
				addPoint(new Point(i,j));
	}
	public void moveShape(int x, int y) {
		this.x1 = this._x1 + x;
		this.y1 = this._y1 + y;
	}
	public void resizeShape(int x, int y) {
		this.stroke = this._stroke + 0.1*Math.abs(x);
	}
}
