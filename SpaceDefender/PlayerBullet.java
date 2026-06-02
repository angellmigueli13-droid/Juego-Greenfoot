import greenfoot.*;

/**
 * Proyectil del jugador.
 * driftX = 0  → bala central (recta)
 * driftX = ±N → balas laterales de la nave Triple (diagonal leve)
 */
public class PlayerBullet extends Actor {

    private static final int SPEED = 12;
    private int driftX;

    public PlayerBullet(int driftX) {
        this.driftX = driftX;
        GreenfootImage img = new GreenfootImage(6, 18);
        // Balas laterales en color ligeramente diferente para distinguirlas
        if (driftX == 0) {
            img.setColor(new Color(0, 220, 255));
        } else {
            img.setColor(new Color(80, 180, 255));
        }
        img.fillRect(0, 0, 6, 18);
        setImage(img);
    }

    @Override
    public void act() {
        if (getWorld() == null) return;
        setLocation(getX() + driftX, getY() - SPEED);
        if (getY() < 0 || getX() < 0 || getX() > SpaceWorld.WORLD_WIDTH) {
            getWorld().removeObject(this);
        }
    }
}
