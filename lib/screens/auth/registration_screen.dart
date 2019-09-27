import 'dart:convert';
import 'dart:io';
import 'package:flutter/services.dart' show rootBundle;
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'package:treflor/wigets/custom_image_from_field.dart';
import 'package:treflor/wigets/custom_text_form_field.dart';
import 'package:treflor/models/register_user.dart';
import 'package:treflor/routes/application.dart';
import 'package:treflor/state/auth_state.dart';
import 'package:treflor/state/config_state.dart';

class RegistrationScreen extends StatefulWidget {
  @override
  _RegistrationScreenState createState() => _RegistrationScreenState();
}

class _RegistrationScreenState extends State<RegistrationScreen> {
  GlobalKey<FormState> _key = GlobalKey();
  RegisterUser _user = RegisterUser.just();
  bool _isRegistering = false;
  var _base64Image = '';

  _RegistrationScreenState() {
    loadInitialImageString();
  }

  void loadInitialImageString() async {
    var byteData = await rootBundle.load("assets/images/profile.jpg");
    var imageUint8List = byteData.buffer
        .asUint8List(byteData.offsetInBytes, byteData.lengthInBytes);
    List<int> imageListInt =
        imageUint8List.map((eachUint8) => eachUint8.toInt()).toList();
    _base64Image = base64.encode(imageListInt);
  }

  @override
  Widget build(BuildContext context) {
    ConfigState configState = Provider.of<ConfigState>(context);
    return Scaffold(
      appBar: AppBar(
        title: Text(""),
      ),
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.only(left: 16.0, right: 16, top: 16),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: <Widget>[
              Text(
                "Sign Up",
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
                    CustomImageFormField(
                      onSaved: _onSavedImage,
                    ),
                    SizedBox(
                      height: 16,
                    ),
                    CustomTextFormField(
                      dark: configState.darkMode,
                      labelText: "email",
                      onSaved: (String value) {
                        _user.email = value;
                      },
                      validator: _validateEmail,
                    ),
                    SizedBox(
                      height: 16,
                    ),
                    CustomTextFormField(
                      dark: configState.darkMode,
                      labelText: "Password",
                      isObscure: true,
                      onSaved: (String value) {
                        _user.password = value;
                      },
                      validator: _validatePassword,
                    ),
                    SizedBox(
                      height: 16,
                    ),
                    CustomTextFormField(
                      dark: configState.darkMode,
                      labelText: "Confirm Password",
                      isObscure: true,
                      onSaved: (String value) {
                        _user.password2 = value;
                      },
                      validator: _validateConfirmPassword,
                    ),
                    SizedBox(
                      height: 16,
                    ),
                    CustomTextFormField(
                      dark: configState.darkMode,
                      labelText: "Given Name",
                      textCapitalization: TextCapitalization.words,
                      onSaved: (String value) {
                        _user.givenName = value;
                      },
                      validator: _validateName,
                    ),
                    SizedBox(
                      height: 16,
                    ),
                    CustomTextFormField(
                      dark: configState.darkMode,
                      labelText: "Family Name",
                      onSaved: (String value) {
                        _user.familyName = value;
                      },
                      validator: _validateName,
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
                          _isRegistering ? Text("Signnig Up") : Text("Sign Up"),
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
                child: Text("Already have an account?"),
                onPressed: _onSignIn,
              ),
            ],
          ),
        ),
      ),
    );
  }

  String _validateName(String name) {
    if (name.isEmpty) {
      return "Name can't be empty";
    }
    return null;
  }

  String _validateEmail(String email) {
    if (email.isEmpty) {
      return "Email can't be empty";
    }
    if (!RegExp(
            r"^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,253}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,253}[a-zA-Z0-9])?)*$")
        .hasMatch(email)) {
      return "Email not in valid format";
    }

    return null;
  }

  String _validatePassword(String password) {
    if (password.isEmpty) {
      return "Password can't be empty";
    }
    if (password.length < 4) {
      return "Password lenght must be more than 4";
    }
    return null;
  }

  String _validateConfirmPassword(String password) {
    if (password.isEmpty || _user.password != _user.password2) {
      return "Passwords doesn't match";
    }
    return null;
  }

  void _onSavedImage(File file) async {
    var base64Image = '';

    if (file == null) {
      base64Image = this._base64Image;
    } else {
      base64Image = base64.encode(file.readAsBytesSync());
    }
    _user.base64Image = base64Image;
  }

  void _onSignIn() {
    Application.router.pop(context);
  }

  void _onSubmit() {
    _key.currentState.save();
    if (_key.currentState.validate()) {
      setState(() {
        _isRegistering = true;
      });
      Provider.of<AuthState>(context).signup(_user).then((success) {
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
