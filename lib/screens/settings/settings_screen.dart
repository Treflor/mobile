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
            Divider(),
          ],
        ),
      ),
    );
  }
}
