package jp.croud.squares.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public abstract class SQLite extends SQLiteOpenHelper
{
	private SQLiteDatabase mDataBase;
	public SQLite(Context context, String dbName, int version)
	{
		super(context, dbName, null, version);
	}
	@Override
	public synchronized void close() {
		if(mDataBase != null)
			mDataBase.close();
		super.close();
	}
	public void insert(String tableName, ContentValues v){
		mDataBase.insert(tableName,null,v);
	}
	public boolean isTable(String name)
	{
		Cursor c = query("select name from sqlite_master where name='?';",name);
		boolean flag =  c.moveToFirst();
		c.close();
		return flag;
	}
	public boolean exec(String sql,Object ...params)
	{
		for(int i= 0;i<20;i++)
		{
			try {
				if(mDataBase == null || mDataBase.isReadOnly())
				{
					if(mDataBase != null)
						mDataBase.close();
					mDataBase = getWritableDatabase();
				}
				SQLiteStatement stmt = mDataBase.compileStatement(sql);
				for (int index=0;i<params.length;i++){
					if(params[i] instanceof Long)
						stmt.bindLong(index,(long)params[i]);
					else if(params[i] instanceof Double)
						stmt.bindDouble(index,(double)params[i]);
					else if(params[i] instanceof Byte[])
						stmt.bindBlob(index,(byte[])params[i]);
					else
						stmt.bindString(index,params[i].toString());
				}
				stmt.execute();
				return true;
			}catch(SQLiteDatabaseLockedException e)
			{
				System.out.println(e);
				try{Thread.sleep(100);} catch (InterruptedException e1) {}
			} catch (SQLException e) {

				System.out.println(e);
				break;
			}
		}

		return false;
	}
	public Cursor query(String sql,String... params)
	{
		for(int i= 0;i<20;i++)
		{
			try {
				if(mDataBase == null)
					mDataBase = getReadableDatabase();
				return mDataBase.rawQuery(sql,params);
			}catch(SQLiteDatabaseLockedException e)
			{
				System.out.println(e);
				try{Thread.sleep(100);} catch (InterruptedException e1) {}
			} catch (SQLException e) {

				System.out.println(e);
				break;
			}
		}
		return null;
	}
	//SQLインジェクション対策
	public static String STR(String str)
	{
		//シングルクオートをシングルクオート二つにエスケーブ
		return str.replaceAll("'", "''");
	}
	public void begin()
	{
		exec("begin;");
	}
	public void commit()
	{
		exec("commit;");
	}
}
