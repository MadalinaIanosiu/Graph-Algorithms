import java.awt.Color;
import java.awt.Graphics;

public class Node
{
	private int coordX;
	private int coordY;
	private int number;
	
	public Node(int coordX, int coordY, int number)
	{
		this.coordX = coordX;
		this.coordY = coordY;
		this.number = number;
	}
	
	public int getCoordX() {
		return coordX;
	}
	public void setCoordX(int coordX) {
		this.coordX = coordX;
	}
	public int getCoordY() {
		return coordY;
	}
	public void setCoordY(int coordY) {
		this.coordY = coordY;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}

	public void drawNode(Graphics g, int node_diam)
	{
		g.setColor(Color.BLACK);
        g.fillOval(coordX, coordY, node_diam, node_diam);
        g.setColor(Color.WHITE);
        g.drawOval(coordX, coordY, node_diam, node_diam);

		if(number < 10)
			g.drawString(((Integer)number).toString(), coordX+7, coordY+10);
		else
			g.drawString(((Integer)number).toString(), coordX+10, coordY+10);
	}
}
