import greenfoot.*;

/**
 * LABEL - Etiqueta de texto para el HUD
 * Actor auxiliar que muestra texto en pantalla con fondo semitransparente.
 */
public class Label extends Actor {

    private String text;
    private int    fontSize;

    public Label(String text, int fontSize) {
        this.text     = text;
        this.fontSize = fontSize;
        updateImage();
    }

    public void setValue(String newText) {
        this.text = newText;
        updateImage();
    }

    public void setValue(int value) {
        setValue(String.valueOf(value));
    }

    private void updateImage() {
        if (text == null || text.isEmpty()) {
            setImage(new GreenfootImage(1, 1));
            return;
        }

        // Medir texto
        GreenfootImage tmp = new GreenfootImage(1, 1);
        tmp.setFont(new Font("Arial", true, false, fontSize));

        // Dimensiones aproximadas
        String[] lines = text.split("\n");
        int lineH = fontSize + 6;
        int w     = 0;
        for (String line : lines) {
            w = Math.max(w, line.length() * (fontSize / 2 + 2));
        }
        w += 20;
        int h = lines.length * lineH + 10;

        GreenfootImage img = new GreenfootImage(w, h);

        // Fondo semitransparente
        img.setColor(new Color(0, 0, 0, 120));
        img.fillRect(0, 0, w, h);

        // Texto blanco
        img.setFont(new Font("Arial", true, false, fontSize));
        img.setColor(Color.WHITE);

        for (int i = 0; i < lines.length; i++) {
            img.drawString(lines[i], 10, (i + 1) * lineH);
        }

        setImage(img);
    }
}
