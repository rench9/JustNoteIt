package com.r4hu7.justnoteit.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.r4hu7.justnoteit.R;
import com.r4hu7.justnoteit.data.model.Note;
import com.r4hu7.justnoteit.databinding.AdapterNoteItemBinding;
import com.r4hu7.justnoteit.utils.NoteNavigator;

import java.lang.ref.WeakReference;
import java.util.List;

public class NoteItemAdapter extends RecyclerView.Adapter<NoteItemAdapter.ViewHolder> {
    private List<Note> items;
    NoteNavigator navigator;

    public NoteItemAdapter(List<Note> items, NoteNavigator noteNavigator) {
        this.items = items;
        this.navigator = noteNavigator;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        AdapterNoteItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.adapter_note_item, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.binding.setNote(items.get(i));
        viewHolder.binding.setNavigator(new WeakReference<>(navigator));
    }

    public void setItems(List<Note> notes) {
        this.items = notes;
        notifyDataSetChanged();
    }

    public void addItem(Note note) {
        this.items.add(note);
        notifyItemInserted(getItemCount() - 1);
    }

    public void addItems(List<Note> notes) {
        this.items.addAll(notes);
        notifyItemRangeInserted(getItemCount() - notes.size(), notes.size());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        AdapterNoteItemBinding binding;

        ViewHolder(@NonNull AdapterNoteItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
