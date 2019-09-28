import 'dart:io';

import 'package:dio/dio.dart';

import 'package:treflor/models/auth_user.dart';
import 'package:treflor/data/remote/dto/auth_response.dart';
import 'package:treflor/data/remote/treflor_api.dart';
import 'package:treflor/config/config.dart';
import 'package:treflor/models/register_user.dart';
import 'package:treflor/models/user.dart';

class TreflorAPIImpl extends TreflorAPI {
  Dio _dio;

  TreflorAPIImpl() : _dio = Dio();

  // BASE_URL
  static const String BASE_URL = API_BASE_URL;

  static const String API_URL = BASE_URL + "";

  static const ENDPOINT_LOGIN = "/oauth/signin";
  static const ENDPOINT_SIGNUP = "/oauth/signup";
  static const ENDPOINT_LOGIN_WITH_GOOGLE = "/oauth/google";
  static const ENDPOINT_USER_INFO = "/user/info";

  @override
  Future<AuthResponse> login(AuthUser user) {
    return _dio
        .post(API_URL + ENDPOINT_LOGIN, data: user.toMap())
        .then((response) => AuthResponse(response.data));
  }

  @override
  Future<User> usersInfo(String token) {
    Options options = Options(
      headers: {HttpHeaders.authorizationHeader: token},
    );
    return _dio
        .get(API_URL + ENDPOINT_USER_INFO, options: options)
        .then((response) {
      return User.fromJson(response.data['user']);
    });
  }

  @override
  Future<AuthResponse> loginWithGoogle(String token) {
    return _dio.post(API_URL + ENDPOINT_LOGIN_WITH_GOOGLE,
        data: {"access_token": token}).then((response) {
      return AuthResponse(response.data);
    }).catchError((err) {
      print(err);
    });
  }

  @override
  Future<AuthResponse> signup(RegisterUser user) {
    return _dio
        .post(API_URL + ENDPOINT_SIGNUP, data: user.toMap())
        .then((response) {
      return AuthResponse(response.data);
    }).catchError((err) {
      throw Exception(err.toString());
    });
  }
}
