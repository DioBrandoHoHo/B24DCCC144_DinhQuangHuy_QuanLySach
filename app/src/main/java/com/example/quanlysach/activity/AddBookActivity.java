package com.example.quanlysach.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlysach.R;
import com.example.quanlysach.model.Book;

import java.util.UUID;

public class AddBookActivity extends AppCompatActivity {

    private EditText etTitle, etAuthor, etNote;
    private Spinner spCategory;
    private RadioGroup rgStatus;
    private RadioButton rbUnread, rbReading, rbRead;
    private RatingBar rbRating;
    private TextView tvRatingValue;
    private Button btnSave;

    private String[] categories = {"Văn học", "Khoa học", "Công nghệ", "Kỹ năng sống", "Khác"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        initViews();
        setupSpinner();
        setupRatingBar();

        btnSave.setOnClickListener(v -> saveBook());
    }

    private void initViews() {
        etTitle = findViewById(R.id.etAddTitle);
        etAuthor = findViewById(R.id.etAddAuthor);
        etNote = findViewById(R.id.etAddNote);
        spCategory = findViewById(R.id.spAddCategory);
        rgStatus = findViewById(R.id.rgAddStatus);
        rbUnread = findViewById(R.id.rbUnread);
        rbReading = findViewById(R.id.rbReading);
        rbRead = findViewById(R.id.rbRead);
        rbRating = findViewById(R.id.rbAddRating);
        tvRatingValue = findViewById(R.id.tvRatingValue);
        btnSave = findViewById(R.id.btnSaveBook);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);
    }

    private void setupRatingBar() {
        rbRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) ->
                tvRatingValue.setText((int) rating + " / 5 sao")
        );
    }

    private void saveBook() {
        String title = etTitle.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();
        String category = spCategory.getSelectedItem().toString();
        String note = etNote.getText().toString().trim();
        float rating = rbRating.getRating();

        if (title.isEmpty()) {
            etTitle.setError("Tên sách không được để trống");
            etTitle.requestFocus();
            return;
        }

        if (author.isEmpty()) {
            etAuthor.setError("Tác giả không được để trống");
            etAuthor.requestFocus();
            return;
        }

        String status = "Chưa đọc";
        int checkedId = rgStatus.getCheckedRadioButtonId();
        if (checkedId == R.id.rbReading) {
            status = "Đang đọc";
        } else if (checkedId == R.id.rbRead) {
            status = "Đã đọc";
        }

        String id = UUID.randomUUID().toString();
        Book newBook = new Book(id, title, author, category, status, rating, note);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("NEW_BOOK", newBook);
        setResult(RESULT_OK, resultIntent);
        Toast.makeText(this, "Thêm sách thành công", Toast.LENGTH_SHORT).show();
        finish();
    }
}