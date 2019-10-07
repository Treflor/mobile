import 'package:fluro/fluro.dart';
import 'package:treflor/routes/route_handler.dart';

class Routes {
  static const main = "/";
  static const login = "/login";
  static const signup = "/signup";
  static const profile = "/profile";
  static const updateRest = "/update/rest";
  static const about = "/about";
  static const help = "/help";

  static void configureRouter(Router router) {
    router.define(main, handler: mainHandler);
    router.define(login,
        handler: loginHandler, transitionType: TransitionType.fadeIn);
    router.define(profile,
        handler: profileHandler, transitionType: TransitionType.fadeIn);
    router.define(signup,
        handler: signupHandler, transitionType: TransitionType.fadeIn);
    router.define(updateRest,
        handler: updateRestHandler, transitionType: TransitionType.inFromRight);
    router.define(about,
        handler: aboutHandler, transitionType: TransitionType.fadeIn);
    router.define(help,
        handler: helpHandler, transitionType: TransitionType.fadeIn);
  }
}
