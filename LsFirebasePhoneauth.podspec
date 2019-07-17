
  Pod::Spec.new do |s|
    s.name = 'LsFirebasePhoneauth'
    s.version = '0.0.1'
    s.summary = 'A plugin that just verifies the phone number'
    s.license = 'MIT'
    s.homepage = 'https://github.com/leonschloemmer/FirebasePhoneAuth'
    s.author = 'Leon Schloemmer'
    s.source = { :git => 'https://github.com/leonschloemmer/FirebasePhoneAuth', :tag => s.version.to_s }
    s.source_files = 'ios/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}'
    s.ios.deployment_target  = '11.0'
    s.dependency 'Capacitor'
    s.dependency 'Firebase'
    s.dependency 'Firebase/Core'
    s.dependency 'Firebase/Auth'
    s.static_framework = true
  end