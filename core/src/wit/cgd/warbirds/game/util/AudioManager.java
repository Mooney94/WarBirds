/**
 * @file        AudioManager.java
 * @author      Aaron Mooney 20072163
 * @assignment  Warbirds
 * @brief       Controls all audio in the game
 *
 * @notes       audio not working
 * 				
 */
package wit.cgd.warbirds.game.util;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
    public static final AudioManager    instance    = new AudioManager();

    private Music                       playingMusic;

    // singleton: prevent instantiation from other classes
    private AudioManager() {}

    public void play(Sound sound) {
        play(sound, 1);
    }

    public void play(Sound sound, float volume) {
        play(sound, volume, 1);
    }

    public void play(Sound sound, float volume, float pitch) {
        play(sound, volume, pitch, 0);
    }

    public void play(Sound sound, float volume, float pitch, float pan) {
        if (!GamePreferences.instance.sound) return;
        sound.play(GamePreferences.instance.soundVolume * volume, pitch, pan);
    }

    public void play(Music music) {
        stopMusic();
        playingMusic = music;
        if (GamePreferences.instance.music) {
            music.setLooping(true);
            music.setVolume(GamePreferences.instance.musicVolume);
            music.play();
        }
    }

    public void stopMusic() {
        if (playingMusic != null) playingMusic.stop();
    }

    public void onSettingsUpdated() {
        if (playingMusic == null) return;
        playingMusic.setVolume(GamePreferences.instance.musicVolume);
        if (GamePreferences.instance.music) {
            if (!playingMusic.isPlaying()) playingMusic.play();
        } else {
            playingMusic.pause();
        }
    }

}