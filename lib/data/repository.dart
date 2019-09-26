import 'package:shared_preferences/shared_preferences.dart';
import 'package:treflor/models/auth_user.dart';
import 'package:treflor/data/remote/dto/login_response.dart';
import 'package:treflor/data/remote/treflor_api.dart';
import 'package:treflor/config/treflor.dart';
import 'package:treflor/data/local/entities/user_provider.dart';
import 'package:treflor/data/remote/treflor_api_impl.dart';
import 'package:treflor/models/user.dart';

class Repository {
  TreflorAPI _api;
  UserProvider _userProvider;

  String accessToken;

  Future<bool> initToken() {
    return SharedPreferences.getInstance().then((pref) {
      String token = pref.getString(Treflor.JWT_TOKEN_KEY);
      print("key");
      print(token);
      if (token != null) {
        accessToken = token;
        return true;
      } else {
        return false;
      }
    });
  }

  Future<LoginResponse> login(AuthUser user) {
    return _api.login(user).then((response) => _storeAccessToken(response));
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

  LoginResponse _storeAccessToken(LoginResponse response) {
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
