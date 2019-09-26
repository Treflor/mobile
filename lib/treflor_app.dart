import 'package:fluro/fluro.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'package:treflor/state/config_bloc.dart';
import 'routes/application.dart';
import 'routes/routes.dart';

class TreflorApp extends StatelessWidget {
  TreflorApp() {
    final router = Router();
    Routes.configureRouter(router);
    Application.router = router;
  }

  @override
  Widget build(BuildContext context) {
    // ConfigBLoC configBLoC = Provider.of<ConfigBLoC>(context);
    return MaterialApp(
      title: "Treflor",
      theme: ThemeData(
        // brightness: configBLoC.darkMode ? Brightness.dark : Brightness.light,
        brightness: Brightness.dark,
      ),
      debugShowCheckedModeBanner: false,
      onGenerateRoute: Application.router.generator,
    );
  }
}
