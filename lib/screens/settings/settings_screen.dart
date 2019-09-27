import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:provider/provider.dart';
import 'package:treflor/state/config_state.dart';
import 'package:treflor/state/auth_state.dart';

class SettingsScreen extends StatefulWidget {
  @override
  _SettingsScreenState createState() => _SettingsScreenState();
}

class _SettingsScreenState extends State<SettingsScreen> {
  @override
  Widget build(BuildContext context) {
    AuthState authState = Provider.of<AuthState>(context);
    ConfigState configBLoC = Provider.of<ConfigState>(context);
    return Scaffold(
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
            ListTile(
            leading: Icon(Icons.account_circle),
            title: Text('General')
          ),
          ListTile(
            leading: Icon(Icons.notifications),
            title: Text('Notifications')
          ),
          ListTile(
            leading: Icon(Icons.lock),
            title: Text('Privacy')
          ),
          ListTile(
            leading: Icon(Icons.security),
            title: Text('Security')
          ),
          ListTile(
            leading: Icon(Icons.help),
            title: Text('Help')
          ),
          ListTile(
            leading: Icon(Icons.report),
            title: Text('About')
          ),
          // Padding(padding: EdgeInsets.only(top: 300.0),),
            Divider(),
            ListTile(
              leading: Icon(FontAwesomeIcons.signOutAlt),
              title: Text("Sign out"),
              onTap: () => authState.signout(),
            ),
          ],
        ),
      ),
    );
  }
}
