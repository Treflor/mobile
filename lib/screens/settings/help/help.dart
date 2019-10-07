import 'package:flutter/material.dart';

class HelpScreen extends StatelessWidget {
  @override

  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Treflor"),
        //TODO default appbar
      ),
      body: Column(
        children: <Widget>[
          ListTile(
            contentPadding: EdgeInsets.fromLTRB(30.0, 20.0, 10.0, 5.0),
            title: Text(
              "How can we help you?",
              style: TextStyle(
                fontSize: 30.0,
              ),
            ),
          ),
          ListTile(
            leading: Icon(Icons.search),
            contentPadding: EdgeInsets.fromLTRB(30.0, 0, 10.0, 5.0),
            //TODO Search bar
          ),
          ListTile(
            contentPadding: EdgeInsets.fromLTRB(30.0, 0, 10.0, 5.0),
            title: Text(
              "FAQ",
              style: TextStyle(
                fontSize: 22.0
              ),
            ),
          ),
          //TODO add faq
        ],
      ),
    );
  }
}