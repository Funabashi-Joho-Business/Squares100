package jp.croud.squares.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AppDB extends SQLite {
	public AppDB(Context context) {
		//ここでデータベースのファイル名とバージョン番号を指定
		super(context, "app.db",1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//初期テーブルの作成
		db.execSQL("CREATE TABLE setting(setting_name text primary key,setting_value text);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//バージョン番号を変えた場合に呼び出される
	}
	public void clearSetting(String name)
	{
		exec("delete from setting where setting_name=?",name);
	}
	public void setSetting(String name,String value)
	{
		String sql = String.format("replace into setting values('%s','%s');",STR(name),STR(value));
		exec(sql);
	}
	public void setSetting(String name,Object value)
	{
		String sql = String.format("replace into setting values('%s','%s');",STR(name),STR(value.toString()));
		exec(sql);
	}
	public String getSetting(String name)
	{
		Cursor c = query("select setting_value from setting where setting_name=?;", STR(name));
		if(!c.moveToFirst())
			return null;
		String s = c.getString(0);
		c.close();
		return s;

	}
	public boolean getSetting(String name,boolean flag)
	{
		String v = getSetting(name);
		if(v == null)
			return flag;
		return v.equals("1");
	}
	public String getSetting(String name,String defValue)
	{
		String v = getSetting(name);
		if(v == null)
			return defValue;
		return v;
	}
	public int getSetting(String name,int defValue)
	{
		String v = getSetting(name);
		if(v == null)
			return defValue;
		return Integer.valueOf(v);
	}
	public float getSetting(String name,float defValue)
	{
		try {
			String v = getSetting(name);
			if(v == null)
				return defValue;
			return Integer.valueOf(v);
		} catch (NumberFormatException e) {
			return defValue;
		}
	}
	public long getSetting(String name,long defValue)
	{
		String v = getSetting(name);
		if(v == null)
			return defValue;
		return Long.valueOf(v);
	}
}
