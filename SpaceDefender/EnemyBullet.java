import greenfoot.*;

/** Proyectil enemigo — baja hacia el jugador. */
public class EnemyBullet extends Actor {

    private int speed;

    public EnemyBullet(int wave) {
        this.speed = 4 + wave / 2;   // oleada 1→4, 3→5, 5→6…
        GreenfootImage img = new GreenfootImage(6, 18);
        img.setColor(new Color(255, 80, 80));
        img.fillRect(0, 0, 6, 18);
        setImage(img);
    }

    @Override
    public void act() {
        if (getWorld() == null) return;
        setLocation(getX(), getY() + speed);
        if (getY() > SpaceWorld.WORLD_HEIGHT + 10) {
            getWorld().removeObject(this);
        }
    }
}
