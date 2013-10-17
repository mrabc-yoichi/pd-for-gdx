package com.me.mygdxgame;

public interface PdInterface {
	public void pdAudio_startAudio();
	public void pdAudio_initAudio(int sampleRate, int inChannels, int outChannels, final int ticksPerBuffer, boolean restart);
	public void pdAudio_stopAudio();
	public boolean pdAudio_isRunning();
	public void pdAudio_release();	
}
