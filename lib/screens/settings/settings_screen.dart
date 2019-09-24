import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:provider/provider.dart';
import 'package:treflor/bloc/config_bloc.dart';
import 'package:treflor/bloc/oauth_bloc.dart';
import 'package:treflor/screens/profile/profile.dart';

class SettingsScreen extends StatefulWidget {
  @override
  _SettingsScreenState createState() => _SettingsScreenState();
}

class _SettingsScreenState extends State<SettingsScreen> {
  @override
  Widget build(BuildContext context) {
    AuthBLoC authBLoC = Provider.of<AuthBLoC>(context);
    ConfigBLoC configBLoC = Provider.of<ConfigBLoC>(context);
    return Scaffold(
      appBar: AppBar(
        title: Text("Settings"),
        actions: <Widget>[
          InkWell(
            child: CircleAvatar(
              child: Hero(
                tag: "profile-pic",
                child: ClipOval(
                  child: CachedNetworkImage(
                    fit: BoxFit.cover,
                    imageUrl: authBLoC.user["photo"],
                  ),
                ),
              ),
            ),
            onTap: () => Navigator.pushNamed(context, ProfileScreen.route),
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
