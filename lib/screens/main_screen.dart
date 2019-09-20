import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:provider/provider.dart';
import 'package:treflor/bloc/oauth_bloc.dart';

class MainScreen extends StatefulWidget {
  static const String route = '/main';

  @override
  _MainScreenState createState() => _MainScreenState();
}

class _MainScreenState extends State<MainScreen> {
var _selectedIndex = 0;

  void _onItemTapped(int index) {
    setState(() {
      _selectedIndex = index;
    });
  }

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
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: _selectedIndex,
        onTap: _onItemTapped,
        type: BottomNavigationBarType.shifting,
        items: [
          BottomNavigationBarItem(
            title: Text('Home'),
            icon: Icon(FontAwesomeIcons.home),
            backgroundColor: Colors.blueGrey,
          ),
          BottomNavigationBarItem(
            title: Text('Start'),
            icon: Icon(FontAwesomeIcons.plus),
            backgroundColor: Colors.amberAccent,
          ),
          BottomNavigationBarItem(
            title: Text('Camera'),
            icon: Icon(FontAwesomeIcons.camera),
            backgroundColor: Colors.greenAccent,
          ),
          BottomNavigationBarItem(
            title: Text('Routes'),
            icon: Icon(FontAwesomeIcons.route),
            backgroundColor: Colors.cyanAccent,
          ),
          BottomNavigationBarItem(
            title: Text('Settings'),
            icon: Icon(FontAwesomeIcons.cog),
            backgroundColor: Colors.redAccent,
          ),
        ],
      ),
    );
  }
}
