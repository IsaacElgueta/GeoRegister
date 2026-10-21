package com.lira.georegister;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IncidenciaAdapter extends RecyclerView.Adapter<IncidenciaAdapter.ViewHolder> {

    private final List<DocumentSnapshot> incidencias = new ArrayList<>();

    public void setIncidencias(List<DocumentSnapshot> nuevasIncidencias) {
        incidencias.clear();
        incidencias.addAll(nuevasIncidencias);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_incidencia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DocumentSnapshot incidencia = incidencias.get(position);
        holder.tvTitulo.setText(valorTexto(incidencia, "titulo", "Sin título"));
        holder.tvDescripcion.setText(valorTexto(incidencia, "descripcion", "Sin descripción"));
        holder.tvCategoria.setText(holder.itemView.getContext().getString(
                R.string.historial_categoria,
                valorTexto(incidencia, "categoria", "Sin categoría")
        ));
        holder.tvFecha.setText(holder.itemView.getContext().getString(
                R.string.historial_fecha,
                valorTexto(incidencia, "fecha", "Sin fecha")
        ));

        Double latitud = incidencia.getDouble("latitud");
        Double longitud = incidencia.getDouble("longitud");
        String ubicacion = latitud != null && longitud != null
                ? String.format(Locale.getDefault(), "%.6f, %.6f", latitud, longitud)
                : "Sin ubicación";
        holder.tvUbicacion.setText(holder.itemView.getContext().getString(
                R.string.historial_ubicacion,
                ubicacion
        ));
    }

    @Override
    public int getItemCount() {
        return incidencias.size();
    }

    private String valorTexto(DocumentSnapshot documento, String campo, String valorPorDefecto) {
        String valor = documento.getString(campo);
        return valor == null || valor.trim().isEmpty() ? valorPorDefecto : valor;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitulo;
        final TextView tvDescripcion;
        final TextView tvCategoria;
        final TextView tvFecha;
        final TextView tvUbicacion;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvItemTitulo);
            tvDescripcion = itemView.findViewById(R.id.tvItemDescripcion);
            tvCategoria = itemView.findViewById(R.id.tvItemCategoria);
            tvFecha = itemView.findViewById(R.id.tvItemFecha);
            tvUbicacion = itemView.findViewById(R.id.tvItemUbicacion);
        }
    }
}
