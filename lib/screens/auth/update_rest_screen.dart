import 'dart:convert';
import 'dart:io';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';

import 'package:treflor/wigets/custom_image_from_field.dart';
import 'package:treflor/wigets/custom_text_form_field.dart';
import 'package:treflor/models/user.dart';
import 'package:treflor/routes/application.dart';
import 'package:treflor/state/auth_state.dart';
import 'package:treflor/state/config_state.dart';
import 'package:treflor/wigets/custom_date_picker_form_field.dart';
import 'package:treflor/wigets/CustomDropDownFormField.dart';

class UpdateRestScreen extends StatefulWidget {
  @override
  _UpdateRestScreenState createState() => _UpdateRestScreenState();
}

class _UpdateRestScreenState extends State<UpdateRestScreen> {
  GlobalKey<FormState> _key = GlobalKey();
  User _user;
  bool _isRegistering = false;

  @override
  Widget build(BuildContext context) {
    ConfigState configState = Provider.of<ConfigState>(context);
    AuthState authState = Provider.of<AuthState>(context);
    _user = authState.user;
    return Scaffold(
      appBar: AppBar(
        title: Text("Treflor"),
      ),
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.only(left: 16.0, right: 16, top: 16),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: <Widget>[
              Text(
                "Update Account",
                style: Theme.of(context).textTheme.headline,
              ),
              Form(
                key: _key,
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: <Widget>[
                    SizedBox(
                      height: 16,
                    ),
                    ClipOval(
                      child: CachedNetworkImage(
                        imageUrl: authState.user.photoUrl,
                        height: 120,
                        width: 120,
                        fit: BoxFit.cover,
                      ),
                    ),
                    SizedBox(
                      height: 16,
                    ),
                    CustomDatePickerFormField(
                      dark: configState.darkMode,
                      context: context,
                      format: DateFormat("dd - MMMM - yyyy"),
                      onSaved: (DateTime date) {
                        _user.birthday = date ?? DateTime.now();
                      },
                    ),
                    SizedBox(
                      height: 16,
                    ),
                    CustomDropDownFormField(
                      dark: configState.darkMode,
                      labelText: "Gender",
                      items: ["Male","Female"],
                      onSaved: (dynamic value) {
                        _user.gender = value;
                      },
                      validator: _validateGender,
                    ),
                    SizedBox(
                      height: 16,
                    ),
                    RaisedButton(
                      elevation: 0,
                      color: Theme.of(context).buttonColor,
                      colorBrightness: configState.darkMode
                          ? Brightness.dark
                          : Brightness.light,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(8),
                      ),
                      padding: EdgeInsets.symmetric(vertical: 16.0),
                      child:
                          _isRegistering ? Text("Updating...") : Text("Update"),
                      onPressed: _isRegistering ? null : _onSubmit,
                    ),
                  ],
                ),
              ),
              SizedBox(
                height: 16,
              ),
              FlatButton(
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(8),
                ),
                padding: EdgeInsets.symmetric(vertical: 16.0),
                child: Text("Skip"),
                onPressed: _onSkip,
              ),
            ],
          ),
        ),
      ),
    );
  }

  String _validateGender(String gender) {
    if (gender == null) {
      return "gender can't be empty";
    }
    return null;
  }

  void _onSkip() {
    Application.router
        .navigateTo(context, '/', replace: true, clearStack: true);
  }

  void _onSubmit() {
    _key.currentState.save();
    if (_key.currentState.validate()) {
      setState(() {
        _isRegistering = true;
      });
      Provider.of<AuthState>(context).update(_user).then((success) {
        if (success) {
          Application.router
              .navigateTo(context, "/", replace: true, clearStack: true);
        } else {
          setState(() {
            _isRegistering = false;
          });
        }
      }).catchError((err) {
        setState(() {
          _isRegistering = false;
        });
        print(err.toString());
        // Scaffold.of(context).showSnackBar(SnackBar(
        //   content: Text("Sign in failed!"),
        // ));
      });
    }
  }
}
