package es.ucm.stalos.androidengine;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.io.IOException;
import java.util.HashMap;

public class Audio {
    public Audio(AssetManager assetManager, int numStreams) {
        _assetManager = assetManager;
        _mediaPlayer = new MediaPlayer();
        _mediaPlayer.reset();
        _soundPool = new SoundPool.Builder().setMaxStreams(numStreams).build();
        _sounds = new HashMap<>();
    }

    //------------------------------------------SOUNDS--------------------------------------------//

    /**
     * Creates and stores a new sound ready to be used
     *
     * @param name     Sound's name-key to store
     * @param fileName File name of the sound with extension
     * @throws Exception if the creation fails
     */
    public void newSound(String name, String fileName) throws Exception {
        Sound sound = new Sound("sounds/" + fileName, _assetManager);
        if (!sound.init())
            throw new Exception();

        _sounds.put(name, sound);
        AssetFileDescriptor afd = sound.getAssetDescriptor();
        sound.setId(_soundPool.load(afd, 1));

    }

    /**
     * Play an specified sound indicating the number of looping
     *
     * @param soundName   Name-Key of the sound to be played
     * @param numberLoops Indicate number of extra-repetitions: -1 -> Infinite
     */
    public void playSound(String soundName, int numberLoops) {
        if (!_sounds.containsKey(soundName)) {
            System.err.println("El sonido '" + soundName + "' no existe...");
            return;
        }

        Sound so = _sounds.get(soundName);

        int id = so.getId();
        int playId = _soundPool.play(id, 100, 100, 1, numberLoops, 1);
        so.setPlayId(playId);
    }

    /**
     * Resume an specified paused sound.
     *
     * @param sound Sound to be resumed.
     */
    public void resumeSound(Sound sound) {
        int id = sound.getPlayId();
        _soundPool.resume(id);
    }

    /**
     * Pause an specified sound. It can be resume.
     *
     * @param sound Sound to be paused
     */
    public void pauseSound(Sound sound) {
        int id = sound.getPlayId();
        _soundPool.pause(id);
    }

    /**
     * Stopped an specified sound. Restart the clip.
     *
     * @param sound Sound to be stopped
     */
    public void stopSound(Sound sound) {
        int id = sound.getPlayId();
        _soundPool.stop(id);
    }

    //-------------------------------------------MUSIC--------------------------------------------//

    /**
     * Play a looped specified sound
     *
     * @param soundName Sound to be played
     */
    public void playMusic(String soundName) {
        if (!_sounds.containsKey(soundName)) {
            System.err.println("La música '" + soundName + "' no existe...");
            return;
        }

        Sound so = _sounds.get(soundName);


        AssetFileDescriptor afd = so.getAssetDescriptor();
        if (!_mediaPlayer.isPlaying()) {
            try {
                _mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                _mediaPlayer.setLooping(true);
                _mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            _mediaPlayer.start();
        }
    }

    /**
     * Resume the background music
     */
    public void resumeMusic() {
        _mediaPlayer.start();
    }

    /**
     * Pause current background music
     */
    public void pauseBackMusic() {
        _mediaPlayer.pause();
    }

    /**
     * Stop the background music
     */
    public void stopMusic() {
        _mediaPlayer.stop();
        _mediaPlayer.reset();
    }

    //--------------------------------------------------------------------------------------------//
    /**
     * Reference to the AssetManager of Android
     */
    private AssetManager _assetManager;
    /**
     * Reference to the audio library of Android
     */
    private MediaPlayer _mediaPlayer;
    /**
     * Sounds' bank
     */
    private SoundPool _soundPool;
    /**
     * Dictionary which contains the fonts
     */
    private HashMap<String, Sound> _sounds;
}
