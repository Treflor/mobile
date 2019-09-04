/* 
  there must me a file treflor.dart in /lib/config/ location.
  structure should look like below
*/

// from treflor backend
class Treflor {
  static const String BASE_URL = "https://api-treflor.herokuapp.com";

// shared prefeences keys
  static const String JWT_TOKEN_KEY = "jwt-token";
}

class OAuthAPIs {
  static const String SIGNIN_API = Treflor.BASE_URL + "/oauth/signin";
  static const String GOOGLE_SIGNIN_API = Treflor.BASE_URL + "/oauth/google";
}
