import 'package:shared_preferences/shared_preferences.dart';
import 'package:treflor/models/auth_user.dart';
import 'package:treflor/data/remote/dto/auth_response.dart';
import 'package:treflor/data/remote/treflor_api.dart';
import 'package:treflor/config/treflor.dart';
import 'package:treflor/data/local/entities/user_provider.dart';
import 'package:treflor/data/remote/treflor_api_impl.dart';
import 'package:treflor/models/user.dart';
import 'package:treflor/models/register_user.dart';

class Repository {
  TreflorAPI _api;
  UserProvider _userProvider;

  String accessToken;

  Future<bool> initToken() {
    return SharedPreferences.getInstance().then((pref) {
      String token = pref.getString(Treflor.JWT_TOKEN_KEY);
      if (token != null) {
        accessToken = token;
        return true;
      } else {
        return false;
      }
    });
  }

  Future<bool> getDarkMode() {
    return SharedPreferences.getInstance().then((pref) {
      bool darkMode = pref.getBool(Treflor.DARK_MODE_KEY);
      return darkMode ?? false;
    });
  }

  Future<bool> toggleDarkMode(bool currentMode) {
    return SharedPreferences.getInstance().then((pref) {
      pref.setBool(Treflor.DARK_MODE_KEY, !currentMode);
      return !currentMode;
    });
  }

  Future<AuthResponse> login(AuthUser user) {
    return _api.login(user).then((response) => _storeAccessToken(response));
  }

  Future<AuthResponse> loginWithGoogle(String accessToken) {
    return _api
        .loginWithGoogle(accessToken)
        .then((response) => _storeAccessToken(response));
  }

  Future<AuthResponse> signup(RegisterUser user) {
    return _api.signup(user).then((response) {
      return login(user.toAuthUser());
    });
  }

  Future<AuthResponse> update(User user) {
    return _api.update(user, accessToken).then((response) {
      return AuthResponse({"token": accessToken});
    });
  }

  Future<void> logout() {
    this.accessToken = null;
    return SharedPreferences.getInstance().then((pref) async {
      pref.setString(Treflor.JWT_TOKEN_KEY, null);
      await _userProvider.delete();
    });
  }

  Future<User> usersInfo() {
    if (accessToken == null) return Future(null);
    return _api.usersInfo(accessToken).then((user) async {
      await _userProvider.insert(user);
      return user;
    });
  }

  Future<User> usersInfoLocal() {
    return _userProvider.getUser();
  }

  AuthResponse _storeAccessToken(AuthResponse response) {
    this.accessToken = response.token;
    SharedPreferences.getInstance().then((pref) {
      pref.setString(Treflor.JWT_TOKEN_KEY, response.token);
    });
    return response;
  }

  // Singleton
  static final Repository _repository = Repository._internal();

  factory Repository() {
    return _repository;
  }

  Repository._internal()
      : _api = TreflorAPIImpl(),
        _userProvider = UserProvider();
}
