import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:http/http.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../config/treflor.dart';

enum AuthState {
  Loading,
  Authorized,
  Unauthorized,
  Error,
}

class OauthBLoC extends ChangeNotifier {
  SharedPreferences _treflorPref;
  AuthState _state = AuthState.Loading;
  String _token = '';

  OauthBLoC() {
    _load();
  }

  void _load() async {
    _treflorPref = await SharedPreferences.getInstance();
    if (_treflorPref != null) {
      var token = _treflorPref.getString(Treflor.JWT_TOKEN_KEY);
      if (token != null && token.isNotEmpty) {
        _jwtToken = token;
        _state = AuthState.Authorized;
      } else {
        _jwtToken = 'token';
        _state = AuthState.Unauthorized;
      }
    }
    notifyListeners();
  }

  String get jwtToken => _token;
  set _jwtToken(String token) {
    if (_treflorPref != null) {
      _token = token;
      _treflorPref.setString(Treflor.JWT_TOKEN_KEY, token);
    }

    if (token == '')
      _authState = AuthState.Unauthorized;
    else
      _authState = AuthState.Authorized;
    notifyListeners();
  }

  AuthState get authState => _state;
  set _authState(AuthState state) => _state = state;

  Future<bool> signIn(String email, String password) async {
    try {
      Response response = await post(
        OAuthAPIs.SIGNIN_API,
        body: {
          "email": email,
          "password": password,
        },
      );

      if (response.statusCode == 200)
        _jwtToken = jsonDecode(response.body)['token'];
      else
        _jwtToken = '';
      return true;
    } catch (error) {
      _authState = AuthState.Error;
      return true;
    }
  }

  void signOut() {
    _jwtToken = '';
  }
}
