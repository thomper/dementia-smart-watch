package com.team7.smartwatch.android;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import com.team7.smartwatch.shared.Utility;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.ParseException;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
	
	private enum LoginError {
		CONNECTION_REFUSED, PASSWORD_INCORRECT, OTHER
	}

	private static final String TAG = LoginActivity.class.getName();
	private static final String SUCCESS_MESSAGE = "Login successful\n";
	private static final String POST_URL = Globals.get().SERVER_ADDRESS
			+ "/login";

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
			mAuthTask.execute(Globals.get().httpContext);
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
	public class UserLoginTask extends AsyncTask<HttpContext, Void, LoginError> {

		private final String mUsername;
		private final String mPassword;

		UserLoginTask(String username, String password) {

			mUsername = username;
			mPassword = password;
		}

		/* Attempts to log in, returns null if successful, or the error
		 * encountered otherwise.
		 */
		@Override
		protected LoginError doInBackground(HttpContext... params) {

			// Create the request.
			HttpPost request = createRequest();
			if (request == null) {
				Log.e(TAG, "Error creating HTTP request");
				return LoginError.OTHER;
			}

			// Send the request.
			AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
			HttpContext httpContext = params[0];
			try {
				HttpResponse response = client.execute(request, httpContext);
				if (responseSucceeded(response)) {
					return null;
				} else {
					return LoginError.OTHER;
				}
			} catch (HttpHostConnectException e) {
				Log.d(TAG, "Connection refused:\n" + Utility.StringFromStackTrace(e));
				return LoginError.CONNECTION_REFUSED;
			} catch (IOException e) {
				Log.e(TAG, Utility.StringFromStackTrace(e));
				return LoginError.OTHER;
			} finally {
				client.close();
			}
		}

		@Override
		protected void onPostExecute(final LoginError error) {

			mAuthTask = null;
			showProgress(false);

			if (error == null) {
				Log.i(TAG, "Login succeeded");
				
				// TODO: Let user select which patient this device is for
				// TODO: Start MainActivity, passing session and patient details
				Intent intent = new Intent(LoginActivity.this,
						PatientSelectorActivity.class);
				startActivity(intent);
			} else {
				Log.i(TAG, "Login failed");
				setErrorMessage(error);
			}
		}

		@Override
		protected void onCancelled() {

			mAuthTask = null;
			showProgress(false);
		}
		
		private void setErrorMessage(LoginError error) {
			
			if (error == null) {
				return;
			}

			if (error == LoginError.PASSWORD_INCORRECT) {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
			} else if (error == LoginError.CONNECTION_REFUSED) {
				mPasswordView
						.setError(getString(R.string.error_connection_refused));
			} else if (error == LoginError.OTHER) {
				mPasswordView
						.setError(getString(R.string.error_unknown));
			}
			mPasswordView.requestFocus();
		}
		
		private HttpPost createRequest() {

			HttpPost request = AndroidUtility.createJSONHttpPost(POST_URL);
			
			// Get JSON.
			JSONObject jObj = createJSON();
			if (jObj == null) {
				return null;
			}

			// Add JSON to request.
			try {
				StringEntity se = new StringEntity(jObj.toString());
				request.setEntity(se);
				return request;
			} catch (UnsupportedEncodingException e) {
				Log.e(TAG, Utility.StringFromStackTrace(e));
	            return null;
			}
		}
		
		private JSONObject createJSON() {

			try {
				JSONObject jObj = new JSONObject();
				jObj.put("username", mUsername);
				jObj.put("password", mPassword);
				return jObj;
			} catch (JSONException e) {
				Log.e(TAG, Utility.StringFromStackTrace(e));
				return null;
			}
		}

		/* Return true if the login succeeded, false otherwise. */
		private boolean responseSucceeded(HttpResponse response) {

			try {
				String responseStr = AndroidUtility.StringFromHttpResponse(response);
				return responseStr.equals(SUCCESS_MESSAGE);
			} catch (ParseException e) {
				Log.e(TAG, Utility.StringFromStackTrace(e));
				return false;
			} catch (IOException e) {
				Log.e(TAG, Utility.StringFromStackTrace(e));
				return false;
			}
		}
	}
}