package com.baima.bgmsound

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_bgm_sound_setting.*

class BgmSoundSettingActivity : AppCompatActivity(), View.OnClickListener {
    private var bgmMediaPlayer: BgmMediaPlayer? = null
    private var soundPlayer: SoundPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.bgm_sound_setting))
        setContentView(R.layout.activity_bgm_sound_setting)

        val context = this

        //背景音乐
        bgmMediaPlayer = BgmMediaPlayer.getInstance()

        cb_bgm.isChecked = bgmMediaPlayer?.getPlayed() ?: false
        tv_bgm_volume.text = bgmMediaPlayer?.getVolume().toString()

        cb_bgm.setOnCheckedChangeListener { buttonView, isChecked ->
            bgmMediaPlayer?.setPlayed(isChecked)
        }

        bt_reduce_bgm_volume.setOnClickListener(this)
        bt_increase_bgm_volume.setOnClickListener(this)

        //音效
        soundPlayer = SoundPlayer.getInstance()
        cb_sound.isChecked = soundPlayer?.getPlayed() ?: false
        tv_sound_volume.text = soundPlayer?.getVolume().toString()

        cb_sound.setOnCheckedChangeListener { buttonView, isChecked ->
            soundPlayer?.setPlayed(isChecked)
        }

        bt_reduce_sound_volume.setOnClickListener(this)
        bt_increase_sound_volume.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_reduce_bgm_volume -> tv_bgm_volume.text =
                bgmMediaPlayer?.reduceVolume().toString()
            R.id.bt_increase_bgm_volume -> tv_bgm_volume.text =
                bgmMediaPlayer?.increaseVolume().toString()

            R.id.bt_reduce_sound_volume -> {
                val volume = soundPlayer?.reduceVolume()
                tv_sound_volume.text = volume?.toString()
                ToastUtil.showToast(this, volume?.toString() ?: "")
            }
            R.id.bt_increase_sound_volume -> {
                val volume = soundPlayer?.increaseVolume()
                tv_sound_volume.text = volume?.toString()
                ToastUtil.showToast(this, volume?.toString() ?: "")
            }
        }
    }
}
