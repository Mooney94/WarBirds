/**
 * @file        Assets.java
 * @author      Aaron Mooney 20072163
 * @assignment  Warbirds
 * @brief       Contains all assets used in the game
 *
 * @notes   	sound assets are commented as I kept getting errors    
 * 				
 */
package wit.cgd.warbirds.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import wit.cgd.warbirds.game.util.Constants;

public class Assets implements Disposable, AssetErrorListener {

	public static final String	TAG			= Assets.class.getName();
	public static final Assets	instance	= new Assets();

	private AssetManager		assetManager;

	public AssetFonts			fonts;

	//public AssetSounds			sounds;
	//public AssetMusic			music;

	public AssetPlayer			player;
	public AssetEnemy			greenEnemy;
	public AssetEnemy			goldEnemy;
	public AssetEnemy			whiteEnemy;
	public Asset				bullet;
	public Asset				doubleBullet;
	public AssetLevelDecoration	levelDecoration;

	private Assets() {}

	public void init(AssetManager assetManager) {

		this.assetManager = assetManager;
		assetManager.setErrorListener(this);

		// load texture for game sprites
		assetManager.load(Constants.TEXTURE_ATLAS_GAME, TextureAtlas.class);

		// TODO load sounds
		 assetManager.load("../android/assets/sounds/enemy_shoot.wav", Sound.class);
		 assetManager.load("../android/assets/sounds/explosion_large.wav", Sound.class);
		 assetManager.load("../android/assets/sounds/player_bullet_01.wav", Sound.class);
		 assetManager.load("../android/assets/sounds/player_hit.wav", Sound.class);


		// load music
		 assetManager.load("music/keith303_-_brand_new_highscore.mp3", Music.class);

		assetManager.finishLoading();


		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames())
			Gdx.app.debug(TAG, "asset: " + a);

		// create atlas for game sprites
		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_GAME);
		for (Texture t : atlas.getTextures())
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		fonts = new AssetFonts();

		// create game resource objects
		player = new AssetPlayer(atlas);
		greenEnemy = new AssetEnemy(atlas, "green");
		goldEnemy = new AssetEnemy(atlas, "yellow");
		whiteEnemy = new AssetEnemy(atlas, "white");
		levelDecoration = new AssetLevelDecoration(atlas);
		bullet = new Asset(atlas, "bullet");
		doubleBullet  = new Asset(atlas, "bullet_double");
		
		// create sound and music resource objects
		//sounds = new AssetSounds(assetManager);
		//music = new AssetMusic(assetManager);

	}

	@Override
	public void dispose() {
		assetManager.dispose();
	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + asset + "'", (Exception) throwable);
	}

	public class Asset {
		public final AtlasRegion	region;

		public Asset(TextureAtlas atlas, String imageName) {
			region = atlas.findRegion(imageName);
			Gdx.app.log(TAG, "Loaded asset '" + imageName + "'");
		}
	}

	public class AssetPlayer {
		public final AtlasRegion	region;
		public final Animation		animationNormal;
		public final Animation		animationExplosionBig;

		public AssetPlayer(TextureAtlas atlas) {
			region = atlas.findRegion("player");

			Array<AtlasRegion> regions = atlas.findRegions("player");
			animationNormal = new Animation(1.0f / 15.0f, regions, Animation.PlayMode.LOOP);
			regions = atlas.findRegions("explosion_big");
			animationExplosionBig = new Animation(1.0f / 15.0f, regions, Animation.PlayMode.LOOP);
		}
	}
	
	public class AssetEnemy {
		public final AtlasRegion	region;
		public final Animation		animationNormal;
		public final Animation		animationExplosionBig;
		
		public AssetEnemy(TextureAtlas atlas, String color){
			region = atlas.findRegion("enemy_plane_" + color);
			Array<AtlasRegion> regions = atlas.findRegions("enemy_plane_" + color);
			animationNormal = new Animation(1.0f / 15.0f, regions, Animation.PlayMode.LOOP);
			regions = atlas.findRegions("explosion_big");
			animationExplosionBig = new Animation(1.0f / 15.0f, regions, Animation.PlayMode.LOOP);
		}
	}

	public class AssetLevelDecoration {

		public final AtlasRegion	islandBig;
		public final AtlasRegion	islandSmall;
		public final AtlasRegion	islandTiny;
		public final AtlasRegion	water;

		public AssetLevelDecoration(TextureAtlas atlas) {
			water = atlas.findRegion("water");
			islandBig = atlas.findRegion("island_big");
			islandSmall = atlas.findRegion("island_small");
			islandTiny = atlas.findRegion("island_tiny");
		}
	}

	public class AssetFonts {
		public final BitmapFont	defaultSmall;
		public final BitmapFont	defaultNormal;
		public final BitmapFont	defaultBig;

		public AssetFonts() {
			// create three fonts using Libgdx's built-in 15px bitmap font
			defaultSmall = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
			defaultNormal = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
			defaultBig = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
			// set font sizes
			defaultSmall.getData().setScale(0.75f);
			defaultNormal.getData().setScale(1.0f);
			defaultBig.getData().setScale(4.0f);
			// enable linear texture filtering for smooth fonts
			defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
	}

	/*public class AssetSounds {

		// TODO list reference to sound assets
		public final Sound enemyShoot;
		public final Sound explosion;
		public final Sound playerShoot;
		public final Sound playerHit;
		

		public AssetSounds(AssetManager am) {
			// TODO 
			enemyShoot = am.get("../android/assets/sounds/enemy_shoot.wav", Sound.class);
			explosion = am.get("../android/assets/sounds/explosion.wav", Sound.class);
			playerShoot = am.get("../android/assets/sounds/player_bullet_01.wav", Sound.class);
			playerHit = am.get("../android/assets/sounds/player_hit.wav", Sound.class);
		}
	}

	public class AssetMusic {
		
		 public final Music music;

		public AssetMusic(AssetManager am) {
			
			music = am.get("music/keith303_-_brand_new_highscore.mp3", Music.class);
		}
	}*/

}
