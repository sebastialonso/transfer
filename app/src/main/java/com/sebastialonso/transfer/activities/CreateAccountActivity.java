package com.sebastialonso.transfer.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.sebastialonso.transfer.R;
import com.sebastialonso.transfer.handlers.AccountHandler;
import com.sebastialonso.transfer.models.Account;

import org.w3c.dom.Text;

public class CreateAccountActivity extends AppCompatActivity {

    private String mSelectedBank;
    private String mSelectedTypeAccount;

    private static final String TAG = "CreateAccountActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTextView = (TextView) toolbar.findViewById(R.id.toolbar_textview);
        toolbarTextView.setText("Nueva cuenta");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        // Database handler
        final AccountHandler db = new AccountHandler(this);
        // Form elements
        // Owner name
        final EditText accountName = (EditText) findViewById(R.id.account_owner_name);
        // Owner rut
        final EditText accountRut = (EditText) findViewById(R.id.account_owner_rut);
        // Account number
        final EditText accountNumber = (EditText) findViewById(R.id.account_number);

        // Account Type
        Spinner typesSpinner = (Spinner) findViewById(R.id.account_type_spinner);
        ArrayAdapter<CharSequence> typesAdapter = ArrayAdapter.createFromResource(this,
                R.array.account_types, android.R.layout.simple_spinner_item);
        typesSpinner.setAdapter(typesAdapter);
        typesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedTypeAccount = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Bank spinner
        final Spinner banksSpinner = (Spinner) findViewById(R.id.banks_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.banks_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        banksSpinner.setAdapter(adapter);
        banksSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedBank = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Submit
        Button submitButton = (Button) findViewById(R.id.submit_account);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfo(v, accountName.getText().toString());
                Account accountToCreate = new Account(mSelectedBank, accountNumber.getText().toString(), accountName.getText().toString(), accountRut.getText().toString(), mSelectedTypeAccount);
                if (isValidAccount(accountToCreate)) {
                    accountToCreate.setAccountId();
                    db.addAccount(accountToCreate);
                    Log.d(TAG, "Cuenta creada");
                    showInfo(v, "Cuenta creada");
                    finishActivity();
                } else showInfo(v, "Necesitamos todos los campos");
            }
        });
    }

    private void showInfo(View v, String info) {
        Snackbar snackbar = Snackbar.make(v, info, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private boolean isValidAccount(Account account) {
        if (account.getAccountNumber().isEmpty() || account.getOwnerName().isEmpty()||
                account.getOwnerRut().isEmpty()) return false;
        return true;
    }

    private void finishActivity() {
        this.setResult(RESULT_OK);
        finish();
    }
}
