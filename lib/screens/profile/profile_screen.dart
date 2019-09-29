import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';
import 'package:treflor/models/user.dart';
import 'index.dart';

class ProfileScreen extends StatelessWidget {
  final formatter = DateFormat("dd - MMMM - yyyy");

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Profile"),
      ),
      body: BlocBuilder<ProfileBloc, ProfileState>(
        bloc: Provider.of<ProfileBloc>(context),
        builder: (context, state) {
          if (state is NoProfileState) {
            return Container();
          }
          if (state is LoadProfileState) {
            return Container(
              child: Center(
                child: CircularProgressIndicator(),
              ),
            );
          }
          User user = (state as InProfileState).user;
          return Container(
            child: Column(
              children: <Widget>[
                Center(
                  child: Hero(
                    tag: "profile-pic",
                    child: ClipOval(
                      child: CachedNetworkImage(
                        imageUrl: user.photoUrl,
                        width: MediaQuery.of(context).size.width * 0.6,
                        height: MediaQuery.of(context).size.width * 0.6,
                        fit: BoxFit.cover,
                      ),
                    ),
                  ),
                ),
                Center(
                  child: Text(
                    user.givenName + " " + user.familyName,
                    style: TextStyle(
                      fontSize: 26,
                      fontWeight: FontWeight.w400,
                    ),
                  ),
                ),
                ListTile(
                  leading: Icon(FontAwesomeIcons.birthdayCake),
                  title: Text("Birthday: ${formatter.format(user.birthday)}"),
                ),
                ListTile(
                  leading: Icon(user.gender == "Male"
                      ? FontAwesomeIcons.male
                      : FontAwesomeIcons.female),
                  title: Text("Gender: ${user.gender}"),
                ),
              ],
            ),
          );
        },
      ),
    );
  }
}
