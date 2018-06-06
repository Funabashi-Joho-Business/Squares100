package jp.croud.squares;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements InputFragment.OnNumberClickListener, View.OnClickListener {
	InputFragment mInput;
	SquaresFragment mSquares;
	TextView mTextView;
	Timer mTimer;
	long mStartCount;
	long mStopCount;
	boolean mActive;

	private SoundPool mSoundPool;
	private int mSound1;
	private int mSound2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mInput = (InputFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentInput);
		mInput.setOnNumberClickListener(this);
		mSquares = (SquaresFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentSquares);

		findViewById(R.id.button).setOnClickListener(this);
		mTextView = findViewById(R.id.textTime);
	}

	@Override
	protected void onPause() {
		super.onPause();
		stop();
		mSoundPool.release();
	}

	@Override
	protected void onResume() {
		super.onResume();
		//サウンドリソースの読み込み
		AudioAttributes attr = new AudioAttributes.Builder()
			                       .setUsage(AudioAttributes.USAGE_MEDIA)
			                       .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
			                       .build();
		mSoundPool = new SoundPool.Builder()
			             .setAudioAttributes(attr)
			             .setMaxStreams(5)
			             .build();

		mSound1 = mSoundPool.load(this, R.raw.sound1, 0);
		mSound2 = mSoundPool.load(this, R.raw.sound2, 0);
		//ゲーム進行中なら再開
		if(mActive)
			start(0,0,false);
	}

	@Override
	public void onNumberClick(int value) {
		if(!mActive)    //ゲーム進行中か？
			return;
		//タップされたナンバーを出力し、結果を受け取る
		if(mSquares.sendNumber(value))
			mSoundPool.play(mSound1, 1.0F, 1.0F, 0, 0, 1.0F);   //成功音
		else
			mSoundPool.play(mSound2, 1.0F, 1.0F, 0, 0, 1.0F);   //失敗音

		//全問を解答したら終了
		if(mSquares.getAnswerCount() == mSquares.getAllCount())
		{
			mActive = false;
			stop();
		}
	}

	@Override
	public void onClick(View v) {
		//スタートを押したら、モードセレクト用のダイアログを表示
		final ModeFragment dialog = new ModeFragment();
		dialog.setOnModeStartListener(new ModeFragment.OnModeStartListener() {
			@Override
			public void onModeStart(int x, int y) {
				start(x,y,true);
			}
		});
		dialog.show(getSupportFragmentManager(),null);

	}

	void start(int x,int y,boolean reset){
		mActive = true; //ゲーム進行中フラグを立てる
		//再開かリセットか確認
		if(reset){
			mSquares.reset(x,y);  //内容をリセット
			mStartCount = System.currentTimeMillis();
		}else //再開なら時間を調整する
			mStartCount = System.currentTimeMillis()-mStopCount;

		//すでにタイマーが動いていたら停止
		if(mTimer != null)
			mTimer.cancel();
		//タイマー処理を開始
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(mTimerRun);
			}
		};
		mTimer = new Timer();
		mTimer.schedule(task,0,100);
	}
	void stop(){
		//タイマーの停止処理
		if(mTimer != null){
			mTimer.cancel();
			mTimer = null;
		}
		//再開に備えて経過時間を記憶
		mStopCount = System.currentTimeMillis() - mStartCount;
	}

	Runnable mTimerRun = new Runnable(){
		@Override
		public void run() {
			long t = System.currentTimeMillis() - mStartCount;
			mTextView.setText(String.format("%.1f",t/1000.0));
		}
	};
}
