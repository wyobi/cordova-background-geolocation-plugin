package com.marianhello.bgloc.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

/**
 * Created by finch on 19/07/16.
 */
public class AccountHelper {
    private static final String TAG = "AccountHelper";

    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static android.accounts.Account CreateSyncAccount(Context context, String accountName, String accountType) {
        Account account = new Account(accountName, accountType);
        // Get an instance of the Android account manager
        AccountManager accountManager =  (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data.
         * addAccountExplicitly can throw SecurityException ("uid N cannot
         * explicitly add accounts of type ...") on first install (authenticator
         * not yet registered) or after a signing-key change leaves the account
         * type owned by a differently-signed prior install. The sync account is
         * best-effort batching for background location, so never let it crash the
         * app — it self-heals on a later start once ownership/indexing settles.
         */
        try {
            accountManager.addAccountExplicitly(account, null, null);
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } catch (SecurityException e) {
            Log.w(TAG, "CreateSyncAccount: addAccountExplicitly failed, continuing without sync account", e);
        }
        return account;
    }
}
