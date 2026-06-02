import greenfoot.*;

/** Animación de explosión procedural que se auto-elimina. */
public class Explosion extends Actor {

    private int frame = 0;
    private static final int TOTAL = 18;

    public Explosion() { updateImage(); }

    @Override
    public void act() {
        if (getWorld() == null) return;
        frame++;
        if (frame >= TOTAL) {
            getWorld().removeObject(this);
            return;
        }
        updateImage();
    }

    private void updateImage() {
        int size  = 10 + frame * 3;
        int alpha = Math.max(0, 255 - frame * 14);
        GreenfootImage img = new GreenfootImage(size, size);
        img.setColor(new Color(255, 120, 0, alpha));
        img.fillOval(0, 0, size, size);
        int core = size / 3;
        img.setColor(new Color(255, 240, 100, alpha));
        img.fillOval(size/2 - core/2, size/2 - core/2, core, core);
        setImage(img);
    }
}
