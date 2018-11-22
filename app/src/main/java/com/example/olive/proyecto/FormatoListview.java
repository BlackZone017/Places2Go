package com.example.olive.proyecto;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FormatoListview extends ArrayAdapter {

    private String[] lugares;
    private String[] descrip;
    private Integer[] images;
    private Activity context;

    public FormatoListview(Activity context, String[] gustos, String[] descrip, Integer[] images ) {
        super(context, R.layout.formato_listview, gustos);

        this.context=context;
        this.lugares=gustos;
        this.descrip=descrip;
        this.images=images;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        View v =convertView;
        ViewHolder viewHolder=null;

        if (v==null){

            LayoutInflater layoutInflater=context.getLayoutInflater();
            v=layoutInflater.inflate(R.layout.formato_listview, null, true);
            viewHolder = new ViewHolder(v);
            v.setTag(viewHolder);


        }else  {
            viewHolder= (ViewHolder) v.getTag();

        }

        viewHolder.imagen.setImageResource(images[position]);
        viewHolder.txtnombre.setText(lugares[position]);
        viewHolder.txtDescrip.setText(descrip[position]);

        return v;
    }

    class ViewHolder{

        TextView txtnombre;
        TextView txtDescrip;
        ImageView imagen;
        ViewHolder(View v){

            txtnombre=v.findViewById(R.id.txtnombre);
            txtDescrip=v.findViewById(R.id.descripcion);
            imagen=v.findViewById(R.id.imgview);
        }

    }
}
