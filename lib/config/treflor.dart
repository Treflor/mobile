import 'config.dart';

class Treflor {
  static const String BASE_URL = API_BASE_URL;

// shared prefeences keys
  static const String JWT_TOKEN_KEY = "jwt-token";
}

class OAuthAPIs {
  static const String SIGNIN_API = Treflor.BASE_URL + "/oauth/signin";
  static const String SIGNUP_API = Treflor.BASE_URL + "/oauth/signup";
  static const String GOOGLE_SIGNIN_API = Treflor.BASE_URL + "/oauth/google";
}
