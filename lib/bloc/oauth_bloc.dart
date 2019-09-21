import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:http/http.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../config/treflor.dart';

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

  AuthBLoC() {
    if (_treflorPref != null) {
      var token = _treflorPref.getString(Treflor.JWT_TOKEN_KEY);
      if (token != null && token.isNotEmpty) {
        _jwtToken = token;
        _state = AuthState.Authorized;
      } else {
        _jwtToken = '';
        _state = AuthState.Unauthorized;
      }
    }
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

  Future<void> signIn(String email, String password) async {
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
    }
  }

  void signOut() {
    _jwtToken = '';
  }

  Future<void> signUp(String email, String password, String image,
      String familyName, String givenName) async {
    try {
      Response response = await post(
        OAuthAPIs.SIGNUP_API,
        body: {
          "email": email,
          "password": password,
          "family_name": familyName,
          "given_name": givenName,
          "photo": 'null',
        },
      );

      if (response.statusCode == 200)
        _jwtToken = jsonDecode(response.body)['token'];
      else
        _jwtToken = '';
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

      Response response = await post(
        OAuthAPIs.GOOGLE_SIGNIN_API,
        body: {
          "access_token": googleAuth.accessToken,
        },
      );

      if (response.statusCode == 200)
        _jwtToken = jsonDecode(response.body)['token'];
      else
        _jwtToken = '';
    } catch (error) {
      _authState = AuthState.Error;
    }
  }
}
