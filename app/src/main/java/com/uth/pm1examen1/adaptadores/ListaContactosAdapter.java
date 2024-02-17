package com.uth.pm1examen1.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uth.pm1examen1.R;
import com.uth.pm1examen1.VerActivity;
import com.uth.pm1examen1.models.Contactos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListaContactosAdapter extends RecyclerView.Adapter<ListaContactosAdapter.ContactoViewHolder> {

    ArrayList<Contactos> listaContactos;
    ArrayList<Contactos> listaOriginal;
    public String Codigo, Pais;

    public ListaContactosAdapter(ArrayList<Contactos> listaContactos) {
        this.listaContactos = listaContactos;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(listaContactos);
    }

    @NonNull
    @Override
    public ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_contacto, null, false);
        return new ContactoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactoViewHolder holder, int position) {

        if (listaContactos.get(position).getPais() == 1) {
            Pais = "Honduras";
            Codigo ="+504";
        }
        if (listaContactos.get(position).getPais() == 2) {
            Pais = "Guatelama";
            Codigo ="+502";
        }
        if (listaContactos.get(position).getPais() == 3) {
            Pais = "El Salvador";
            Codigo ="+503";
        }
        if (listaContactos.get(position).getPais() == 4) {
            Pais = "Nicaragua";
            Codigo ="+505";
        }
        if (listaContactos.get(position).getPais() == 5) {
            Pais = "Costa Rica";
            Codigo ="+506";
        }

        holder.viewPais.setText(Pais);
        holder.viewNombre.setText(listaContactos.get(position).getNombre());
        holder.viewTelefono.setText(Codigo+ " "+listaContactos.get(position).getTelefono());

        File foto = new File(listaContactos.get(position).getAvatar());
        holder.avatar.setImageURI(Uri.fromFile(foto));

    }

    public void filtrado(final String txtBuscar) {
        int longitud = txtBuscar.length();
        if (longitud == 0) {
            listaContactos.clear();
            listaContactos.addAll(listaOriginal);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<Contactos> collecion = listaContactos.stream()
                        .filter(i -> i.getNombre().toLowerCase().contains(txtBuscar.toLowerCase()))
                        .collect(Collectors.toList());
                listaContactos.clear();
                listaContactos.addAll(collecion);
            } else {
                for (Contactos c : listaOriginal) {
                    if (c.getNombre().toLowerCase().contains(txtBuscar.toLowerCase())) {
                        listaContactos.add(c);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return listaContactos.size();
    }

    public class ContactoViewHolder extends RecyclerView.ViewHolder {

        TextView viewNombre, viewTelefono, viewPais;
        ImageView avatar;

        public ContactoViewHolder(@NonNull View itemView) {
            super(itemView);

            viewNombre = itemView.findViewById(R.id.viewNombre);
            viewTelefono = itemView.findViewById(R.id.viewTelefono);
            viewPais = itemView.findViewById(R.id.viewPais);
            avatar = itemView.findViewById(R.id.avatar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, VerActivity.class);
                    intent.putExtra("ID", listaContactos.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
