import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:provider/provider.dart';
import 'package:treflor/screens/camera/camera_screen.dart';
import 'package:treflor/screens/home/home_screen.dart';
import 'package:treflor/screens/route/route_screen.dart';
import 'package:treflor/screens/settings/settings_screen.dart';
import 'package:treflor/screens/start/start_screen.dart';
import 'package:treflor/state/auth_state.dart';
import 'package:treflor/routes/application.dart';
import 'package:treflor/state/config_state.dart';

class MainScreen extends StatefulWidget {
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

  final _screens = [
    HomeScreen(),
    StartScreen(),
    CameraScreen(),
    RouteScreen(),
    SettingsScreen(),
  ];

  @override
  Widget build(BuildContext context) {
    ConfigState configState = Provider.of<ConfigState>(context);
    AuthState authState = Provider.of<AuthState>(context);
    return Scaffold(
      appBar: AppBar(
        title: Text("Treflor"),
        actions: <Widget>[
          CircleAvatar(
            child: InkWell(
              borderRadius: BorderRadius.circular(50),
              child: Hero(
                tag: "profile-pic",
                child: ClipOval(
                  child: authState.user != null
                      ? CachedNetworkImage(
                          placeholder: (context, str) {
                            print(str);
                            return Text("A");
                          },
                          fit: BoxFit.cover,
                          imageUrl: authState.user.photoUrl,
                        )
                      : Image.asset(
                          "assets/images/profile.jpg",
                          fit: BoxFit.cover,
                        ),
                ),
              ),
              onTap: () => authState.user != null
                  ? _goToLogin(context, "/profile")
                  : _goToLogin(context, "/login"),
            ),
          ),
        ],
      ),
      body: Container(
        child: _screens[_selectedIndex],
      ),
      bottomNavigationBar: BottomNavigationBar(
        backgroundColor: configState.darkMode ? Colors.blueGrey : Colors.grey,
        selectedItemColor:
            configState.darkMode ? Colors.white : Colors.blueGrey,
        unselectedItemColor: configState.darkMode ? Colors.grey : Colors.white,
        currentIndex: _selectedIndex,
        onTap: _onItemTapped,
        type: BottomNavigationBarType.fixed,
        showUnselectedLabels: false,
        items: [
          BottomNavigationBarItem(
            title: Text('Home'),
            icon: Icon(FontAwesomeIcons.home),
          ),
          BottomNavigationBarItem(
            title: Text('Start'),
            icon: Icon(FontAwesomeIcons.plus),
          ),
          BottomNavigationBarItem(
            title: Text('Camera'),
            icon: Icon(FontAwesomeIcons.camera),
          ),
          BottomNavigationBarItem(
            title: Text('Routes'),
            icon: Icon(FontAwesomeIcons.route),
          ),
          BottomNavigationBarItem(
            title: Text('Settings'),
            icon: Icon(FontAwesomeIcons.cog),
          ),
        ],
      ),
    );
  }

  void _goToLogin(BuildContext context, uri) {
    Application.router.navigateTo(context, uri);
  }
}
