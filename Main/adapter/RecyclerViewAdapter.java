package com.example.agenda.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda.DAO.DAO;
import com.example.agenda.DadosPessoais;
import com.example.agenda.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    Context context;
    List<String> nomes = new ArrayList<String>();
    String[] telefones;
    String[] sexos;
    View viewOnCreate;
    ViewHolder viewHolderLocal;

    public RecyclerViewAdapter(Context contextRecebido, String[] nomesRecebidos, String[] telefonesRecebidos, String[] sexosRecebidos){
        context = contextRecebido;
        nomes.addAll(Arrays.asList(nomesRecebidos));
        telefones = telefonesRecebidos;
        sexos = sexosRecebidos;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textNome;
        public TextView textTelefone;
        public ImageView icone;

        public ViewHolder(View itemView){
            super(itemView);

            textNome = itemView.findViewById(R.id.textNome);
            textTelefone = itemView.findViewById(R.id.textTelefone);
            icone = itemView.findViewById(R.id.icone);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("Testando...", "Click");


                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewOnCreate = LayoutInflater.from(context).inflate(R.layout.recyclerview_itens, parent, false);
        viewHolderLocal = new ViewHolder(viewOnCreate);
        return viewHolderLocal;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.textNome.setText(nomes.get(position));
        holder.textTelefone.setText(telefones[position]);

        if(sexos[position].equals("F")){
            holder.icone.setImageResource(R.drawable.ic_woman);
        }else{
            holder.icone.setImageResource(R.drawable.ic_man);
        }

        viewOnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DadosPessoais.class);
                intent.putExtra("nome", nomes.get(position));

                intent.putExtra("sexo", sexos[position]);
                intent.putExtra("telefone", telefones[position]);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);


//                DAO dao = new DAO(context);
//                dao.apagarPessoa(nomes.get(position));
//
//                nomes.remove(position);
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, nomes.size());

            }
        });

//        holder.icone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DAO dao = new DAO(context);
//                dao.apagarPessoa(nomes.get(position));
//
//                nomes.remove(position);
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, nomes.size());
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return nomes.size();
    }
}
