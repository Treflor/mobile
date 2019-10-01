import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:image_picker/image_picker.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';
import 'package:treflor/models/user.dart';
import 'package:treflor/routes/application.dart';
import 'package:treflor/state/auth_state.dart';
import 'index.dart';

class ProfileScreen extends StatelessWidget {
  final formatter = DateFormat("dd - MMMM - yyyy");

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Profile"),
        actions: <Widget>[
          PopupMenuButton(
            onSelected: (int value) {
              switch (value) {
                case 0:
                  _showChangePictureMethodSelect(context);
                  break;
                case 1:
                  _signOut(context);
                  break;
                default:
              }
            },
            itemBuilder: (context) => [
              PopupMenuItem<int>(
                child: Text("Change picture"),
                value: 0,
              ),
              PopupMenuItem<int>(
                child: Text("Logout"),
                value: 1,
              ),
            ],
          ),
        ],
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

  void _showChangePictureMethodSelect(BuildContext pageContext) {
    showDialog(
      context: pageContext,
      builder: (context) => AlertDialog(
        title: Text("Choose method"),
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(16),
        ),
        elevation: 8,
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[
            ListTile(
              leading: Icon(FontAwesomeIcons.camera),
              title: Text("Camera"),
              onTap: () => _showImagePicker(context, ImageSource.camera),
            ),
            ListTile(
              leading: Icon(FontAwesomeIcons.photoVideo),
              title: Text("Gallery"),
              onTap: () => _showImagePicker(pageContext, ImageSource.gallery),
            ),
          ],
        ),
      ),
    );
  }

  void _showImagePicker(BuildContext context, ImageSource source) {
    ImagePicker.pickImage(source: source).then((image) {
      if (image != null) {
        // Provider.of<ProfileBloc>(context).uploadImage(image);
        Navigator.pop(context);
      }
    });
  }

  void _signOut(BuildContext context) {
    Provider.of<AuthState>(context).signout().then((_) {
      Application.router.pop(context);
    });
  }
}
