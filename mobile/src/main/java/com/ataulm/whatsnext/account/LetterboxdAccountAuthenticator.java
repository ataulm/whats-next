package com.ataulm.whatsnext.account;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.ataulm.support.Clock;
import com.ataulm.whatsnext.Token;
import com.ataulm.whatsnext.TokensStore;
import com.ataulm.whatsnext.api.Letterboxd;

import java.io.IOException;

class LetterboxdAccountAuthenticator extends SimpleAbstractAccountAuthenticator {

    private static final Uri AUTH_URI = Uri.parse("https://api-v2launch.trakt.tv/oauth/authorize");

    private final Context context;
    private final TokensStore tokensStore;
    private final Clock clock;
    private final Letterboxd letterboxd;

    LetterboxdAccountAuthenticator(Context context, Letterboxd letterboxd) {
        super(context);
        this.context = context.getApplicationContext();
        this.tokensStore = TokensStore.create(context.getApplicationContext());
        this.letterboxd = letterboxd;
        this.clock = new Clock();
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        Bundle result = new Bundle();
        result.putParcelable(
                AccountManager.KEY_INTENT,
                new Intent(context, SignInActivity.class)
                        .putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
                        .setData(AUTH_URI)
        );
        return result;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        Token token = tokensStore.getToken();
        if (token == null) {
            return bundleThatWillPromptUserLogin(response);
        }

        if (needToRefreshAccessToken(token)) {
            Token refreshedToken = refreshAuthTokenBundle(token);
            tokensStore.store(refreshedToken);
            return authTokenBundleFrom(account, refreshedToken);
        } else {
            return authTokenBundleFrom(account, token);
        }
    }

    private Bundle bundleThatWillPromptUserLogin(AccountAuthenticatorResponse response) {
        Bundle result = new Bundle();
        result.putParcelable(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        result.putParcelable(
                AccountManager.KEY_INTENT,
                new Intent(context, SignInActivity.class)
                        .putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
                        .setData(AUTH_URI)
        );
        return result;
    }

    private boolean needToRefreshAccessToken(Token token) {
        return token.getExpiryMillisSinceEpoch() > clock.getCurrentTimeMillis();
    }

    private Token refreshAuthTokenBundle(Token token) throws NetworkErrorException {
        try {
            return letterboxd.refreshAccessToken(token.getRefreshToken());
        } catch (IOException e) {
            throw new NetworkErrorException(e);
        }
    }

    private Bundle authTokenBundleFrom(Account account, Token token) {
        Bundle bundle = new Bundle();
        bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
        bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
        bundle.putString(AccountManager.KEY_AUTHTOKEN, token.getAccessToken());
        return bundle;
    }
}
