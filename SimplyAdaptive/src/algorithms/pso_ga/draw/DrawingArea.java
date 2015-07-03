package algorithms.pso_ga.draw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * A drawing area
 * 
 * @author pcingola@users.sourceforge.net
 */
public class DrawingArea extends JComponent {

	protected SwarmShow2D controller;
	protected Dimension preferredSize;
	Image image=null; 
	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------

	public DrawingArea(SwarmShow2D controller) {
		this.controller = controller;
		setBackground(Color.WHITE);
		setOpaque(true);
	}

	//-------------------------------------------------------------------------
	// Methods
	//-------------------------------------------------------------------------

	/** Clear screen */
	public void clear() {
		if(this.image != null)
			this.getGraphics().drawImage(this.image, 0, 0,this.getWidth(),this.getHeight(), this);
		//paintComponent(this.getGraphics());
	}

	/** Get dimention */
	public Dimension getPreferredSize() {
		return controller.getPreferredSize();
	}

	/** Paint */
	protected void paintComponent(Graphics g) {
		// Paint background if we're opaque.
		if( isOpaque() ) {
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
		}
	}


	/** Show swarm's points */
	protected void showSwarm() {
		controller.getSwarm().show(getGraphics(), getForeground(), getWidth(), getHeight(), controller.getShowDimention0(), controller.getShowDimention1(), controller.isShowVelocity());
	}

	public Image getImage() {
		return image;
	}

	public void setImage(String fileName) {
		 File f = new File(fileName);
		 if( f.exists()){
			try {
				this.image = ImageIO.read(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		 }
	}

}
