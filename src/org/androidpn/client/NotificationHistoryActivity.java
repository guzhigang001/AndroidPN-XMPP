package org.androidpn.client;

import java.util.ArrayList;
import java.util.List;

import org.androidpn.demoapp.R;
import org.litepal.crud.DataSupport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NotificationHistoryActivity extends Activity {
	
	private ListView listView;
	private notificationAdapter adapter;
	private List<NotificationHistory> listDataHistories=new ArrayList<NotificationHistory>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_history);
		listDataHistories=DataSupport.findAll(NotificationHistory.class);
		listView=(ListView) findViewById(R.id.list);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				NotificationHistory history=listDataHistories.get(arg2);
				Intent intent = new Intent(NotificationHistoryActivity.this,
		                NotificationDetailsActivity.class);
		        intent.putExtra(Constants.NOTIFICATION_API_KEY, history.getApiKey());
		        intent.putExtra(Constants.NOTIFICATION_TITLE, history.getTitle());
		        intent.putExtra(Constants.NOTIFICATION_MESSAGE, history.getMessage());
		        intent.putExtra(Constants.NOTIFICATION_URI, history.getUri());
		        intent.putExtra(Constants.NOTIFICATION_IMAGE_URL, history.getImageUrl());
				startActivity(intent);
			}
		});
		
		adapter=new notificationAdapter(this, 0, listDataHistories);
		listView.setAdapter(adapter);
		registerForContextMenu(listView);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0,0,0,"É¾³ý");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		if (item.getItemId()==0) {
			AdapterContextMenuInfo menuInfo=(AdapterContextMenuInfo) item.getMenuInfo();
			int index=menuInfo.position;
			NotificationHistory history=listDataHistories.get(index);
			history.delete();
			listDataHistories.remove(index);
			adapter.notifyDataSetChanged();
		}
		return super.onContextItemSelected(item);
		
	}
	class notificationAdapter extends ArrayAdapter<NotificationHistory>{

		public notificationAdapter(Context context,
				int textViewResourceId, List<NotificationHistory> objects) {
			super(context, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			NotificationHistory nHistory=getItem(position);
            View view;
            if (convertView==null) {
				view=LayoutInflater.from(getContext()).inflate(R.layout.notification_history_item, null);
			}else {
				view=convertView;
			}
            TextView tv_title=(TextView) view.findViewById(R.id.tv_title);
            TextView tv_time=(TextView) view.findViewById(R.id.tv_time);

            tv_title.setText(nHistory.getTitle());
            tv_time.setText(nHistory.getTime());
			return view;
		}
		
	}
}
