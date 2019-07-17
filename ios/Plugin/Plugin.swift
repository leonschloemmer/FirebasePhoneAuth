import Foundation
import Capacitor

import FirebaseCore
import FirebaseAuth

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitor.ionicframework.com/docs/plugins/ios
 */
@objc(LSFirebasePhoneAuth)
public class LSFirebasePhoneAuth: CAPPlugin {
    
    @objc func verifyPhoneNumber(_ call: CAPPluginCall) {
		if (call.hasOption("phoneNumber")) {
			let phonenumber = call.getString("phoneNumber") ?? ""
			
			PhoneAuthProvider.provider().verifyPhoneNumber(phonenumber, uiDelegate: nil) { (verificationId, error) in
				if let error = error {
					call.error(error.localizedDescription)
					return
				}
				
				call.success([
					"verificationId": verificationId as Any
				])
			}
		}
	}
	
	@objc func trySignInWithCode(_ call: CAPPluginCall) {
		if call.hasOption("verificationId") && call.hasOption("code") {
			let verificationId = call.getString("verificationId") ?? ""
			let code = call.getString("code") ?? ""
			
			let credential = PhoneAuthProvider.provider().credential(withVerificationID: verificationId, verificationCode: code)
			
			Auth.auth().signIn(with: credential) { (authResult, error) in
				if let error = error {
					call.error(error.localizedDescription)
					return
				}
				
				call.success()
			}
		}
	}
}
