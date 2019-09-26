import 'package:fluro/fluro.dart';
import 'package:treflor/routes/route_handler.dart';

class Routes {
  static const main = "/";
  static const login = "/login";
  static const signup = "/signup";
  static const profile = "/profile";

  static void configureRouter(Router router) {
    router.define(main, handler: mainHandler);
    router.define(login,
        handler: loginHandler, transitionType: TransitionType.fadeIn);
    router.define(profile,
        handler: profileHandler, transitionType: TransitionType.fadeIn);
  }
}
