import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:treflor/state/oauth_bloc.dart';

class ProfileScreen extends StatelessWidget {
  static const String route = '/profile';

  @override
  Widget build(BuildContext context) {
    AuthBLoC authBLoC = Provider.of<AuthBLoC>(context);
    return Scaffold(
      appBar: AppBar(
        title: Text(authBLoC.user.givenName + " " + authBLoC.user.familyName),
      ),
      body: Column(
        children: <Widget>[
          Center(
            child: Hero(
              tag: "profile-pic",
              child: ClipOval(
                child: CachedNetworkImage(
                  imageUrl: authBLoC.user.photoUrl,
                  width: MediaQuery.of(context).size.width * 0.6,
                  height: MediaQuery.of(context).size.width * 0.6,
                  fit: BoxFit.cover,
                ),
              ),
            ),
          ),
          Center(
            child: Text(
              authBLoC.user.givenName + " " + authBLoC.user.familyName,
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
