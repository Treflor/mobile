import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:provider/provider.dart';
import 'package:treflor/bloc/config_bloc.dart';
import 'package:treflor/bloc/oauth_bloc.dart';
import 'package:treflor/bloc/user_bloc.dart';

class SettingsScreen extends StatefulWidget {
  @override
  _SettingsScreenState createState() => _SettingsScreenState();
}

class _SettingsScreenState extends State<SettingsScreen> {
  @override
  Widget build(BuildContext context) {
    AuthBLoC authBLoC = Provider.of<AuthBLoC>(context);
    ConfigBLoC configBLoC = Provider.of<ConfigBLoC>(context);
    UserBLoC userBLoC = Provider.of<UserBLoC>(context);
    print(userBLoC.user);
    return Scaffold(
      appBar: AppBar(
        title: Text("Settings"),
        actions: <Widget>[
          CircleAvatar(
            // child: ClipOval(
            //   child: CachedNetworkImage(
            //     fit: BoxFit.cover,
            //     imageUrl: authBLoC.user["photo"],
            //   ),
            // ),
          ),
        ],
      ),
      body: Container(
        child: ListView(
          children: <Widget>[
            ListTile(
              leading: Icon(
                configBLoC.darkMode
                    ? FontAwesomeIcons.solidLightbulb
                    : FontAwesomeIcons.lightbulb,
              ),
              title: Text("Dark Mode"),
              onTap: () => configBLoC.toggleDarkMode(),
            ),
            Divider(),
            ListTile(
              leading: Icon(FontAwesomeIcons.signOutAlt),
              title: Text("Sign out"),
              onTap: () => authBLoC.signOut(),
            ),
          ],
        ),
      ),
    );
  }
}
