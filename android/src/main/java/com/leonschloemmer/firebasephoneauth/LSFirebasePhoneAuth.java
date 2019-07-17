package com.leonschloemmer.firebasephoneauth;

import android.support.annotation.NonNull;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONArray;

import java.util.concurrent.TimeUnit;

@NativePlugin()
public class LSFirebasePhoneAuth extends Plugin {

//    @PluginMethod()
//    public void verifyPhoneNumber(PluginCall call) {
////        String value = call.getString("value");
////
////        JSObject ret = new JSObject();
////        ret.put("value", value);
////        call.success(ret);
//    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            JSObject ret = new JSObject();
            ret.put("success", true);
            notifyListeners("verificationCompleted", ret);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            JSObject ret = new JSObject();
            ret.put("success", false);
            ret.put("message", e.getLocalizedMessage());
            notifyListeners("verificationCompleted", ret);
        }

        @Override
        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            JSObject ret = new JSObject();
            ret.put("verificationId", verificationId);
            notifyListeners("codeSent", ret);
        }
    };

    @PluginMethod()
    public void verifyPhoneNumber(PluginCall call) {
        if (call.getData().has("phoneNumber")) {
            String phone = call.getString("phoneNumber");

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phone,
                    0,
                    TimeUnit.SECONDS,
                    getActivity(),
                    callbacks
            );

            call.success();
        }
        call.reject("No phone number provided");
    }

    @PluginMethod
    public void trySignInWithCode(PluginCall call) {
        if (call.getData().has("verificationId")
                && call.getData().has("code")) {
            String verificationId = call.getString("verificationId");
            String code = call.getString("code");

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

            FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                JSObject ret = new JSObject();
                                ret.put("success", true);
                                notifyListeners("verificationCompleted", ret);
                            } else {
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    JSObject ret = new JSObject();
                                    ret.put("success", false);
                                    ret.put("message", "wrong-code");
                                    notifyListeners("verificationCompleted", ret);
                                } else {
                                    JSObject ret = new JSObject();
                                    ret.put("success", false);
                                    ret.put("message", task.getException().getLocalizedMessage());
                                    notifyListeners("verificationCompleted", ret);
                                }
                            }
                        }
                    });
        }
    }
}
