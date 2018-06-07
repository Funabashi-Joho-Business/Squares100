package jp.croud.squares;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import jp.croud.squares.db.AppDB;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModeFragment extends DialogFragment implements View.OnClickListener {
	final static int MAX_SIZE = 10;

	interface OnModeStartListener{
		void onModeStart(int x,int y);
	}
	OnModeStartListener mListener;
	void setOnModeStartListener(OnModeStartListener listener){
		mListener = listener;
	}

	public ModeFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_mode, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		NumberPicker numX = view.findViewById(R.id.numX);
		NumberPicker numY = view.findViewById(R.id.numY);
		numX.setMinValue(1);
		numX.setMaxValue(MAX_SIZE);
		numY.setMinValue(1);
		numY.setMaxValue(MAX_SIZE);


		AppDB db = new AppDB(getContext());
		numX.setValue(db.getSetting("SizeX",MAX_SIZE));
		numY.setValue(db.getSetting("SizeY",MAX_SIZE));
		db.close();

		view.findViewById(R.id.buttonStart).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(mListener != null) {
			NumberPicker numX = getView().findViewById(R.id.numX);
			NumberPicker numY = getView().findViewById(R.id.numY);
			int x = numX.getValue();
			int y = numY.getValue();
			mListener.onModeStart(x,y);

			AppDB db = new AppDB(getContext());
			db.setSetting("SizeX",x);
			db.setSetting("SizeY",y);
			db.close();

			getDialog().cancel();
		}
	}
}
