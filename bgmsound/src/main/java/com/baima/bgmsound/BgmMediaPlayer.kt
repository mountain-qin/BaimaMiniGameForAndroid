package com.baima.bgmsound

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.MediaPlayer

/**
 * 用于播放assets下的文件
 */
class BgmMediaPlayer private constructor(private val context: Context, val fileName: String) {
    private val mediaPlayer = MediaPlayer()
    private var isPlayed = true
    private var volume = DEFAULT_VOLUME

    private var sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE)
        isPlayed = sharedPreferences.getBoolean(SHARED_PREFERENCE_IS_PLAYED, true)
        volume = sharedPreferences.getInt(SHARED_PREFERENCE_VOLUME, DEFAULT_VOLUME)
        mediaPlayer.setVolume(volume / 100F, volume / 100F)
    }


    fun play() {
        if (!isPlayed) {
            return
        }

        mediaPlayer.reset()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)

        val assetFileDescriptor = context.assets.openFd(fileName)
        mediaPlayer.setDataSource(
            assetFileDescriptor.fileDescriptor,
            assetFileDescriptor.startOffset,
            assetFileDescriptor.length
        )
        assetFileDescriptor.close()

        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
        }

        mediaPlayer.setOnCompletionListener {
            mediaPlayer.start()
        }
    }


    fun stop() {
        mediaPlayer.stop()
    }


    fun release() {
        mediaPlayer.release()
        instance = null
    }


    fun getPlayed(): Boolean {
        return isPlayed
    }


    fun setPlayed(isPlayed: Boolean) {
        this.isPlayed = isPlayed

        if (isPlayed) {
            play()
        } else {
            stop()
        }

        sharedPreferences.edit()
            .putBoolean(SHARED_PREFERENCE_IS_PLAYED, isPlayed)
            .apply()
    }


    fun getVolume(): Int {
        return volume
    }


    /**
     * 减小音量
     */
    fun reduceVolume(): Int {
        if (volume > 0) {
            volume -= 5
            mediaPlayer.setVolume(volume / 100F, volume / 100F)

            sharedPreferences.edit()
                .putInt(SHARED_PREFERENCE_VOLUME, volume)
                .apply()
        }
        return volume
    }


    /**
     * 增大音量
     */
    fun increaseVolume(): Int {
        if (volume < 100) {
            volume += 5
            mediaPlayer.setVolume(volume / 100F, volume / 100F)

            sharedPreferences.edit()
                .putInt(SHARED_PREFERENCE_VOLUME, volume)
                .apply()
        }
        return volume
    }


    companion object {
        private val SHARED_PREFERENCE_IS_PLAYED = "bgm_is_played"
        private val SHARED_PREFERENCE_VOLUME = "bgm_volume"
        private val DEFAULT_VOLUME = 100

        private var instance: BgmMediaPlayer? = null


        fun getInstance(context: Context, fileName: String): BgmMediaPlayer {
            if (instance == null) {
                instance = BgmMediaPlayer(context, fileName)
            }
            return instance!!
        }


        /**
         * 如果调用这个方法之前调用过        fun getInstance(context: Context, fileName: String): BgmMediaPlayer，返回值不为空；
         */
        fun getInstance(): BgmMediaPlayer? {
            return instance
        }
    }
}