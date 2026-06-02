import greenfoot.*;

/**
 * NAVE DEL JUGADOR
 *
 * Dos modos según ShipType:
 *   NORMAL → velocidad 5, cooldown 20, dispara 1 bala centrada
 *   TRIPLE → velocidad 3, cooldown 35, dispara 3 balas en abanico
 */
public class PlayerShip extends Actor {

    private SpaceWorld            world;
    private SpaceWorld.ShipType   shipType;

    private int  speed;
    private int  shootCooldown;
    private int  shootTimer    = 0;
    private boolean isInvincible    = false;
    private int     invincibleTimer = 0;

    public PlayerShip(SpaceWorld world, SpaceWorld.ShipType type) {
        this.world    = world;
        this.shipType = type;

        if (type == SpaceWorld.ShipType.TRIPLE) {
            speed         = 3;
            shootCooldown = 35;
        } else {
            speed         = 5;
            shootCooldown = 20;
        }

        GreenfootImage img = new GreenfootImage("player_ship.png");
        img.scale(50, 50);

        // Tinte azul para nave Triple
        if (type == SpaceWorld.ShipType.TRIPLE) {
            GreenfootImage tinted = new GreenfootImage(50, 50);
            tinted.drawImage(img, 0, 0);
            tinted.setColor(new Color(0, 100, 255, 80));
            tinted.fillRect(0, 0, 50, 50);
            setImage(tinted);
        } else {
            setImage(img);
        }
    }

    @Override
    public void act() {
        if (getWorld() == null) return;
        handleMovement();
        handleShooting();
        handleInvincibility();
        checkCollisions();
    }

    private void handleMovement() {
        int x = getX();
        int y = getY();
        if (Greenfoot.isKeyDown("left")  || Greenfoot.isKeyDown("a")) x -= speed;
        if (Greenfoot.isKeyDown("right") || Greenfoot.isKeyDown("d")) x += speed;
        if (Greenfoot.isKeyDown("up")    || Greenfoot.isKeyDown("w")) y -= speed;
        if (Greenfoot.isKeyDown("down")  || Greenfoot.isKeyDown("s")) y += speed;
        x = Math.max(25, Math.min(SpaceWorld.WORLD_WIDTH  - 25, x));
        y = Math.max(25, Math.min(SpaceWorld.WORLD_HEIGHT - 25, y));
        setLocation(x, y);
    }

    private void handleShooting() {
        shootTimer--;
        if (Greenfoot.isKeyDown("space") && shootTimer <= 0) {
            if (shipType == SpaceWorld.ShipType.TRIPLE) {
                shootTriple();
            } else {
                shootSingle();
            }
            Greenfoot.playSound("laser.wav");
            shootTimer = shootCooldown;
        }
    }

    private void shootSingle() {
        PlayerBullet b = new PlayerBullet(0);
        getWorld().addObject(b, getX(), getY() - 30);
    }

    private void shootTriple() {
        // Centro, izquierda, derecha con ligera diagonal
        PlayerBullet center = new PlayerBullet(0);
        PlayerBullet left   = new PlayerBullet(-3);
        PlayerBullet right  = new PlayerBullet(3);
        getWorld().addObject(center, getX(),      getY() - 30);
        getWorld().addObject(left,  getX() - 18,  getY() - 20);
        getWorld().addObject(right, getX() + 18,  getY() - 20);
    }

    private void handleInvincibility() {
        if (isInvincible) {
            invincibleTimer--;
            getImage().setTransparency(invincibleTimer % 10 < 5 ? 80 : 255);
            if (invincibleTimer <= 0) {
                isInvincible = false;
                getImage().setTransparency(255);
            }
        }
    }

    private void checkCollisions() {
        if (isInvincible) return;

        EnemyBullet hit = (EnemyBullet) getOneIntersectingObject(EnemyBullet.class);
        if (hit != null) {
            if (hit.getWorld() != null) getWorld().removeObject(hit);
            takeDamage();
            return;
        }

        EnemyShip enemy = (EnemyShip) getOneIntersectingObject(EnemyShip.class);
        if (enemy != null) {
            if (enemy.getWorld() != null) {
                world.enemyDestroyed();
                getWorld().removeObject(enemy);
            }
            takeDamage();
            return;
        }

        Asteroid ast = (Asteroid) getOneIntersectingObject(Asteroid.class);
        if (ast != null) {
            if (ast.getWorld() != null) getWorld().removeObject(ast);
            takeDamage();
        }
    }

    public void takeDamage() {
        if (getWorld() == null) return;
        world.loseLife();
        Greenfoot.playSound("explosion.wav");
        isInvincible    = true;
        invincibleTimer = 120;
    }
}
