import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:treflor/models/auth_user.dart';
import 'package:treflor/data/repository.dart';
import 'package:treflor/data/remote/dto/auth_response.dart';
import 'package:treflor/models/user.dart';
import 'package:treflor/models/register_user.dart';

class AuthState extends ChangeNotifier {
  final Repository _respository = Repository();

  // state
  User _user;

  User get user => _user;

  AuthState() {
    _respository.initToken().then((isSuccess) {
      if (isSuccess) {
        _respository.usersInfoLocal().then((user) async {
          this._user = user;
          notifyListeners();
          await _respository.usersInfo().then((User user) {
            this._user = user;
            notifyListeners();
          }).catchError((err) {});
        });
      } else {
        this._user = null;
        notifyListeners();
      }
    }).catchError((err) {
      this._user = null;
      notifyListeners();
    });
  }

  Future<bool> signin(AuthUser user) {
    return _respository.login(user).then((response) => _loadUserData(response));
  }

  Future<bool> signup(RegisterUser user) {
    return _respository
        .signup(user)
        .then((response) => _loadUserData(response));
  }

  Future<bool> update(User user) {
    return _respository
        .update(user)
        .then((response) => _loadUserData(response));
  }

  Future<bool> signInWithGoogle() {
    GoogleSignIn _googleSignIn = GoogleSignIn(
      signInOption: SignInOption.standard,
      scopes: [
        'profile',
        'email',
        'openid',
      ],
    );
    return _googleSignIn.signIn().then((res) async {
      var auth = await res.authentication;
      return await _respository
          .loginWithGoogle(auth.accessToken)
          .then((response) => _loadUserData(response))
          .catchError((e) => false);
    });
  }

  Future<void> signout() {
    return _respository.logout().then((_) {
      _user = null;
      notifyListeners();
    });
  }

  Future<bool> _loadUserData(AuthResponse response) {
    print("Success: " + response.token);
    return _respository.usersInfo().then((User user) {
      this._user = user;
      notifyListeners();
      return true;
    });
  }
}
