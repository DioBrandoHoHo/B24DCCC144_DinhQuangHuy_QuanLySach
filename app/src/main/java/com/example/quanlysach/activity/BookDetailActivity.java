package com.example.quanlysach.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlysach.R;
import com.example.quanlysach.model.Book;

public class BookDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvAuthorCategory, tvStatus, tvNote;
    private RatingBar rbRating;
    private Button btnMarkAsRead, btnBack, btnDelete;

    private Book currentBook;
    private int bookPosition = -1;
    private boolean isUpdated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        initViews();
        getDataFromIntent();
        displayBookData();
        setupListeners();
        setupBackPressHandler();
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tvDetailTitle);
        tvAuthorCategory = findViewById(R.id.tvDetailAuthorCategory);
        tvStatus = findViewById(R.id.tvDetailStatus);
        tvNote = findViewById(R.id.tvDetailNote);
        rbRating = findViewById(R.id.rbDetailRating);
        btnMarkAsRead = findViewById(R.id.btnMarkAsRead);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDeleteBook);
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            currentBook = (Book) intent.getSerializableExtra("BOOK");
            bookPosition = intent.getIntExtra("POSITION", -1);
        }
    }

    private void displayBookData() {
        if (currentBook != null) {
            tvTitle.setText(currentBook.getTitle());
            tvAuthorCategory.setText(currentBook.getAuthor() + " • " + currentBook.getCategory());
            tvStatus.setText(currentBook.getStatus());
            rbRating.setRating(currentBook.getRating());

            if (currentBook.getNote() != null && !currentBook.getNote().isEmpty()) {
                tvNote.setText(currentBook.getNote());
            } else {
                tvNote.setText("Không có ghi chú.");
            }

            if ("Đã đọc".equals(currentBook.getStatus())) {
                btnMarkAsRead.setEnabled(false);
                btnMarkAsRead.setAlpha(0.5f);
            }
        }
    }

    private void setupListeners() {
        rbRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser && currentBook != null) {
                currentBook.setRating(rating);
                isUpdated = true;
            }
        });

        btnMarkAsRead.setOnClickListener(v -> {
            if (currentBook != null) {
                currentBook.setStatus("Đã đọc");
                tvStatus.setText("Đã đọc");
                btnMarkAsRead.setEnabled(false);
                btnMarkAsRead.setAlpha(0.5f);
                isUpdated = true;
                Toast.makeText(this, "Đã đánh dấu là Đã đọc", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> {
            if (isUpdated) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("ACTION", "UPDATE");
                resultIntent.putExtra("POSITION", bookPosition);
                resultIntent.putExtra("UPDATED_BOOK", currentBook);
                setResult(RESULT_OK, resultIntent);
            }
            finish();
        });

        btnDelete.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("ACTION", "DELETE");
            resultIntent.putExtra("POSITION", bookPosition);
            setResult(RESULT_OK, resultIntent);
            Toast.makeText(this, "Đã xóa sách", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void setupBackPressHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isUpdated) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("ACTION", "UPDATE");
                    resultIntent.putExtra("POSITION", bookPosition);
                    resultIntent.putExtra("UPDATED_BOOK", currentBook);
                    setResult(RESULT_OK, resultIntent);
                }
                finish();
            }
        });
    }
}