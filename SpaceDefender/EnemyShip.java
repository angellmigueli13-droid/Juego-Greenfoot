import greenfoot.*;

/**
 * NAVE ENEMIGA
 * Movimiento lateral + descenso al rebotar.
 * Dispara con frecuencia que escala por oleada.
 */
public class EnemyShip extends Actor {

    private static final int DROP_DISTANCE   = 22;
    private static final int BASE_SHOOT_RATE = 90;

    private int  speedX;
    private int  wave;
    private int  shootCountdown;
    private int  health;

    public EnemyShip(int wave) {
        this.wave   = wave;
        this.speedX = 1 + wave / 2;
        this.health = 1 + (wave / 4);
        this.shootCountdown = randomShootTimer();
        applyImage();
    }

    /**
     * Aplica color según la vida actual:
     *   1 HP  → imagen normal (verde)
     *   2 HP  → tinte naranja
     *   3+ HP → tinte rojo intenso
     */
    private void applyImage() {
        GreenfootImage img = new GreenfootImage("enemy_ship.png");
        img.scale(45, 45);

        if (health == 2) {
            // Tinte naranja encima
            GreenfootImage overlay = new GreenfootImage(45, 45);
            overlay.setColor(new Color(255, 140, 0, 130));
            overlay.fillRect(0, 0, 45, 45);
            img.drawImage(overlay, 0, 0);
        } else if (health >= 3) {
            // Tinte rojo intenso
            GreenfootImage overlay = new GreenfootImage(45, 45);
            overlay.setColor(new Color(220, 0, 0, 160));
            overlay.fillRect(0, 0, 45, 45);
            img.drawImage(overlay, 0, 0);
        }
        setImage(img);
    }

    @Override
    public void act() {
        if (getWorld() == null) return;
        move();
        if (getWorld() == null) return;
        handleShooting();
        if (getWorld() == null) return;
        checkBulletHit();
    }

    private void move() {
        int x = getX() + speedX;
        int y = getY();

        if (x <= 22 || x >= SpaceWorld.WORLD_WIDTH - 22) {
            speedX = -speedX;
            y += DROP_DISTANCE;
        }
        setLocation(x, y);

        // Llegó al fondo → daño al jugador
        if (y >= SpaceWorld.WORLD_HEIGHT - 50) {
            ((SpaceWorld) getWorld()).loseLife();
            ((SpaceWorld) getWorld()).enemyDestroyed();
            getWorld().removeObject(this);
        }
    }

    private void handleShooting() {
        shootCountdown--;
        if (shootCountdown <= 0) {
            if (!getWorld().getObjects(PlayerShip.class).isEmpty()) {
                EnemyBullet bullet = new EnemyBullet(wave);
                getWorld().addObject(bullet, getX(), getY() + 25);
                Greenfoot.playSound("enemy_laser.wav");
            }
            shootCountdown = randomShootTimer();
        }
    }

    private void checkBulletHit() {
        PlayerBullet bullet = (PlayerBullet) getOneIntersectingObject(PlayerBullet.class);
        if (bullet != null) {
            if (bullet.getWorld() != null) getWorld().removeObject(bullet);
            health--;
            if (health <= 0) {
                destroy();
            } else {
                // Parpadeo de daño y luego actualizar color según HP restante
                getImage().setTransparency(80);
                Greenfoot.delay(4);
                if (getWorld() != null) applyImage();
            }
        }
    }

    private void destroy() {
        Greenfoot.playSound("explosion.wav");
        Explosion exp = new Explosion();
        getWorld().addObject(exp, getX(), getY());
        ((SpaceWorld) getWorld()).enemyDestroyed();
        getWorld().removeObject(this);
    }

    private int randomShootTimer() {
        int minRate = Math.max(18, BASE_SHOOT_RATE - wave * 7);
        return minRate + Greenfoot.getRandomNumber(50);
    }
}
