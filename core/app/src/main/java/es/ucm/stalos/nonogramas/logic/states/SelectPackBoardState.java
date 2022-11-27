package es.ucm.stalos.nonogramas.logic.states;

import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import es.ucm.stalos.androidengine.State;
import es.ucm.stalos.androidengine.Engine;
import es.ucm.stalos.androidengine.Font;
import es.ucm.stalos.androidengine.Image;
import es.ucm.stalos.androidengine.TouchEvent;
import es.ucm.stalos.nonogramas.logic.data.DataSystem;
import es.ucm.stalos.nonogramas.logic.Assets;
import es.ucm.stalos.nonogramas.logic.data.PackageData;
import es.ucm.stalos.nonogramas.logic.enums.GridType;
import es.ucm.stalos.nonogramas.logic.interfaces.ButtonCallback;
import es.ucm.stalos.nonogramas.logic.objects.SelectPackageButton;

//TODO: Hacer toda la lógica de los paquetes

// LÓGICA: Crear una interfaz que nos muestre las categorías y niveles disponibles, diferenciando
// entre bloqueados (no pueden jugarse) y desbloqueados (podemos volver a acceder a ellos en
// cualquier momento).

// LÓGICA CON DATOS GUARDADOS: Consiste en un conjunto de categorías y niveles en los que
// únicamente podremos avanzar al siguiente si hemos completado el nivel actual. Los niveles y
// categorías en este modo son fijos. Para poder tener este modo tenemos que guardar cuantos niveles
// ha desbloqueado el jugador hasta el momento

// GRAFICOS: En estos niveles del modo historia, deben representarse imágenes pixeladas (una
//abeja, una casa, un árbol, un faro, etc.)

// CATEGORÍAS POR DIFICULTAD: ada categoría tendrá X niveles de una misma dificultad / tamaño, y
// según avance el jugador completando los niveles de la categoría actual se desbloquearan nuevas
// categorías con mayor dificultad / tamaño de los tableros.
// EJEMPLO: Categoría fácil, 20 con tableros de 5x5. Al completar los 20 niveles se desbloquea la
// categoría intermedia con 20 niveles de 10x10.



/**
 * State where the user select the package to play into the Story mode
 */
public class SelectPackBoardState extends AbstractSelectBoard {

    public SelectPackBoardState(Engine engine) {
        super(engine);
    }

//-----------------------------------------OVERRIDE-----------------------------------------------//

    @Override
    public boolean init() {
        try {
            super.init();

            // BUTTONS
            initSelectButtons();

            _modeText = "Modo Historia";
            _commentText = "Selecciona el paquete";

        } catch (Exception e) {
            System.out.println("Error SelectPackBoardState");
            System.out.println(e);
            return false;
        }

        return true;
    }

//-------------------------------------------MISC-------------------------------------------------//

    /**
     * Initialize the buttons to select the levels
     *
     * @throws Exception in case of font creation fails
     */
    private void initSelectButtons() throws Exception {
        _selectButtons = new ArrayList<>();

        float min = Math.min((_graphics.getLogWidth() * 0.2f), (_graphics.getLogHeight() * 0.2f));
        float[] size = new float[]{min, min};

        Font font = _graphics.newFont("Molle-Regular.ttf", 20, true);

        int[] pos = new int[2];

        initGridTypesMap();

        int j = 0;
        for (int i = 0; i < GridType.MAX.getValue(); i++) {
            pos[0] = (int)(_graphics.getLogWidth() * 0.1f) * (1 + (3 * j));
            pos[1] = (int)(_graphics.getLogHeight() * 0.143f) * (3 + (i / 3) * 2);

            // Comprueba si el paquete esta desbloqueado
            boolean unlocked = (i <= DataSystem._historyData._currentPackage);

            GridType grid = _gridTypes.get(i);

            final SelectPackageButton _level = new SelectPackageButton(pos, size, grid, font, unlocked);
            _level.setCallback(new ButtonCallback() {
                @Override
                public void doSomething() {
                    // Seleccion de los datos del paquete escogido
//                    PackageData data = DataSystem._packageDataList.get(grid.getValue());
                    State selectLevel = new SelectLevelState(_engine, grid);//, data);
                    _engine.reqNewState(selectLevel);
                    _audio.playSound(Assets.clickSound, 0);
                    _audio.stopMusic();
                }
            });
            _selectButtons.add(_level);
            j++;
            if (j == 3) j = 0;
        }
    }


//----------------------------------------ATTRIBUTES----------------------------------------------//
    /**
     * Choosen grid type
     */
//    GridType _choosenGrid;
}
