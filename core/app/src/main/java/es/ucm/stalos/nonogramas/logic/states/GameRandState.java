package es.ucm.stalos.nonogramas.logic.states;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import es.ucm.stalos.androidengine.TouchEvent;
import es.ucm.stalos.androidengine.State;
import es.ucm.stalos.androidengine.Engine;
import es.ucm.stalos.androidengine.Font;
import es.ucm.stalos.androidengine.Image;
import es.ucm.stalos.nonogramas.logic.Assets;
import es.ucm.stalos.nonogramas.logic.enums.PlayingState;
import es.ucm.stalos.nonogramas.logic.interfaces.ButtonCallback;
import es.ucm.stalos.nonogramas.logic.objects.Board;


// TODO: REWARDED VIDEO, SISTEMA DE VIDAS, PALETA DE COLORES

// REWARDED VIDEO: Los vídeos recompensados serán opcionales para el jugador, dándole ventajas sobre el
//juego si deciden verlos. Para más información sobre cómo recompensamos al jugador leer
//la subsección siguiente, Retención y recompensas


// SISTEMA DE VIDAS: Implementar la lógica del sistema de vidas para que no pueda colocar nuevas
// casillas hasta tener vidas de nuevo. Añadir apartado gráfico para las vidas (corazones o lo que sea)

// PALETA DE COLORES: crearemos un conjunto de características que les permite customizar la vista
// del juego y la paleta de colores del tablero.

// ENUNCIADO: Por ello crearemos un sistema de recompensas que permite conseguir las paletas de
//colores y a su vez que nos de la opción a ganar también vidas (?)

// PRACTICA 2: Refactorización de los GameState
public class GameRandState extends AbstractGameState {

    public GameRandState(Engine engine, int rows, int columns, boolean isRandom) {
        super(engine, rows, columns, true);
    }

//-----------------------------------------OVERRIDE-----------------------------------------------//

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);

    }

    @Override
    public void render() {
        super.render();
    }

//-------------------------------------------MISC-------------------------------------------------//

    @Override
    protected void initButtons() throws Exception {
        super.initButtons();

        _giveupCallback = new ButtonCallback() {
            @Override
            public void doSomething() {
                State selectLevel;
                // Story mode
                // TODO: En lugar de volver a StoryPackage hay que volver a PackageLevel
                //  para ello habría que guardar el paquete que se está jugando actualmente
                //  porque el SelectPackageLevel pedirá como parámetro el paquete a cargar
                if (!_isRandom)
                    selectLevel = new SelectPackageState(_engine);
                    // Random mode
                else
                    selectLevel = new SelectRandLevelState(_engine);
                _engine.reqNewState(selectLevel);
                _audio.stopMusic();
                _audio.playSound(Assets.clickSound, 0);
            }
        };

        _backCallback = new ButtonCallback() {
            @Override
            public void doSomething() {
                State selectLevel;
                // Story mode
                // TODO: En lugar de volver a StoryPackage hay que volver a PackageLevel
                //  para ello habría que guardar el paquete que se está jugando actualmente
                //  porque el SelectPackageLevel pedirá como parámetro el paquete a cargar
                if (!_isRandom)
                    selectLevel = new SelectPackageState(_engine);
                    // Random mode
                else
                    selectLevel = new SelectRandLevelState(_engine);

                _engine.reqNewState(selectLevel);
                _audio.stopMusic();
                _audio.playSound(Assets.clickSound, 0);
            }
        };
    }

}
