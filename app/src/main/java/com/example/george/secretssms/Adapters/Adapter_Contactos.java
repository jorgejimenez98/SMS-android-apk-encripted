package com.example.george.secretssms.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import com.example.george.secretssms.R;
import com.example.george.secretssms.modelo.Contacto;

import java.util.ArrayList;

public class Adapter_Contactos extends RecyclerView.Adapter<Adapter_Contactos.MyHolder> implements Filterable {
    RecyclerTouchListener listener;

    public interface RecyclerTouchListener {
        void onClickItem(View v, int position);
    }

    Context context;
    LayoutInflater inflater;
    CustomFilter filter;
    ArrayList<Contacto> model;
    ArrayList<Contacto> arrayList;

    public Adapter_Contactos(Context context, ArrayList<Contacto> model) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;
        this.arrayList = model;
    }


    public int getItemCount() {
        return model.size();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.row_contacto, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        Contacto contacto = model.get(position);
        if (contacto.getLetra() != null) {
            holder.letra.setText(contacto.getLetra());
            holder.contacto.setText(contacto.getContacto());
        } else {
            holder.letra.setText("");
            holder.contacto.setText(contacto.getContacto());
        }

    }

    public void setClickListener(RecyclerTouchListener value) {
        this.listener = value;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView contacto, letra;

        public MyHolder(final View view) {
            super(view);
            letra = (TextView) view.findViewById(R.id.txt_letra);
            contacto = (TextView) view.findViewById(R.id.txt_contacto);
            letra.setTextColor(Color.WHITE);
            contacto.setTextColor(Color.WHITE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onClickItem(view, getAdapterPosition());
                }
            });
        }
    }

    public ArrayList<Contacto> currientList() {
        return model;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter();
        }
        return filter;
    }

    class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                constraint = constraint.toString().toUpperCase();
                ArrayList<Contacto> filter = new ArrayList<>();
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getContacto().toUpperCase().contains(constraint)) {
                        Contacto x = arrayList.get(i);
                        filter.add(x);
                    }
                }
                results.count = filter.size();
                results.values = filter;
            } else {
                results.count = arrayList.size();
                results.values = arrayList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            model = (ArrayList<Contacto>) results.values;
            notifyDataSetChanged();
        }
    }
}
