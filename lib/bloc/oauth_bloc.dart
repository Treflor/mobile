import 'dart:convert';

import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:treflor/config/treflor.dart';

enum AuthState {
  Loading,
  Authorized,
  Unauthorized,
  Error,
}

class AuthBLoC extends ChangeNotifier {
  final SharedPreferences _treflorPref = Treflor.treflorPref;
  AuthState _state = AuthState.Loading;
  String _token = '';
  dynamic _user = {};
  Dio _dio = Dio();

  AuthBLoC() {
    if (_treflorPref != null) {
      var token = _treflorPref.getString(Treflor.JWT_TOKEN_KEY);
      var userString = _treflorPref.getString(Treflor.USER_KEY);
      print("User String");
      print(userString);
      if (token != null && token.isNotEmpty) {
        _jwtToken = token;
        _state = AuthState.Authorized;
      } else {
        _jwtToken = '';
        _state = AuthState.Unauthorized;
      }
      if (userString != null && userString.isNotEmpty) {
        print("ondecode");
        print(json.decode(userString));
        user = json.decode(userString);
      } else {
        user = {};
      }
    }
  }

  String get jwtToken => _token;
  set _jwtToken(String token) {
    if (_treflorPref != null) {
      _token = token;
      _treflorPref.setString(Treflor.JWT_TOKEN_KEY, token);
    }

    if (token == '') {
      _authState = AuthState.Unauthorized;
    } else {
      _authState = AuthState.Authorized;
    }
    notifyListeners();
  }

  dynamic get user => _user;
  set user(dynamic user) {
    print("on save");
    print(user);
    if (_treflorPref != null) {
      _user = user;
      _treflorPref.setString(Treflor.USER_KEY, json.encode(user));
      print("on pred");
      print(_treflorPref.getString(Treflor.USER_KEY));
    }
    notifyListeners();
  }

  AuthState get authState => _state;
  set _authState(AuthState state) => _state = state;

  Future<void> signIn(String email, String password) async {
    try {
      Response response = await _dio.post(
        OAuthAPIs.SIGNIN_API,
        data: {
          "email": email,
          "password": password,
        },
      );

      if (response.statusCode == 200) {
        _jwtToken = response.data['token'];
        _loadUser();
      } else {
        _jwtToken = '';
      }
      return true;
    } catch (error) {
      _authState = AuthState.Error;
    }
  }

  void signOut() => {
        _jwtToken = '',
        user = {},
      };

  Future<void> signUp(FormData data) async {
    try {
      await _dio.post(OAuthAPIs.SIGNUP_API, data: data);
    } catch (error) {
      _authState = AuthState.Error;
    }
  }

  Future<void> googleSignIn() async {
    try {
      final GoogleSignIn _googleSignIn = GoogleSignIn();
      final GoogleSignInAccount googleUser = await _googleSignIn.signIn();
      final GoogleSignInAuthentication googleAuth =
          await googleUser.authentication;

      Response response = await _dio.post(
        OAuthAPIs.GOOGLE_SIGNIN_API,
        data: {
          "access_token": googleAuth.accessToken,
        },
      );

      if (response.statusCode == 200) {
        _jwtToken = response.data['token'];
        _loadUser();
      } else {
        _jwtToken = '';
      }
    } catch (error) {
      _authState = AuthState.Error;
    }
  }

  Future<void> _loadUser() async {
    var response = await _dio.get(UserAPI.USER_API,
        options: Options(headers: {"Authorization": jwtToken}));

    user = response.data['user'];
  }
}
