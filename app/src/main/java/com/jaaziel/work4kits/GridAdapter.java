package com.jaaziel.work4kits;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
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

			holder.titulo = (TextView) convertView.findViewById(R.id.label);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            holder.horario = (TextView) convertView.findViewById(R.id.horario);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}


        final Map<String, String> details = IOSingleton.Instance().getJobDetails(mMobileValues[position]);

        if (details != null) {
            holder.titulo.setText(details.get("Vaga"));
            holder.status.setText("Status: "+ details.get("Status"));
            holder.horario.setText("Horário: "+ details.get("Horário"));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (details != null && details.get("Status").equals("Livre"))
                showDialog(mContext, details, parent);
            }
        });

		return convertView;
	}

	static class ViewHolder {
		TextView titulo;
        TextView status;
        TextView horario;
	}

    public void showDialog(final Context context, final Map<String, String> details, final ViewGroup parent){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.vagadiallog);



        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        final String vagaId = details.get("id");
        String nomeVaga = details.get("Vaga");
        String descricao = details.get("Descrição");
        String horario = details.get("Horário");
        String empresa = details.get("Empresa");


        ((TextView) dialog.findViewById(R.id.vagaNome)).setText("Vaga: " + nomeVaga);
        ((TextView) dialog.findViewById(R.id.vagaEmpresa)).setText("Empresa: " + empresa);
        ((TextView) dialog.findViewById(R.id.vagaHorario)).setText("Horário: " + horario);
        ((TextView) dialog.findViewById(R.id.vagaDescricao)).setText("Descrição: " + descricao);

        dialog.findViewById(R.id.vagaAceita).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RESTUtil ut = new RESTUtil(Volley.newRequestQueue(context), Request.Method.PATCH, vagaId, context);
                IOSingleton.Instance().changeStatus(vagaId);
                notifyDataSetChanged();
                ((GridView) parent).invalidateViews();
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.vagaRejeita).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}