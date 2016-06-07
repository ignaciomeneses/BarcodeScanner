package com.phonegap.plugins.barcodescanner;

import android.content.Intent;
import android.util.Log;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class BarcodeScanner extends CordovaPlugin {

    private static final String SCAN = "scan";
    private static final String CANCELLED = "cancelled";
    private static final String FORMAT = "format";
    private static final String TEXT = "text";

    private static final String LOG_TAG = "BarcodeReader";

    private CallbackContext callbackContext;

	 /**
     * Constructor.
     */
    public BarcodeScanner() {
    }
	
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		this.callbackContext = callbackContext;
        if (action.equals(SCAN)) {
            scan();
            return true;
        }else{
			return false;
		}
    }
	
	/**
     * Starts an intent to scan and decode a barcode.
     */
    public void scan() {
		IntentIntegrator integrator = new IntentIntegrator(null);
        integrator.setBeepEnabled(false);
        integrator.setOrientationLocked(true);
        integrator.initiateScan(this);
    }
	
	/**
     * Called when the barcode scanner intent completes.
     *
     * @param requestCode The request code originally supplied to startActivityForResult(),
     *                       allowing you to identify who this result came from.
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param intent      An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode,resultCode,intent);
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,intent);
		if(result!=null && result.getContents() != null){
			JSONObject obj = new JSONObject();
			try {
				obj.put(TEXT, result.getContents());
				obj.put(FORMAT, result.getFormatName());
				obj.put(CANCELLED, false);
			} catch (JSONException e) {
				Log.d(LOG_TAG, "This should never happen");
			}
			//this.success(new PluginResult(PluginResult.Status.OK, obj), this.callback);
			this.callbackContext.success(obj);
        }else{
			JSONObject obj = new JSONObject();
			try {
				obj.put(TEXT, "");
				obj.put(FORMAT, "");
				obj.put(CANCELLED, true);
			} catch (JSONException e) {
				Log.d(LOG_TAG, "This should never happen");
			}
			//this.success(new PluginResult(PluginResult.Status.OK, obj), this.callback);
			this.callbackContext.success(obj);
        }
    }
	
}