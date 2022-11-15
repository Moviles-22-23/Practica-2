package es.ucm.stalos.androidengine;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.io.IOException;

public class AndroidAudio {
    public AndroidAudio(AssetManager assetManager) {
        _assetManager = assetManager;
        _mediaPlayer = new MediaPlayer();
        _mediaPlayer.reset();
        _soundPool = new SoundPool.Builder().setMaxStreams(10).build();
    }

    public AndroidSound newSound(String file) throws Exception {
        AndroidSound sound = new AndroidSound("sounds/" + file, _assetManager);
        if (!sound.init()) throw new Exception();

        AssetFileDescriptor afd = sound.getAssetDescriptor();
        sound.setId(_soundPool.load(afd, 1));

        return sound;
    }

    public AndroidSound getSound(String id) {
        return null;
    }

    public void play(AndroidSound sound, int numberLoops) {
        int id = ((AndroidSound) sound).getId();
        int playId = _soundPool.play(id, 100, 100, 1, numberLoops, 1);
        ((AndroidSound) sound).setPlayId(playId);
    }

    public void playMusic(AndroidSound sound) {
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

    public void pause(AndroidSound sound) {
        int id = sound.getPlayId();
        _soundPool.pause(id);
    }

    public void pause() {
        _mediaPlayer.pause();
    }

    public void pauseMusic(AndroidSound music) {
        _mediaPlayer.pause();
    }

    public void stop(AndroidSound sound) {
        int id = sound.getPlayId();
        _soundPool.stop(id);
    }

    public void stopMusic(AndroidSound music) {
        _mediaPlayer.stop();
        _mediaPlayer.reset();
    }

    public void resume(AndroidSound sound) {
        int id = sound.getPlayId();
        _soundPool.resume(id);
    }


    public void resumeMusic(AndroidSound music) {
        _mediaPlayer.start();
    }

    public void resume() {
        _mediaPlayer.start();
    }

    /**
     * Reference to the AssetManager of Android
     */
    private AssetManager _assetManager;
    /**
     * Reference to the audio library of Android
     */
    private MediaPlayer _mediaPlayer;
    private SoundPool _soundPool;
}
