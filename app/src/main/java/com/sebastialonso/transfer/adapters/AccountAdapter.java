package com.sebastialonso.transfer.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sebastialonso.transfer.MainActivity;
import com.sebastialonso.transfer.handlers.AccountHandler;
import com.sebastialonso.transfer.interfaces.ItemClickListener;
import com.sebastialonso.transfer.R;
import com.sebastialonso.transfer.models.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seba on 30-07-16.
 */
public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

    private List<Account> mUserList;
    private Context mContext;
    private static MainActivity mMainActivity;
    private static AccountHandler db;
    private static final String TAG = "AccountAdapter";

    public static int firstLongClickPosition = -1;
    private List<Account> selectedUsers;

    public AccountAdapter(List<Account> userList, Context context) {
        this.mUserList = userList;
        this.mContext = context;
        mMainActivity = (MainActivity) context;
        db = new AccountHandler(mContext);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private CardView mCardView;
        private TextView mAccountNumber;
        private TextView mAccountType;
        private TextView mBank;
        private TextView mOwnerName;
        private TextView mOwnerRut;
        private CheckBox mAccountSelected;
        private ItemClickListener clickListener; // Interface object

        public ViewHolder(View view) {
            super(view);
            mCardView = (CardView) view.findViewById(R.id.account_cardview);
            mAccountNumber = (TextView) view.findViewById(R.id.card_account_number);
            mAccountType = (TextView) view.findViewById(R.id.card_account_type);
            mBank = (TextView) view.findViewById(R.id.card_account_bank);
            mOwnerName = (TextView) view.findViewById(R.id.card_account_owner_name);
            mOwnerRut = (TextView) view.findViewById(R.id.card_account_owner_rut);
            mAccountSelected = (CheckBox) view.findViewById(R.id.card_account_checkbox);

            // CardView level listeners.
            mAccountSelected.setOnClickListener(this);
            mCardView.setOnLongClickListener(this);

        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onClick(v, getAdapterPosition(), true);
            return true;
        }
    }

    @Override
    public int getItemCount(){
        return mUserList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Account account = mUserList.get(position);
        viewHolder.mAccountNumber.setText(account.getAccountNumber());
        viewHolder.mAccountType.setText(account.getAccountType());
        viewHolder.mOwnerRut.setText(account.getOwnerRut());
        viewHolder.mOwnerName.setText(account.getOwnerName());
        viewHolder.mBank.setText(account.getBankName());
        if (!mMainActivity.isInActionMode) {
            viewHolder.mAccountSelected.setVisibility(View.GONE);
        } else {
            viewHolder.mAccountSelected.setVisibility(View.VISIBLE);
            if (firstLongClickPosition == position) viewHolder.mAccountSelected.setChecked(true);
            else viewHolder.mAccountSelected.setChecked(false);
        }

        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    handleViewLongClick(view, position);
                } else {
                    Log.d(TAG, "handling click");
                    handleClick(view, position);
                }
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.account_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    public void deleteAccounts() {
        mUserList.removeAll(selectedUsers);
        for (Account remove : selectedUsers) {
            db.removeAccount(remove.getAccountId());
        }
        selectedUsers.clear();
    }

    public void addAccount(Account account) {
        mUserList.add(account);
        notifyItemInserted(mUserList.size() - 1);
    }

    public String getSelectedToolbarText() {
        int counter = selectedUsers.size();
        if (counter == 1) return counter + " cuenta seleccionada";
        else return counter + " cuentas seleccionadas";
    }

    public void handleClick(View view, int position) {
        Log.d(TAG, "me estoy llamando");
        // position is according to adapter, meaning usersList position
        if (((CheckBox) view).isChecked()) {
            selectedUsers.add(mUserList.get(position));
        } else {
            selectedUsers.remove(mUserList.get(position));
        }
        mMainActivity.updateToolbarText(getSelectedToolbarText());
    }
    public void handleViewLongClick(View view, int position) {
        if (mMainActivity.isInActionMode) {
            Log.d(TAG, "long click again, but I aint doing no shit");
        } else {
            mMainActivity.enterActionMode();
            firstLongClickPosition = position;
            Log.d(TAG, "checked position" + position);
            selectedUsers = new ArrayList<Account>();
            selectedUsers.add(mUserList.get(position));
            mMainActivity.updateToolbarText(getSelectedToolbarText());
            notifyDataSetChanged();
        }
    }
}
