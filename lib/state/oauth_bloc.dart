import 'dart:convert';

import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:treflor/config/treflor.dart';
import 'package:treflor/models/auth_user.dart';
import 'package:treflor/data/repository.dart';
import 'package:treflor/data/remote/dto/login_response.dart';
import 'package:treflor/models/user.dart';

class AuthBLoC extends ChangeNotifier {
  final Repository _respository = Repository();

  // state
  User _user;

  bool get isAuthenticated => _user != null && _user.email != null;
  bool get isLoading => _user != null && _user.email == null;
  User get user => _user;

  AuthBLoC() {
    _user = User(familyName: "", givenName: "", photoUrl: "");
    notifyListeners();
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

  Future<bool> _loadUserData(LoginResponse response) {
    print("Success: " + response.token);
    return _respository.usersInfo().then((User user) {
      this._user = user;
      notifyListeners();
      return response.success;
    });
  }

  Future<void> signout() {
    return _respository.logout().then((_) {
      _user = null;
      notifyListeners();
    });
  }

  // void signOut() => {
  //       _jwtToken = '',
  //     };

  // Future<void> signUp(FormData data) async {
  //   try {
  //     await _dio.post(OAuthAPIs.SIGNUP_API, data: data);
  //   } catch (error) {
  //     _authState = AuthState.Error;
  //   }
  // }

  // Future<void> googleSignIn() async {
  //   try {
  //     final GoogleSignIn _googleSignIn = GoogleSignIn();
  //     final GoogleSignInAccount googleUser = await _googleSignIn.signIn();
  //     final GoogleSignInAuthentication googleAuth =
  //         await googleUser.authentication;

  //     Response response = await _dio.post(
  //       OAuthAPIs.GOOGLE_SIGNIN_API,
  //       data: {
  //         "access_token": googleAuth.accessToken,
  //       },
  //     );

  //     if (response.statusCode == 200) {
  //       _jwtToken = response.data['token'];
  //     } else {
  //       _jwtToken = '';
  //     }
  //   } catch (error) {
  //     _authState = AuthState.Error;
  //   }
  // }
}
