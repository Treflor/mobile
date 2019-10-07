import 'package:flutter/material.dart';

class AboutScreen extends StatelessWidget {
  @override

  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("About"),
        //TODO Add defualt appbar
      ),
      body: Column(
        children: <Widget>[
          ListTile(
            contentPadding: EdgeInsets.fromLTRB(30.0, 10.0, 10.0, 5.0),
            title: Text(
              "Version",
              style: TextStyle(
                fontSize: 15.0
              ),
            ),
            subtitle: Text("v1.0.0"),
          ),
          ListTile(
            contentPadding: EdgeInsets.fromLTRB(30.0, 5.0, 10.0, 5.0),
            title: Text(
              "User ID",
              style: TextStyle(
                fontSize: 15.0
              ),
            ),
            subtitle: Text("672732283"),
          ),
          ListTile(
            contentPadding: EdgeInsets.fromLTRB(30.0, 5.0, 10.0, 5.0),
            title: Text(
              "Debug ID",
              style: TextStyle(
                fontSize: 15.0
              ),
            ),
            subtitle: Text("d3247234e-r3r223"),
          ),
          ListTile(
            contentPadding: EdgeInsets.fromLTRB(30.0, 5.0, 10.0, 5.0),
            title: Text(
              "What's new",
              style: TextStyle(
                fontSize: 15.0
              ),
            ),
          ),
          ListTile(
            contentPadding: EdgeInsets.fromLTRB(30.0, 5.0, 10.0, 5.0),
            title: Text(
              "Rate on Google Play",
              style: TextStyle(
                fontSize: 15.0
              ),
            ),
          ),
          ListTile(
            contentPadding: EdgeInsets.fromLTRB(30.0, 5.0, 10.0, 5.0),
            title: Text(
              "Send feedback",
              style: TextStyle(
                fontSize: 15.0
              ),
            ),
          ),
          ListTile(
            contentPadding: EdgeInsets.fromLTRB(30.0, 5.0, 10.0, 5.0),
            title: Text(
              "Terms of Service",
              style: TextStyle(
                fontSize: 15.0
              ),
            ),
          ),
          ListTile(
            contentPadding: EdgeInsets.fromLTRB(30.0, 5.0, 10.0, 5.0),
            title: Text(
              "Community Standards",
              style: TextStyle(
                fontSize: 15.0
              ),
            ),
          ),
          ListTile(
            contentPadding: EdgeInsets.fromLTRB(30.0, 5.0, 10.0, 5.0),
            title: Text(
              "Data Policy",
              style: TextStyle(
                fontSize: 15.0
              ),
            ),
          ),
        ],
      ),
    );
  }
}