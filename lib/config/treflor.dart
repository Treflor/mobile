import 'package:shared_preferences/shared_preferences.dart';

import 'config.dart';

class Treflor {
  static const String BASE_URL = API_BASE_URL;

  static SharedPreferences treflorPref;

// shared prefeences keys
  static const String JWT_TOKEN_KEY = "jwt-token";

  static const String DARK_MODE_KEY = "dark-mode";
}

class OAuthAPIs {
  static const String SIGNIN_API = Treflor.BASE_URL + "/oauth/signin";
  static const String SIGNUP_API = Treflor.BASE_URL + "/oauth/signup";
  static const String GOOGLE_SIGNIN_API = Treflor.BASE_URL + "/oauth/google";
}
