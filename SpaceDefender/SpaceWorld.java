import greenfoot.*;

/**
 * SPACE DEFENDER — Mundo principal
 */
public class SpaceWorld extends World {

    public static final int WORLD_WIDTH  = 800;
    public static final int WORLD_HEIGHT = 600;

    public enum ShipType { NORMAL, TRIPLE }

    private int  score       = 0;
    private int  lives       = 3;
    private int  wave        = 0;
    private int  enemiesLeft = 0;
    private int  frameCount  = 0;

    private boolean gameOver      = false;
    private boolean gameActive    = false;
    private boolean selectingShip = true;
    private boolean showingInfo   = false;

    private ShipType chosenShip = ShipType.NORMAL;

    private static final String[] BACKGROUNDS = {
        "space_bg.png",
        "space_bg2.png",
        "space_bg3.png",
        "space_bg4.png"
    };
    private int currentBgIndex = -1;
    private boolean shownAdvancedAlert = false;

    private Label scoreLabel;
    private Label livesLabel;
    private Label waveLabel;
    private Label centerLabel;
    private Label shipALabel;
    private Label shipBLabel;
    private Label selectorLabel;

    public SpaceWorld() {
        super(WORLD_WIDTH, WORLD_HEIGHT, 1, false);
        setBackground("space_bg.png");
        setupUI();
        showShipSelect();
    }

    @Override
    public void act() {
        frameCount++;

        if (selectingShip) {
            handleShipSelection();
            return;
        }

        if (showingInfo) {
            if (Greenfoot.isKeyDown("enter")) {
                showingInfo = false;
                centerLabel.setValue("");
                startGame();
            }
            return;
        }

        if (!gameActive) return;

        if (gameOver) {
            if (Greenfoot.isKeyDown("r")) Greenfoot.setWorld(new SpaceWorld());
            return;
        }

        checkWaveComplete();
        maybeSpawnAsteroid();
        updateHUD();
    }

    // ── Seleccion de nave ────────────────────────────────────────────────
    private void showShipSelect() {
        centerLabel.setValue(
            "SPACE DEFENDER\n" +
            "\n" +
            "Elige tu nave:"
        );
        shipALabel.setValue("[1]  AGUILA-1");
        shipBLabel.setValue("[2]  HIDRA-X");
        selectorLabel.setValue("");
    }

    private void handleShipSelection() {
        if (Greenfoot.isKeyDown("1")) {
            chosenShip = ShipType.NORMAL;
            selectingShip = false;
            clearSelectScreen();
            showShipInfoCard();
        }
        if (Greenfoot.isKeyDown("2")) {
            chosenShip = ShipType.TRIPLE;
            selectingShip = false;
            clearSelectScreen();
            showShipInfoCard();
        }
    }

    private void clearSelectScreen() {
        shipALabel.setValue("");
        shipBLabel.setValue("");
        selectorLabel.setValue("");
    }

    private String shipName() {
        return chosenShip == ShipType.NORMAL ? "AGUILA-1" : "HIDRA-X";
    }

    /**
     * Muestra la ficha de nave como tarjeta destacada con Greenfoot.delay,
     * igual que la alerta de aliens. Luego el jugador presiona ENTER.
     */
    private void showShipInfoCard() {
        // Muestra ficha sin ENTER todavia
        String card = buildCard();
        centerLabel.setValue(card);
        // Pausa larga para que el jugador lea la ficha completa
        Greenfoot.delay(200);
        // Luego aparece el prompt de ENTER
        centerLabel.setValue(card + "\n\nPresiona ENTER para despegar");
        showingInfo = true;
    }

    private String buildCard() {
        if (chosenShip == ShipType.NORMAL) {
            return
                ">>> NAVE SELECCIONADA <<<\n" +
                "\n" +
                "AGUILA-1\n" +
                "Caza estandar de la flota galactica.\n" +
                "Rapida y maniobrable. Ideal para\n" +
                "esquivar y atacar con precision.\n" +
                "\n" +
                "  Vida:      3 corazones\n" +
                "  Disparo:   1 dardo — veloz y preciso\n" +
                "  Velocidad: Alta";
        } else {
            return
                ">>> NAVE SELECCIONADA <<<\n" +
                "\n" +
                "HIDRA-X\n" +
                "Prototipo experimental de combate.\n" +
                "Su canon triple cubre mas area pero\n" +
                "el peso extra la hace mas lenta.\n" +
                "\n" +
                "  Vida:      2 corazones\n" +
                "  Disparo:   3 dardos en abanico\n" +
                "  Velocidad: Baja";
        }
    }

