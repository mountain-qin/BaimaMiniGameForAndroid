package com.baima.bgmsound

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build

/**
 *音效播放器
 *用于播放raw文件夹下的音效文件；
 *
 */
class SoundPlayer private constructor(
    context: Context,
    var soundResIds: Array<Int>,
    var maxStreams: Int = 20
) {
    private val soundPool: SoundPool
    private val soundIdMap = mutableMapOf<Int, Int>()
    private val sharedPreferences: SharedPreferences
    private var isPlayed = true
    private var volume = DEFAULT_VOLUME


    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = SoundPool.Builder()
                .setMaxStreams(maxStreams)
                .build()
        } else {
            soundPool = SoundPool(maxStreams, AudioManager.STREAM_MUSIC, 1)
        }

        for (soundResId in soundResIds) {
            soundIdMap.put(soundResId, soundPool.load(context, soundResId, 1))
        }

        sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE)
        isPlayed = sharedPreferences.getBoolean(SHARED_PREFERENCE_IS_PLAYED, true)
        volume = sharedPreferences.getInt(SHARED_PREFERENCE_VOLUME, DEFAULT_VOLUME)
    }


    fun play(sounndResId: Int): Int {
        if (!isPlayed) {
            return -1
        }
        return soundPool.play(
            soundIdMap.get(sounndResId) ?: 0,
            volume / 100F,
            volume / 100F,
            1,
            0,
            1F
        )
    }


    fun release() {
        soundPool.release()
        instance = null
    }


    /**
     * 减小音量
     */
    fun reduceVolume(): Int {
        if (volume > 0) {
            volume -= 5

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

            sharedPreferences.edit()
                .putInt(SHARED_PREFERENCE_VOLUME, volume)
                .apply()
        }
        return volume
    }


    fun getPlayed(): Boolean {
        return isPlayed
    }


    fun setPlayed(isPlayed: Boolean) {
        this.isPlayed = isPlayed

        sharedPreferences.edit()
            .putBoolean(SHARED_PREFERENCE_IS_PLAYED, isPlayed)
            .apply()
    }


    fun getVolume(): Int {
        return volume
    }


    companion object {
        private val SHARED_PREFERENCE_IS_PLAYED = "sound_is_played"
        private val SHARED_PREFERENCE_VOLUME = "sound_volume"
        private val DEFAULT_VOLUME = 100

        private var instance: SoundPlayer? = null

        fun getInstance(
            context: Context,
            soundResIds: Array<Int>,
            maxStreams: Int = 20
        ): SoundPlayer {
            if (instance == null) {
                instance = SoundPlayer(context, soundResIds, maxStreams)
            }
            return instance!!
        }


        /**
         * 调用用这个方法之前已经调用过fun getInstance(context: Context,soundResIds: Array<Int>,maxStreams: Int = 20): SoundPlayer，返回值 不为空；
         */
        fun getInstance(): SoundPlayer? {
            return instance
        }
    }
}