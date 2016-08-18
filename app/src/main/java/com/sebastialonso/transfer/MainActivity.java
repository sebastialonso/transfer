package com.sebastialonso.transfer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sebastialonso.transfer.activities.CreateAccountActivity;
import com.sebastialonso.transfer.adapters.AccountAdapter;
import com.sebastialonso.transfer.handlers.AccountHandler;
import com.sebastialonso.transfer.models.Account;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int CREATE_ACCOUNT_REQUEST = 1;
    private static final String TAG = "MainActivity";

    private Context mContext;
    private RecyclerView mRecyclerView;
    private AccountAdapter mAccountAdapter;
    private List<Account> mAccounts;
    public AccountHandler db;
    private Toolbar mToolbar;

    public boolean isInActionMode = false;
    public int firstLongClickPressed = -1;
    TextView toolbarTextView;
    List<Account> mSelected = new ArrayList<Account>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        toolbarTextView = (TextView) findViewById(R.id.toolbar_textview);
        toolbarTextView.setVisibility(View.GONE);

        mContext = this;

        // Database
        db = new AccountHandler(this);
        int countRecords = db.countRecords();
        Log.d(TAG, "Numero de cuentas en BD: " + String.valueOf(countRecords));
        mAccounts = db.getAllAccounts();

        // RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.accounts_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAccountAdapter = new AccountAdapter(mAccounts, MainActivity.this);
        mRecyclerView.setAdapter(mAccountAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check if returning from creating account
        if (requestCode == CREATE_ACCOUNT_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Get the last object
                Account latest = db.last();
                mAccountAdapter.addAccount(latest);
            } else {
                Log.d(TAG, "\nCanceled. Dont reload db");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    public boolean enterActionMode() {
        // Set actionMode flags
        isInActionMode = true;

        // Clean menu on toolbar
        showActionModeToolbar();

        mAccountAdapter.notifyDataSetChanged(); // Show checkboxes
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return true;
    }

    public void updateToolbarText(String updatedText) {
        Log.d(TAG, updatedText);
        toolbarTextView.setText(updatedText);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_item_delete_account || item.getItemId() == android.R.id.home) {
            if (item.getItemId() == R.id.toolbar_item_delete_account) mAccountAdapter.deleteAccounts();
            clearActionMode();
            mAccountAdapter.notifyDataSetChanged();
        } else if (item.getItemId() == R.id.toolbar_item_add_account) {
            // New activity
            Intent createAccountIntent = new Intent(getApplicationContext(), CreateAccountActivity.class);
            startActivityForResult(createAccountIntent, CREATE_ACCOUNT_REQUEST);
        }
        else Log.d(TAG, "otro boton " + String.valueOf(item.getItemId()) + " " + item.getTitle());
        return super.onOptionsItemSelected(item);

    }

    public void clearActionMode() {
        isInActionMode = false;
        // Render correct toolbar
        hideActionModeToolbar();
    }

    // Handle back button behaviour
    @Override
    public void onBackPressed() {
        if (isInActionMode) {
            clearActionMode();
            mAccountAdapter.notifyDataSetChanged();
        } else {
            super.onBackPressed();
        }
    }

    public void showActionModeToolbar() {
        mToolbar.getMenu().clear();
        mToolbar.inflateMenu(R.menu.menu_action_mode);
        toolbarTextView.setVisibility(View.VISIBLE);
    }

    public void hideActionModeToolbar() {
        mToolbar.getMenu().clear();
        mToolbar.inflateMenu(R.menu.menu_activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbarTextView.setVisibility(View.GONE);
        toolbarTextView.setText("0 cuentas seleccionadas");
    }


}
