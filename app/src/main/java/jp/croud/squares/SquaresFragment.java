package jp.croud.squares;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class SquaresFragment extends Fragment {
	int mSizeX = 10;
	int mSizeY = 10;
	int[] mNumberX = new int[10];
	int[] mNumberY = new int[10];
	int mIndex;
	private Animation mAnimation;


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

		//getLayoutInflater().inflate(R.layout.fragment_squares,)
		reset(1,1);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	public void reset(int x,int y){
		//マス目の作成
		int countX = x+1;
		int countY = y+1;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1);
		LinearLayout parent = (LinearLayout)getView();
		parent.removeAllViews();
		for(int j=0;j<countY;j++){
			LinearLayout layout = new LinearLayout(getContext());
			for(int i=0;i<countX;i++){
				if(i == 0 && j == 0)
					layout.addView(new Space(getContext()), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,1));
				else
					getLayoutInflater().inflate(R.layout.frame_item,layout,true);
			}
			parent.addView(layout,params);
		}
		//データの配置
		mSizeX = x;
		mSizeY = y;
		Random rand = new Random();
		for(int i=0;i<x;i++){
			int value = rand.nextInt(10);
			setSquaresText(i+1,0,String.valueOf(value));
			setSquaresColor(i+1,0,R.drawable.ic_header1);
			mNumberX[i] = value;
		}
		for(int i=0;i<y;i++){
			int value = rand.nextInt(10);
			setSquaresText(0,i+1,String.valueOf(value));
			setSquaresColor(0,i+1,R.drawable.ic_header1);
			mNumberY[i] = value;
		}
		for(int j=0;j<y;j++){
			for(int i=0;i<x;i++) {
				setSquaresText(i+1,j + 1,  "");
				setSquaresColor(i + 1,j + 1,  R.drawable.ic_item1);
			}
		}
		mIndex = 0;
		setSquaresColor(0,R.drawable.ic_header2);
		setSquaresColor2(0,R.drawable.ic_item2);
	}
	void setSquaresText(int x,int y,String value){
		ViewGroup view = (ViewGroup)getView();
		ViewGroup line = (ViewGroup) view.getChildAt(y);
		ViewGroup frame = (ViewGroup) line.getChildAt(x);
		TextView textView = (TextView) frame.getChildAt(0);
		textView.setText(value);
		textView.startAnimation(mAnimation);
	}
	void setSquaresColor(int x,int y,int id){
		ViewGroup view = (ViewGroup)getView();
		ViewGroup line = (ViewGroup) view.getChildAt(y);
		ViewGroup frame = (ViewGroup) line.getChildAt(x);
		frame.setBackgroundResource(id);
	}
	public void setSquaresColor(int index,int id){
		if(mIndex >= mSizeX*mSizeY)
			return;
		int x = mIndex % mSizeX;
		int y = mIndex / mSizeX;
		setSquaresColor(0,y+1,id);
		setSquaresColor(x+1,0,id);
	}
	public void setSquaresColor2(int index,int id){
		if(mIndex >= mSizeX*mSizeY)
			return;
		int x = mIndex % mSizeX;
		int y = mIndex / mSizeX;
		setSquaresColor(x+1,y+1,id);
	}
	public boolean sendNumber(int number){
		if(mIndex >= mSizeX*mSizeY)
			return false;
		int x = mIndex % mSizeX;
		int y = mIndex / mSizeX;
		int value = (mNumberX[x]+mNumberY[y])%10;
		if(value == number){
			setSquaresText(x+1,y+1,String.valueOf(value));
			setSquaresColor(mIndex,R.drawable.ic_header1);
			setSquaresColor2(mIndex,R.drawable.ic_item1);
			mIndex++;
			setSquaresColor(mIndex,R.drawable.ic_header2);
			setSquaresColor2(mIndex,R.drawable.ic_item2);
			return true;
		}
		setSquaresText(x+1,y+1,"×");
		return false;
	}
	public int getAnswerCount(){
		return mIndex;
	}
	public int getAllCount(){
		return mSizeX * mSizeY;
	}
}
