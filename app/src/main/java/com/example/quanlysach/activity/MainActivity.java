package com.example.quanlysach.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlysach.R;
import com.example.quanlysach.adapter.BookAdapter;
import com.example.quanlysach.model.Book;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView tvTotalBooks, tvReadBooks, tvUnreadBooks;
    private ListView lvBooks;
    private Button btnAddBook;

    private ArrayList<Book> bookList;
    private BookAdapter adapter;

    private ActivityResultLauncher<Intent> addBookLauncher;
    private ActivityResultLauncher<Intent> detailBookLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initData();
        setupAdapter();
        setupLaunchers();
        setupListeners();
        updateStatistics();
    }

    private void initViews() {
        tvTotalBooks = findViewById(R.id.tvTotalBooks);
        tvReadBooks = findViewById(R.id.tvReadBooks);
        tvUnreadBooks = findViewById(R.id.tvUnreadBooks);
        lvBooks = findViewById(R.id.lvBooks);
        btnAddBook = findViewById(R.id.btnAddBook);
    }

    private void initData() {
        bookList = new ArrayList<>();
        bookList.add(new Book("1", "Lập trình Android cơ bản", "Nguyễn Văn A", "Công nghệ", "Đang đọc", 4.0f, "Sách dành cho người mới bắt đầu"));
        bookList.add(new Book("2", "Đắc nhân tâm", "Dale Carnegie", "Kỹ năng sống", "Đã đọc", 5.0f, "Sách rất hay về giao tiếp"));
        bookList.add(new Book("3", "Dế Mèn phiêu lưu ký", "Tô Hoài", "Văn học", "Chưa đọc", 0.0f, "Truyện thiếu nhi kinh điển"));
    }

    private void setupAdapter() {
        adapter = new BookAdapter(this, R.layout.item_book, bookList);
        lvBooks.setAdapter(adapter);
    }

    private void updateStatistics() {
        int total = bookList.size();
        int read = 0;
        int unread = 0;

        for (Book book : bookList) {
            if ("Đã đọc".equals(book.getStatus())) {
                read++;
            } else {
                unread++;
            }
        }

        tvTotalBooks.setText(String.valueOf(total));
        tvReadBooks.setText(String.valueOf(read));
        tvUnreadBooks.setText(String.valueOf(unread));
    }

    private void setupLaunchers() {
        addBookLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Book newBook = (Book) result.getData().getSerializableExtra("NEW_BOOK");
                        if (newBook != null) {
                            bookList.add(newBook);
                            adapter.notifyDataSetChanged();
                            updateStatistics();
                        }
                    }
                }
        );

        detailBookLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        String action = data.getStringExtra("ACTION");
                        int position = data.getIntExtra("POSITION", -1);

                        if (position != -1) {
                            if ("DELETE".equals(action)) {
                                bookList.remove(position);
                            } else if ("UPDATE".equals(action)) {
                                Book updatedBook = (Book) data.getSerializableExtra("UPDATED_BOOK");
                                if (updatedBook != null) {
                                    bookList.set(position, updatedBook);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            updateStatistics();
                        }
                    }
                }
        );
    }

    private void setupListeners() {
        btnAddBook.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
            addBookLauncher.launch(intent);
        });

        lvBooks.setOnItemClickListener((parent, view, position, id) -> {
            Book selectedBook = bookList.get(position);
            Intent intent = new Intent(MainActivity.this, BookDetailActivity.class);
            intent.putExtra("BOOK", selectedBook);
            intent.putExtra("POSITION", position);
            detailBookLauncher.launch(intent);
        });
    }
}