package com.mobdev.hellonetworking.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobdev.hellonetworking.R;
import com.mobdev.hellonetworking.network.HttpRequestManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Spinner httpLibrarySpinner = null;

    private Button sendHTTPRequestButton = null;

    private TextView resultTextView = null;

    private MutableLiveData<String> httpResponseLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        handleDataManagement();
    }

    private void initUI(){

        //Init HTTP Libraries Spinner Item
        httpLibrarySpinner = (Spinner)findViewById(R.id.httpLibrarySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.http_libraries_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        httpLibrarySpinner.setAdapter(adapter);

        sendHTTPRequestButton = (Button)findViewById(R.id.sendHTTPRequestButton);
        sendHTTPRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendHttpRequest();
            }
        });

        resultTextView = (TextView)findViewById(R.id.resultTextView);

    }

    private void handleDataManagement(){

        //Init Mutable Live Data
        httpResponseLiveData = new MutableLiveData<>();

        httpResponseLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String newHttpResponse) {
                Log.d(TAG, "New HTTP Response Received !");
                updateHttpResponseResult(newHttpResponse);
            }
        });
    }

    private void sendHttpRequest(){

        if(httpLibrarySpinner == null
                || httpLibrarySpinner.getSelectedItem() == null
                || httpLibrarySpinner.getSelectedItem().toString().length() == 0)
            Log.e(TAG, "Error retrieving HTTP Library ...");
        else {
            HttpRequestManager.scheduleHttpDummyRequest(httpLibrarySpinner.getSelectedItem().toString(), httpResponseLiveData);
        }
    }

    private void updateHttpResponseResult(String newHttpResultText){
        if(resultTextView != null)
            resultTextView.setText(newHttpResultText);
    }
}
