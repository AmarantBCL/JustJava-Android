package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static int COFFEE_PRICE = 5;
    private static int WHIPPED_CREAM_PRICE = 1;
    private static int CHOCOLATE_PRICE = 2;
    private static int MIN_COFFEE = 1;
    private static int MAX_COFFEE = 100;
    private int quantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void submitOrder(View view) {
        if (quantity <= 0) {
            Toast toast = Toast.makeText(this, getString(R.string.make_order), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        EditText editBox = findViewById(R.id.name_edit);
        CheckBox creamBox = findViewById(R.id.cream_checkbox);
        CheckBox chocoBox = findViewById(R.id.chocolate_checkbox);
        String name = editBox.getText().toString();
        if (name.isEmpty()) {
            Toast toast = Toast.makeText(this, getString(R.string.give_name), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        boolean hasWhippedCream = creamBox.isChecked();
        boolean hasChocolate = chocoBox.isChecked();
        int price = calculatePrice(hasWhippedCream, hasChocolate);
        emailIntent(getString(R.string.order_info, name),
                createOrderSummary(name, price, hasWhippedCream, hasChocolate));
    }

    private void emailIntent(String subject, String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"vadamarant@gmail.com", "Manager.assistant@obzhora.org"});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void increment(View view) {
        if (quantity + 1 > MAX_COFFEE) {
            Toast toast = Toast.makeText(this, getString(R.string.order_more),
                    Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    public void decrement(View view) {
        if (quantity - 1 < MIN_COFFEE) {
            Toast toast = Toast.makeText(this, getString(R.string.order_less),
                    Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    private void displayQuantity(int number) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    private String createOrderSummary(String name, int price, boolean hasWhippedCream, boolean hasChocolate) {
        return String.format(getString(R.string.order_summary_name, name) +
                        "\n" + getString(R.string.order_summary_cream) + " %s" +
                        "\n" + getString(R.string.order_summary_chocolate) + " %s" +
                        "\n" + getString(R.string.order_summary_quantity, quantity) +
                        "\n" + getString(R.string.order_summary_total, price) +
                        "\n" + getString(R.string.thank_you),
                hasWhippedCream, hasChocolate);
    }

    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        int whippedCreamPrice = hasWhippedCream ? WHIPPED_CREAM_PRICE : 0;
        int chocolatePrice = hasChocolate ? CHOCOLATE_PRICE : 0;
        return (COFFEE_PRICE + whippedCreamPrice + chocolatePrice) * quantity;
    }
}