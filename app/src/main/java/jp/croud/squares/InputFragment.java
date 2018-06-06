package jp.croud.squares;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class InputFragment extends Fragment implements View.OnClickListener {
	//ナンバークリックコールバック用リスナー
	interface OnNumberClickListener{
		void onNumberClick(int value);
	}
	OnNumberClickListener mListener;
	void setOnNumberClickListener(OnNumberClickListener listener){
		mListener = listener;
	}

	public InputFragment() {

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_input, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		//キー配置リスト
		final int[] num = {7, 8, 9, 4, 5, 6, 1, 2, 3, 0, 0, 0};
		//キー設定
		for(int i=0;i<num.length;i++){
			ViewGroup parent = (ViewGroup)((ViewGroup)view).getChildAt(i/3);
			ImageButton imageButton = (ImageButton)((ViewGroup)parent.getChildAt(i%3)).getChildAt(0);
			imageButton.setTag(num[i]);
			imageButton.setOnClickListener(this);
			TextView textView = (TextView)((ViewGroup)parent.getChildAt(i%3)).getChildAt(1);
			textView.setText(String.valueOf(num[i]));
		}

	}

	@Override
	public void onClick(View v) {
		//キータップコールバック
		if(mListener != null)
			mListener.onNumberClick((int)v.getTag());
	}
}
