/* 
  there must me a file treflor.dart in /lib/config/ location.
  structure should look like below
*/

// from treflor backend
class Treflor {
  static const String BASE_URL = "https://api-treflor.herokuapp.com";
}

// from traflor backend
class OAuthAPIs {
  static const String SIGNIN_API = Treflor.BASE_URL + "/oauth/signin";
}
