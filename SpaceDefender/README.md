# SPACE DEFENDER — Proyecto Greenfoot

## Cómo abrir el proyecto

1. Abre **Greenfoot** (versión 3.x o superior).
2. Ve a **Scenario → Open…** y selecciona esta carpeta (`SpaceDefender/`).
3. Haz clic en **Compile All**.
4. Presiona el botón **▶ Run** (o usa el botón Play de Greenfoot).
5. En la pantalla del juego, presiona **ENTER** para comenzar.

## Assets requeridos

Coloca estos archivos en la carpeta del proyecto:

### Imágenes (carpeta raíz del proyecto)
| Archivo           | Descripción                              |
|-------------------|------------------------------------------|
| `space_bg.png`    | Fondo estrellado 800×600 px              |
| `player_ship.png` | Nave del jugador (transparencia PNG)     |
| `enemy_ship.png`  | Nave enemiga (transparencia PNG)         |

> **Nota:** Puedes usar cualquier imagen libre de derechos o generarla con IA.
> Si los archivos no existen, Greenfoot muestra un rectángulo de color por defecto.

### Sonidos (carpeta `sounds/`)
| Archivo            | Descripción         |
|--------------------|---------------------|
| `laser.wav`        | Disparo del jugador |
| `enemy_laser.wav`  | Disparo del enemigo |
| `explosion.wav`    | Explosión           |

> Los sonidos se pueden omitir; Greenfoot simplemente no los reproducirá.

## Controles

| Tecla           | Acción          |
|-----------------|-----------------|
| ← → ↑ ↓ / WASD | Mover nave      |
| ESPACIO         | Disparar        |
| ENTER           | Iniciar juego   |
| R               | Reiniciar       |

## Estructura de clases (POO)

```
Actor  (Greenfoot)
├── PlayerShip      — Nave del jugador, movimiento y disparo
├── EnemyShip       — Enemigo con IA simple, disparo aleatorio
├── PlayerBullet    — Proyectil del jugador
├── EnemyBullet     — Proyectil del enemigo (escala con oleada)
├── Asteroid        — Obstáculo dinámico con rotación
├── Explosion       — Animación de partículas
└── Label           — HUD de puntuación / vidas / oleada

World  (Greenfoot)
└── SpaceWorld      — Mundo principal, lógica de oleadas y puntaje
```

## Mecánicas principales

- **5 oleadas** con dificultad progresiva.
- **Asteroides** que caen aleatoriamente y dañan al jugador y a los enemigos.
- **Puntaje** por destruir enemigos (×100 por oleada) + bonus al completar oleada (+500).
- **Sistema de vidas** con inmunidad temporal tras recibir daño.
- **Condición de victoria**: destruir todos los enemigos de las 5 oleadas.
- **Game Over**: quedarse sin vidas.
