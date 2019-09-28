import 'package:treflor/models/auth_user.dart';
import 'package:treflor/models/user.dart';
import 'package:treflor/data/remote/dto/auth_response.dart';
import 'package:treflor/models/register_user.dart';
import 'package:treflor/data/remote/dto/tresponse.dart';

abstract class TreflorAPI {
  Future<AuthResponse> login(AuthUser user);
  Future<AuthResponse> loginWithGoogle(String token);
  Future<AuthResponse> signup(RegisterUser user);
  Future<TResponse> update(User user,String token);

  // profile
  Future<User> usersInfo(String token);
}
