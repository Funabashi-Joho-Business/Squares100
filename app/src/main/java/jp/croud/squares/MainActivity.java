package jp.croud.squares;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(mActive)
			start(false);
	}

	@Override
	public void onNumberClick(int value) {
		if(!mActive)
			return;
		mSquares.sendNumber(value);
		if(mSquares.getAnserCount() == 100)
		{
			mActive = false;
			stop();
		}
	}

	@Override
	public void onClick(View v) {
		start(true);
	}

	void start(boolean reset){
		mActive = true;
		if(reset){
			mSquares.reset();
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
