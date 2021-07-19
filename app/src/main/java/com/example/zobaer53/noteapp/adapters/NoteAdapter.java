package com.example.zobaer53.noteapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zobaer53.noteapp.R;
import com.example.zobaer53.noteapp.roomdb.Note;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter <NoteAdapter.BeanHolder>{

    private List<Note> list;
    private Context context;
     private LayoutInflater layoutInflater;
     private OnNoteItemClick onNoteItemClick;

    public NoteAdapter(List<Note> list, Context context) {
        layoutInflater =LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        this.onNoteItemClick = (OnNoteItemClick) context;
    }

public interface OnNoteItemClick{

        void onNoteClick(int pos);

}

    @NonNull
    @Override
    public NoteAdapter.BeanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.note_list_item,parent,false);

        return new BeanHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  NoteAdapter.BeanHolder holder, int position) {
         holder.textViewtitle.setText(list.get(position).getTitle());
         holder.textViewContent.setText(list.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // BeanHolder Class (ViewHolder)
    public class BeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textViewContent;
        TextView textViewtitle;

        public BeanHolder(@NonNull  View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textViewContent = itemView.findViewById(R.id.textViewContent );
            textViewtitle = itemView.findViewById(R.id.textViewTitle);

        }

        @Override
        public void onClick(View v) {
          onNoteItemClick.onNoteClick(getAdapterPosition());
        }
    }
}
