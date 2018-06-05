package jp.croud.squares;

import android.media.AudioAttributes;
import android.media.AudioManager;
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

		AudioAttributes attr = new AudioAttributes.Builder()
			                       .setUsage(AudioAttributes.USAGE_MEDIA)
			                       .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
			                       .build();
		mSoundPool = new SoundPool.Builder()
			             .setAudioAttributes(attr)
			             .setMaxStreams(5)
			             .build();

		mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		mSound1 = mSoundPool.load(this, R.raw.sound1, 0);
		mSound2 = mSoundPool.load(this, R.raw.sound2, 0);

		if(mActive)
			start(0,0,false);
	}

	@Override
	public void onNumberClick(int value) {
		if(!mActive)
			return;
		if(mSquares.sendNumber(value))
			mSoundPool.play(mSound1, 1.0F, 1.0F, 0, 0, 1.0F);
		else
			mSoundPool.play(mSound2, 1.0F, 1.0F, 0, 0, 1.0F);

		if(mSquares.getAnswerCount() == 100)
		{
			mActive = false;
			stop();
		}
	}

	@Override
	public void onClick(View v) {
		//start(true);
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
		mActive = true;
		if(reset){
			mSquares.reset(x,y);
			mStartCount = System.currentTimeMillis();
		}else
			mStartCount = System.currentTimeMillis()-mStopCount;
		if(mTimer != null)
			mTimer.cancel();
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
		if(mTimer != null){
			mTimer.cancel();
			mTimer = null;
		}
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
