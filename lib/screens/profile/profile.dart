import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:treflor/state/auth_state.dart';

class ProfileScreen extends StatelessWidget {
  static const String route = '/profile';

  @override
  Widget build(BuildContext context) {
    AuthState authState = Provider.of<AuthState>(context);
    return Scaffold(
      appBar: AppBar(
        title: Text(authState.user.givenName + " " + authState.user.familyName),
      ),
      body: Column(
        children: <Widget>[
          Center(
            child: Hero(
              tag: "profile-pic",
              child: ClipOval(
                child: CachedNetworkImage(
                  imageUrl: authState.user.photoUrl,
                  width: MediaQuery.of(context).size.width * 0.6,
                  height: MediaQuery.of(context).size.width * 0.6,
                  fit: BoxFit.cover,
                ),
              ),
            ),
          ),
          Center(
            child: Text(
              authState.user.givenName + " " + authState.user.familyName,
              style: TextStyle(
                fontSize: 26,
                fontWeight: FontWeight.w400,
              ),
            ),
          )
        ],
      ),
    );
  }
}
