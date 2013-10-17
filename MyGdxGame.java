package com.me.mygdxgame;

import java.io.File;
import java.io.IOException;

import org.puredata.android.io.PdAudio;
import org.puredata.core.PdBase;
import org.puredata.core.PdListener;
import org.puredata.core.utils.PdDispatcher;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyGdxGame implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	
	
	public final PdInterface pdinterface;
	public MyGdxGame(PdInterface pdinterface) {
		this.pdinterface = pdinterface;
	}
	
	public PdDispatcher pdDispatcher = new PdDispatcher() {
		@Override
		public void print(String s) {
			// TODO Auto-generated method stub
		}
	};
	
	public PdListener listener1 = new PdListener() {	
		@Override
		public void receiveSymbol(String source, String symbol) {}
		
		@Override
		public void receiveMessage(String source, String symbol, Object... args) {}
		
		@Override
		public void receiveList(String source, Object... args) {}
		
		@Override
		public void receiveFloat(String source, float x) {}
		
		@Override
		public void receiveBang(String source) {
			sprite.rotate(5f);
		}
	};

	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(1, h/w);
		batch = new SpriteBatch();
		
		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion region = new TextureRegion(texture, 0, 0, 512, 275);
		
		sprite = new Sprite(region);
		sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
		
		pdGo();

	}
	
	public void pdGo(){
        /* Copy Pd files from "assets" directory to local path which libpd can get */
        final FileHandle pdAssetsPath, pdLocalpath;
        pdLocalpath = Gdx.files.local("pd_files");
        pdLocalpath.mkdirs();
        
        if (Gdx.app.getType() == ApplicationType.Android) {
		   pdAssetsPath = Gdx.files.internal("testforblog.pd");
		} else {
		  // ApplicationType.Desktop ..
		  pdAssetsPath = Gdx.files.internal("./bin/testforblog.pd");
		}
        
        pdAssetsPath.copyTo(pdLocalpath);
        final String openPdPatch = new File(Gdx.files.getLocalStoragePath(), "pd_files/testforblog.pd").getAbsolutePath().substring(1);
        /* OK */
        
        try {PdBase.openPatch(openPdPatch);} 
        catch (IOException e) {e.printStackTrace();}
        
        try {PdAudio.initAudio(44100, 0, 2, 1, true);}
        catch (IOException e) {e.printStackTrace();}
        pdinterface.pdAudio_initAudio(44100, 0, 2, 1, true);
		pdinterface.pdAudio_startAudio();
		
		PdBase.setReceiver(pdDispatcher);
		pdDispatcher.addListener("metrorecv", listener1);
		
        /* Delete copied pd file */
        pdLocalpath.deleteDirectory();			
	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
		pdinterface.pdAudio_release();
		PdBase.release();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		sprite.draw(batch);
		batch.end();
		
		if (Gdx.input.justTouched()){
			PdBase.sendBang("attack");		
			sprite.rotate(-10f);
		}
		
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
