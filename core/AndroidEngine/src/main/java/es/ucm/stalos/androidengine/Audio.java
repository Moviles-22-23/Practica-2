package es.ucm.stalos.androidengine;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.io.IOException;

public class Audio {
    public Audio(AssetManager assetManager, int numStreams) {
        _assetManager = assetManager;
        _mediaPlayer = new MediaPlayer();
        _mediaPlayer.reset();
        _soundPool = new SoundPool.Builder().setMaxStreams(numStreams).build();
    }

    //------------------------------------------SOUNDS--------------------------------------------//

    /**
     * Create a new sound from assets
     *
     * @param file filename
     * @return Sound
     */
    public Sound newSound(String file) throws Exception {
        Sound sound = new Sound("sounds/" + file, _assetManager);
        if (!sound.init()) throw new Exception();

        AssetFileDescriptor afd = sound.getAssetDescriptor();
        sound.setId(_soundPool.load(afd, 1));

        return sound;
    }

    /**
     * Play an specified sound indicating the number of looping
     *
     * @param sound       Sound to be played
     * @param numberLoops Indicate number of extra-repetitions: -1 -> Infinite
     */
    public void playSound(Sound sound, int numberLoops) {
        int id = ((Sound) sound).getId();
        int playId = _soundPool.play(id, 100, 100, 1, numberLoops, 1);
        ((Sound) sound).setPlayId(playId);
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
     * @param sound Sound to be played
     */
    public void playMusic(Sound sound) {
        AssetFileDescriptor afd = sound.getAssetDescriptor();
        if (!_mediaPlayer.isPlaying()) {
            try {
                _mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                _mediaPlayer.setLooping(true);
                _mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            _mediaPlayer.start();
            //System.out.println("playmusic");
        }
    }

    /**
     * Resume the background music
     *
     */
    public void resumeMusic() {
        _mediaPlayer.start();
    }

    /**
     * Pause current background music
     *
     */
    public void pauseBackMusic() {
        _mediaPlayer.pause();
    }

    /**
     * Stop the background music
     *
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
}
