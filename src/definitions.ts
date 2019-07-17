declare global {
  interface PluginRegistry {
    LSFirebasePhoneAuth: LSFirebasePhoneAuthPlugin;
  }
}

export interface LSFirebasePhoneAuthPlugin {
  // echo(options: { value: string }): Promise<{value: string}>;
  verifyPhoneNumber(options: {phoneNumber: string}): Promise<{verificationId: string}>;
  trySignInWithCode(options: {verificationId: string, code: string}): Promise<any>;
}
