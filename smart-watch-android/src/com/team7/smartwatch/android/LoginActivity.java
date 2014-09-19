package com.team7.smartwatch.android;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.team7.smartwatch.shared.Utility;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends Activity {

	private static final String TAG = LoginActivity.class.getName();
	private static final String SERVER_ADDRESS = "http://192.168.1.41:8080";
	private static final String POST_URL = SERVER_ADDRESS + "/login";
	private static final String SUCCESS_MESSAGE = "Login successful\n";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// UI references.
	private EditText mUsernameView;
	private EditText mPasswordView;
	private View mProgressView;
	private View mLoginFormView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// Set up the login form.
		mUsernameView = (EditText) findViewById(R.id.username);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
		mSignInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});

		mLoginFormView = findViewById(R.id.login_form);
		mProgressView = findViewById(R.id.login_progress);
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid username, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mUsernameView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		String username = mUsernameView.getText().toString();
		String password = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password if the user entered one.
		if (TextUtils.isEmpty(password)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (!isPasswordValid(password)) {
			mPasswordView.setError(getString(R.string.error_password_too_short));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid username.
		if (TextUtils.isEmpty(username)) {
			mUsernameView.setError(getString(R.string.error_field_required));
			focusView = mUsernameView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true);
			mAuthTask = new UserLoginTask(username, password);
			mAuthTask.execute((Void) null);
		}
	}

	private boolean isPasswordValid(String password) {
		// TODO: Replace this with your own logic
		return password.length() > 4;
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});

			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mProgressView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mProgressView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

		private final String mUsername;
		private final String mPassword;

		UserLoginTask(String username, String password) {
			mUsername = username;
			mPassword = password;
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			// Create the request.
			List<Pair<String, Object>> json = new ArrayList<Pair<String, Object>>();
			json.add(Pair.create("username", (Object)mUsername));
			json.add(Pair.create("password", (Object)mPassword));
			HttpPost request = createRequestFromJSON(json, POST_URL);

			// Send the request.
			AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
			try {
				HttpResponse response = client.execute(request);
				return responseSucceeded(response);
			} catch (IOException e) {
				Log.e(TAG, Utility.StringFromStackTrace(e));
				return false;
			} finally {
				client.close();
			}
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				// TODO: Let user select which patient this device is for
				// TODO: Keep track of session
				Log.i(TAG, "Login succeeded");
				// TODO: Start MainActivity, passing session and patient details
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				intent.putExtra("SERVER_ADDRESS", SERVER_ADDRESS);
				startActivity(intent);
			} else {
				Log.i(TAG, "Login failed");
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
		
		/* Return true if the login succeeded, false otherwise. */
		private boolean responseSucceeded(HttpResponse response) {
			try {
				String responseStr = EntityUtils.toString(response.getEntity());
				return responseStr.equals(SUCCESS_MESSAGE);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
	}
	
	private HttpPost createRequestFromJSON(List<Pair<String, Object>> values,
			String postUrl) {

		// Create request and set header.
		HttpPost request = new HttpPost(postUrl);
        request.setHeader("Accept", "application/json");
		request.setHeader("Content-type", "application/json");
		
		// Add JSON values.
		JSONObject jObj = new JSONObject();
		try {
			for (Pair<String, Object> pair : values) {
				jObj.put(pair.first, pair.second);
			}
			StringEntity se = new StringEntity(jObj.toString());
			request.setEntity(se);
			return request;
		} catch (JSONException e) {
			Log.e(TAG, Utility.StringFromStackTrace(e));
            return null;
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, Utility.StringFromStackTrace(e));
            return null;
		}
	}
}
