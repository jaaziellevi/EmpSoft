package com.jaaziel.work4kits;

import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

import java.util.Map;

public class GridAdapter extends BaseAdapter {

	private Context mContext;
	private final String[] mMobileValues;

    public GridAdapter(Context context, String[] mobileValues) {
        RESTUtil ut = new RESTUtil(Volley.newRequestQueue(context), Request.Method.GET, context);

        mContext = context;
		mMobileValues = mobileValues;
    }

	@Override
	public int getCount() {
		return mMobileValues.length;
	}

	@Override
	public String getItem(int position) {
		return mMobileValues[position];
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.grid_item, parent, false);
			holder = new ViewHolder();

			holder.text = (TextView) convertView.findViewById(R.id.label);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageStatus);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}


        final Map<String, String> details = IOSingleton.Instance().getJobDetails(mMobileValues[position]);
        if (details != null) {
            holder.text.setText(details.get("Vaga"));
            int imagem = 0;
            if (details.get("Status").equals("Livre")) {
                imagem = R.drawable.thumb_green_48;
            } else imagem = R.drawable.thumb_yellow_48;
            holder.imageView.setImageDrawable(ResourcesCompat.getDrawable(convertView.getResources(), imagem, null));
        }

        //Fazer um dialog simples ou custom para status livre ou solicitado
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                alertDialog.setTitle(details.get("Vaga"));
                alertDialog.setMessage("Status da vaga: " + details.get("Status"));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

		return convertView;
	}

	static class ViewHolder {
        ImageView imageView;
		TextView text;
	}
}