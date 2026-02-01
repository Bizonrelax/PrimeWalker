import javax.swing.*;
import java.awt.*;

public class ImageDisplay extends JPanel {
    private Image image;
    
    public void setImage(Image image) {
        this.image = image;
        if (image != null) {
            setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
        }
        revalidate();
        repaint();
    }
    
    public Image getImage() {
        return image;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }
    }
}