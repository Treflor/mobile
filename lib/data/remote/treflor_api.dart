import 'package:treflor/models/auth_user.dart';
import 'package:treflor/models/user.dart';
import 'package:treflor/data/remote/dto/login_response.dart';

abstract class TreflorAPI {
  Future<LoginResponse> login(AuthUser user);

  // profile
  Future<User> usersInfo(String token);
}
