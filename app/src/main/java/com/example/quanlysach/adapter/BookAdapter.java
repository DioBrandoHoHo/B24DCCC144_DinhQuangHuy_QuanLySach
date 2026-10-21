package com.example.quanlysach.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.quanlysach.R;
import com.example.quanlysach.model.Book;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    private Context context;
    private int resource;
    private List<Book> books;

    public BookAdapter(Context context, int resource, List<Book> books) {
        super(context, resource, books);
        this.context = context;
        this.resource = resource;
        this.books = books;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        Book book = books.get(position);

        TextView tvTitle = convertView.findViewById(R.id.tvBookTitle);
        TextView tvAuthor = convertView.findViewById(R.id.tvBookAuthor);
        TextView tvCategory = convertView.findViewById(R.id.tvBookCategory);
        TextView tvStatus = convertView.findViewById(R.id.tvBookStatus);
        RatingBar rbRating = convertView.findViewById(R.id.rbBookRating);

        if (book != null) {
            tvTitle.setText(book.getTitle());
            tvAuthor.setText("Tác giả: " + book.getAuthor());
            tvCategory.setText(book.getCategory());
            tvStatus.setText(book.getStatus());
            rbRating.setRating(book.getRating());
        }

        return convertView;
    }
}