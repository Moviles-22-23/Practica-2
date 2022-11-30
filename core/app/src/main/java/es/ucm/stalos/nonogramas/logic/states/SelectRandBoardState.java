package es.ucm.stalos.nonogramas.logic.states;

//public class SelectRandBoardState extends SelectBoard {
//    public SelectRandBoardState(Engine engine) {
//        super(engine, true);
//    }
//
////-----------------------------------------OVERRIDE-----------------------------------------------//
//
//    @Override
//    public boolean init() {
//        try {
//            super.init();
//
//            // BUTTONS
//            initSelectButtons();
//
//            _modeText = "JUEGO ALEATORIO";
//            _commentText = "Selecciona el tamaño del puzzle";
//        } catch (Exception e) {
//            System.out.println("Error SelectRandBoardState");
//            System.out.println(e);
//            return false;
//        }
//
//        return true;
//    }

//-------------------------------------------MISC-------------------------------------------------//

//    /**
//     * Initialize the buttons to select the levels
//     *
//     * @throws Exception in case of font creation fails
//     */
//    private void initSelectButtons() throws Exception {
//        _selectButtons = new ArrayList<>();
//
//        float min = Math.min((_graphics.getLogWidth() * 0.2f), (_graphics.getLogHeight() * 0.2f));
//        float[] size = new float[]{min, min};
//
//        Font font = _graphics.newFont("Molle-Regular.ttf", 20, true);
//
//        int[] pos = new int[2];
//
//        initGridTypesMap();
//
//        int j = 0;
//        for (int i = 0; i < GridType.MAX.getValue(); i++) {
//            pos[0] = (int) (_graphics.getLogWidth() * 0.1f) * (1 + (3 * j));
//            pos[1] = (int) (_graphics.getLogHeight() * 0.143f) * (3 + (i / 3) * 2);
//
//            final SelectPackageButton _level = new SelectPackageButton(pos, size, _gridTypes.get(i), font, true);
//            _level.setCallback(new ButtonCallback() {
//                @Override
//                public void doSomething() {
//                    int r = _level.getRows();
//                    int c = _level.getCols();
//                    State gameState = new GameRandState(_engine, r, c, true);
//                    _engine.reqNewState(gameState);
//                    _audio.playSound(Assets.clickSound, 0);
//                    _audio.stopMusic();
//                }
//            });
//            _selectButtons.add(_level);
//            j++;
//            if (j == 3) j = 0;
//        }
//    }
//}
