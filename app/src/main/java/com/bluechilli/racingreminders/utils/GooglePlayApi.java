package com.bluechilli.racingreminders.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class GooglePlayApi {
	
	
    public boolean servicesConnected(Activity activity){
    	
    	int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
    	
    	if (resultCode == ConnectionResult.SUCCESS){
    		return true;
    	}
    	else{
    		
    		Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, activity, 9000);
    		
    		if (errorDialog != null) {
    			ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(activity.getFragmentManager(), "Google Play");
            }
    		
    		return false;
    	}
    }
    
    public static class ErrorDialogFragment extends DialogFragment {
        private Dialog mDialog;

        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
}
