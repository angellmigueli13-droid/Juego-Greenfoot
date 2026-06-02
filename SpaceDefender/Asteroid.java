import greenfoot.*;

/**
 * ASTEROIDE — cae en diagonal, rota visualmente.
 * SIEMPRE verifica getWorld() != null antes de cualquier operación
 * para evitar ActorRemovedFromWorld.
 */
public class Asteroid extends Actor {

    private int speedX;
    private int speedY;

    public Asteroid() {
        speedX = Greenfoot.getRandomNumber(5) - 2;
        speedY = 3 + Greenfoot.getRandomNumber(3);

        GreenfootImage img = new GreenfootImage(40, 40);
        img.setColor(new Color(140, 120, 100));
        img.fillOval(2, 5, 36, 30);
        img.setColor(new Color(100, 85, 70));
        img.drawOval(2, 5, 36, 30);
        img.setColor(new Color(160, 140, 120));
        img.fillOval(8, 10, 12, 10);
        setImage(img);
    }

    @Override
    public void act() {
        // Guard: si ya no estamos en el mundo, no hacer nada
        if (getWorld() == null) return;

        setLocation(getX() + speedX, getY() + speedY);
        getImage().rotate(2);

        // Salir por abajo
        if (getY() > SpaceWorld.WORLD_HEIGHT + 30) {
            getWorld().removeObject(this);
            return;   // ← return inmediato tras eliminar
        }

        // Colisión con enemigo
        EnemyShip enemy = (EnemyShip) getOneIntersectingObject(EnemyShip.class);
        if (enemy != null) {
            if (enemy.getWorld() != null) {
                ((SpaceWorld) getWorld()).enemyDestroyed();
                getWorld().removeObject(enemy);
            }
            getWorld().removeObject(this);
            return;   // ← return inmediato tras eliminar
        }
    }
}