    // ── UI ───────────────────────────────────────────────────────────────
    private void setupUI() {
        scoreLabel = new Label("Score: 0", 24);
        addObject(scoreLabel, 80, 20);

        livesLabel = new Label("Vidas: " + lives, 24);
        addObject(livesLabel, WORLD_WIDTH - 80, 20);

        waveLabel = new Label("Oleada: -", 24);
        addObject(waveLabel, WORLD_WIDTH / 2, 20);

        centerLabel = new Label("", 24);
        addObject(centerLabel, WORLD_WIDTH / 2, WORLD_HEIGHT / 2 - 60);

        shipALabel = new Label("", 20);
        addObject(shipALabel, WORLD_WIDTH / 2, WORLD_HEIGHT / 2 + 40);

        shipBLabel = new Label("", 20);
        addObject(shipBLabel, WORLD_WIDTH / 2, WORLD_HEIGHT / 2 + 130);

        selectorLabel = new Label("", 20);
        addObject(selectorLabel, WORLD_WIDTH / 2, WORLD_HEIGHT / 2 + 210);
    }

    private void updateHUD() {
        scoreLabel.setValue("Score: " + score);
        livesLabel.setValue("Vidas: " + lives);
        waveLabel.setValue("Oleada: " + wave);
    }

    // ── Inicio ───────────────────────────────────────────────────────────
    public void startGame() {
        gameActive = true;
        if (chosenShip == ShipType.TRIPLE) lives = 2;
        updateHUD();
        addObject(new PlayerShip(this, chosenShip), WORLD_WIDTH / 2, WORLD_HEIGHT - 80);
        nextWave();
    }

    // ── Fondos por oleada ─────────────────────────────────────────────────
    private void applyBackground(int waveNumber) {
        int idx = Math.min(waveNumber - 1, BACKGROUNDS.length - 1);
        if (idx != currentBgIndex) {
            currentBgIndex = idx;
            setBackground(BACKGROUNDS[idx]);
        }
    }

    // ── Sistema de oleadas ────────────────────────────────────────────────
    private void nextWave() {
        wave++;
        applyBackground(wave);

        int enemyHp = 1 + (wave / 4);

        if (enemyHp >= 2 && !shownAdvancedAlert) {
            shownAdvancedAlert = true;
            centerLabel.setValue(
                "!ALERTA DE INTELIGENCIA!\n" +
                "\n" +
                "Los aliens de esta oleada son\n" +
                "de una raza mas avanzada.\n" +
                "Tienen blindaje reforzado y\n" +
                "necesitan varios impactos para caer.\n" +
                "\n" +
                "  Naranja = 2 vidas\n" +
                "  Rojo    = 3 o mas vidas\n" +
                "\n" +
                "Preparate..."
            );
            Greenfoot.delay(130);
            centerLabel.setValue("");
        }

        int rows = Math.min(1 + wave / 2, 5);
        int cols = Math.min(3 + wave,    10);
        int hGap   = (WORLD_WIDTH - 80) / cols;
        int startY = 70;

        enemiesLeft = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int x = 50 + c * hGap;
                int y = startY + r * 55;
                addObject(new EnemyShip(wave), x, y);
                enemiesLeft++;
            }
        }
    }

    private void checkWaveComplete() {
        if (enemiesLeft <= 0) {
            int bonus = 300 + wave * 100;
            addScore(bonus);

            centerLabel.setValue(
                "Oleada " + wave + " completada!\n" +
                "+" + bonus + " puntos\n" +
                "\n" +
                "Siguiente oleada..."
            );
            Greenfoot.delay(70);
            centerLabel.setValue("");

            nextWave();
        }
    }

    // ── Asteroides ────────────────────────────────────────────────────────
    private void maybeSpawnAsteroid() {
        int interval = Math.max(110, 350 - wave * 22);
        if (frameCount % interval == 0) {
            int x = Greenfoot.getRandomNumber(WORLD_WIDTH - 60) + 30;
            addObject(new Asteroid(), x, -20);
        }
        if (wave >= 5) {
            int interval2 = Math.max(150, 500 - wave * 20);
            if (frameCount % interval2 == 13) {
                int x = Greenfoot.getRandomNumber(WORLD_WIDTH - 60) + 30;
                addObject(new Asteroid(), x, -20);
            }
        }
    }

    // ── Eventos de juego ──────────────────────────────────────────────────
    public void enemyDestroyed() {
        enemiesLeft = Math.max(0, enemiesLeft - 1);
        addScore(80 + wave * 20);
    }

    public void addScore(int pts) { score += pts; }

    public void loseLife() {
        lives--;
        if (lives <= 0) triggerGameOver();
    }

    private void triggerGameOver() {
        gameOver   = true;
        gameActive = false;
        removeObjects(getObjects(PlayerShip.class));
        centerLabel.setValue(
            "GAME OVER\n" +
            "Llegaste a la oleada " + wave + "\n" +
            "Score: " + score + "\n" +
            "\n" +
            "R para reiniciar"
        );
    }

    public int      getWave()     { return wave;      }
    public int      getLives()    { return lives;     }
    public int      getScore()    { return score;     }
    public boolean  isActive()    { return gameActive; }
    public ShipType getShipType() { return chosenShip; }
}
