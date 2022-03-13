package com.abe.android.floorcodes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abe.android.floorcodes.adapters.FilterListAdapter;
import com.abe.android.floorcodes.models.StackingFilter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    AutoCompleteTextView mBarcodeContent;
    ImageView mCodeImage;
    AppDatabase database;
    //AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = AppDatabase.getAppDatabase(getApplicationContext());
        mBarcodeContent = findViewById(R.id.barcodeContent);
        mCodeImage = findViewById(R.id.codeImage);

        List<StackingFilter> f = new ArrayList<>();
        FilterListAdapter adapter = new FilterListAdapter(this,R.layout.autocomplete_dropdown
                ,f);

        mBarcodeContent.setAdapter(adapter);
        mBarcodeContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                //... your stuff
                Log.println(Log.DEBUG,"sdfsd","dfgdf");
              //  mBarcodeContent.setText(parent.getItemAtPosition(position).toString());

               // mBarcodeContent.setImeOptions(EditorInfo.IME_ACTION_DONE);
            }

        });

        //autoCompleteTextView = findViewById(R.id.barcodeContent);
        /*
        mBarcodeContent.addTextChangedListener(new TextWatcher() {

            private static final int TOTAL_SYMBOLS = 7; // A-00.0A size of pattern
            private static final int TOTAL_DIGITS = 5; // max numbers of digits in pattern: 0000 x 4
            private static final int DIVIDER_MODULO = 2; // means divider position is every 5th symbol beginning with 1
            private static final int DIVIDER_POSITION = DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
            private static final char DIVIDER = '-';

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // noop
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // noop
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                    s.replace(0, s.length(), buildCorrectString(getDigitArray(s, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER));
                }
            }

            private boolean isInputCorrect(Editable s, int totalSymbols, int dividerModulo, char divider) {
                boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
                for (int i = 0; i < s.length(); i++) { // check that every element is right
                    if (i > 0 && (i + 1) % dividerModulo == 0) {
                        isCorrect &= divider == s.charAt(i);
                    } else {
                        isCorrect &= Character.isDigit(s.charAt(i));
                    }
                }
                return true;
            }

            private String buildCorrectString(char[] digits, int dividerPosition, char divider) {
                final StringBuilder formatted = new StringBuilder();

                for (int i = 0; i < digits.length; i++) {
                    if (digits[i] != 0) {
                        formatted.append(digits[i]);
                        if ((i > 0) && (i < (digits.length - 1)) && (i==1)) {
                            formatted.append(divider);
                        }
                    }
                }

                return formatted.toString();
            }

            private char[] getDigitArray(final Editable s, final int size) {
                char[] digits = new char[size];
                int index = 0;
                for (int i = 0; i < s.length() && index < size; i++) {
                    char current = s.charAt(i);
                    if (Character.isDigit(current)) {
                        digits[index] = current;
                        index++;
                    }
                }
                return digits;
            }
        });
*/


        mBarcodeContent.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ) {
                    //do here your stuff f
                    InputMethodManager in = (InputMethodManager) getSystemService(v.getContext().INPUT_METHOD_SERVICE);
                    //in.perfo
                    in.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

                    generateBarcode();
                    return true;
                }
                return false;
            }
        });

        List<StackingFilter> s = null;
        try{s = readData();}
        catch (IOException e){}
        database.myDao().insertAll(s);

    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
    private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Toast.makeText(MainActivity.this,
                            "Clicked item from auto completion list "
                                    + adapterView.getItemAtPosition(i).toString()
                            , Toast.LENGTH_SHORT).show();
                }
            };

    private List<StackingFilter> readData() throws IOException
    {
        List<StackingFilter> stackingFilters = new ArrayList<>();
        InputStream is = getResources().openRawResource(R.raw.floor);
        InputStreamReader csvStreamReader = new InputStreamReader(is);
        CSVReader reader = new CSVReader(csvStreamReader);

        //read line by line
        String[] record = null;
        String first, second;

        while((record = reader.readNext())!= null)
        {
            first = record[0];
            second = record[1];

            stackingFilters.add(new StackingFilter(first,second));
        }

        return stackingFilters;
    }


    private void generateBarcode()
    {
        String barcodeContent;
        StackingFilter s =  database.myDao().findCode(mBarcodeContent.getText().toString());//.content;
        if(s == null)
        {
            Toast.makeText(this,"Empty",Toast.LENGTH_LONG).show();
        }
        else {
            barcodeContent = s.content;
            QRCodeWriter writer = new QRCodeWriter();
            try {
                BitMatrix bitMatrix = writer.encode(barcodeContent, BarcodeFormat.QR_CODE, 512, 512);
                int width = bitMatrix.getWidth();
                int height = bitMatrix.getHeight();
                Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {

                        bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.TRANSPARENT);
                    }
                }

                mCodeImage.setImageBitmap(bmp);
            } catch (WriterException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    protected void onDestroy() {
        database.cleanUp();
        super.onDestroy();
    }
}
