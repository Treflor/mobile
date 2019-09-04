import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:provider/provider.dart';
import 'package:treflor/bloc/oauth_bloc.dart';

class MainScreen extends StatelessWidget {
  static const String route = '/main';
  @override
  Widget build(BuildContext context) {
    AuthBLoC oauthBLoC = Provider.of<AuthBLoC>(context);
    return Scaffold(
      appBar: AppBar(
        title: Text("Main Screen"),
        actions: <Widget>[
          IconButton(
            icon: Icon(FontAwesomeIcons.signOutAlt),
            onPressed: () => oauthBLoC.signOut(),
          )
        ],
      ),
      body: Container(),
    );
  }
}
