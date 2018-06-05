package jp.croud.squares;


import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class SquaresFragment extends Fragment {
	int mHeaderColor1 = 0xffffaa44;
	int mHeaderColor2 = 0xffdd8833;

	int[] mNumberX = new int[10];
	int[] mNumberY = new int[10];
	int mIndex;
	private Animation mAnimation;
	private SoundPool mSoundPool;
	private int mSound1;
	private int mSound2;

	public SquaresFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_squares, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
		//reset();
	}

	@Override
	public void onResume() {
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
		mSound1 = mSoundPool.load(getContext(), R.raw.sound1, 0);
		mSound2 = mSoundPool.load(getContext(), R.raw.sound2, 0);
	}

	@Override
	public void onPause() {
		mSoundPool.release();
		super.onPause();
	}

	public void reset(){
		Random rand = new Random();
		for(int i=0;i<10;i++){
			int a = rand.nextInt(10);
			int b = rand.nextInt(10);
			setSquaresText(i+1,0,String.valueOf(a));
			setSquaresColor(i+1,0,mHeaderColor1);

			setSquaresText(0,i+1,String.valueOf(b));
			setSquaresColor(0,i+1,mHeaderColor1);

			mNumberX[i] = a;
			mNumberY[i] = b;
		}
		for(int j=0;j<10;j++){
			for(int i=0;i<10;i++)
				setSquaresText(j+1,i+1,"");
		}
		mIndex = 0;
		setSquaresColor(0,mHeaderColor2);
	}
	void setSquaresText(int x,int y,String value){
		ViewGroup view = (ViewGroup)getView();
		ViewGroup line = (ViewGroup) view.getChildAt(y);
		ViewGroup frame = (ViewGroup) line.getChildAt(x);
		TextView textView = (TextView) frame.getChildAt(0);
		textView.setText(value);
		textView.startAnimation(mAnimation);
	}
	void setSquaresColor(int x,int y,int color){
		ViewGroup view = (ViewGroup)getView();
		ViewGroup line = (ViewGroup) view.getChildAt(y);
		ViewGroup frame = (ViewGroup) line.getChildAt(x);
		TextView textView = (TextView) frame.getChildAt(0);
		textView.setBackgroundColor(color);
	}
	public void setSquaresColor(int index,int color){
		if(mIndex >= 100)
			return;
		int x = mIndex % 10;
		int y = mIndex / 10;
		setSquaresColor(0,y+1,color);
		setSquaresColor(x+1,0,color);
	}
	public boolean sendNumber(int number){
		if(mIndex >= 100)
			return false;
		int x = mIndex % 10;
		int y = mIndex / 10;
		int value = (mNumberX[x]+mNumberY[y])%10;
		if(value == number){
			setSquaresText(x+1,y+1,String.valueOf(value));
			setSquaresColor(mIndex,mHeaderColor1);
			mIndex++;
			setSquaresColor(mIndex,mHeaderColor2);
			mSoundPool.play(mSound1, 1.0F, 1.0F, 0, 0, 1.0F);
			return true;
		}
		setSquaresText(x+1,y+1,"×");
		mSoundPool.play(mSound2, 1.0F, 1.0F, 0, 0, 1.0F);

		return false;
	}
	public int getAnserCount(){
		return mIndex;
	}
}
