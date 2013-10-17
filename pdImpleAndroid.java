package com.me.mygdxgame;

import java.io.IOException;

import org.puredata.android.io.PdAudio;

import android.app.Activity;

public class pdImpleAndroid extends Activity implements PdInterface{
	
	@Override
	public void pdAudio_startAudio() {
		PdAudio.startAudio(this);
	}
	
	@Override
	public void pdAudio_initAudio(int sampleRate, int inChannels, int outChannels, final int ticksPerBuffer, boolean restart) {
		try {
			PdAudio.initAudio(sampleRate, inChannels, outChannels, ticksPerBuffer, restart);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void pdAudio_stopAudio() {
		PdAudio.stopAudio();
	}

	@Override
	public boolean pdAudio_isRunning() {
		return PdAudio.isRunning();
	}

	@Override
	public void pdAudio_release() {
		PdAudio.release();
	}

}