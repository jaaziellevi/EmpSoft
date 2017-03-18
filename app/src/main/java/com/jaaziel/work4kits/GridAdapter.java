package com.jaaziel.work4kits;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
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
	public View getView(int position, View convertView, final ViewGroup parent) {

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

		convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(mContext, details);
			}
		});

		return convertView;
	}

	static class ViewHolder {
        ImageView imageView;
		TextView text;
	}

    public void showDialog(final Context context, final Map<String,String> details){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.vagadiallog);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);

        final String vagaId = details.get("id");
        String nomeVaga = details.get("Vaga");
        String descricao = details.get("Descrição");
        String horario = details.get("Horário");


        ((TextView) dialog.findViewById(R.id.vagaNome)).setText("Vaga: " + nomeVaga);
        ((TextView) dialog.findViewById(R.id.vagaDescricao)).setText("Descrição: " + descricao);
        ((TextView) dialog.findViewById(R.id.vagaHorario)).setText("Horário: " + horario);

        dialog.findViewById(R.id.textView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RESTUtil ut = new RESTUtil(Volley.newRequestQueue(context), Request.Method.PATCH, vagaId);
                IOSingleton.Instance().changeStatus(vagaId);
                notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.textView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}