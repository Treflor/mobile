import 'package:fluro/fluro.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'package:treflor/state/config_state.dart';
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
    ConfigState configState = Provider.of<ConfigState>(context);
    return MaterialApp(
      title: "Treflor",
      theme: ThemeData(
        brightness: configState.darkMode ? Brightness.dark : Brightness.light,
      ),
      debugShowCheckedModeBanner: false,
      onGenerateRoute: Application.router.generator,
    );
  }
}
