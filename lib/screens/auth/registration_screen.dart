import 'dart:convert';
import 'dart:io';
import 'package:flutter/services.dart' show rootBundle;
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:image_picker/image_picker.dart';
import 'package:provider/provider.dart';

import 'package:treflor/state/auth_state.dart';

class RegistrationScreen extends StatefulWidget {
  static const String route = '/registration';
  @override
  _RegistrationScreenState createState() => _RegistrationScreenState();
}

class _RegistrationScreenState extends State<RegistrationScreen> {
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  final TextEditingController _familyNameController = TextEditingController();
  final TextEditingController _givenNameController = TextEditingController();

  Image _image = Image.asset("assets/images/profile.jpg");
  File _file = null;

  Future<void> _onSignup(
      Future<void> Function(FormData) signup, context) async {
    var base64Image = '';

    if (_file == null) {
      var byteData = await rootBundle.load("assets/images/profile.jpg");
      var audioUint8List = byteData.buffer
          .asUint8List(byteData.offsetInBytes, byteData.lengthInBytes);
      List<int> imageListInt =
          audioUint8List.map((eachUint8) => eachUint8.toInt()).toList();
      base64Image = base64.encode(imageListInt);
    } else {
      base64Image = base64.encode(_file.readAsBytesSync());
    }

    FormData data = FormData.fromMap({
      "email": _emailController.text,
      "password": _passwordController.text,
      "family_name": _familyNameController.text,
      "given_name": _givenNameController.text,
      "photo": base64Image,
    });

    _showProgressDialog(context);

    await signup(data);
    Navigator.pop(context);
    Navigator.pop(context);
  }

  void _showProgressDialog(context) => showDialog(
        context: context,
        builder: (context) => Container(
          child: Center(
            child: CircularProgressIndicator(),
          ),
        ),
      );

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Registration"),
      ),
      body: SingleChildScrollView(
        child: Container(
          padding: EdgeInsets.all(10),
          child: Column(
            children: <Widget>[
              SizedBox(
                height: 120,
                width: 120,
                child: InkWell(
                  child: ClipOval(
                    child: _image,
                  ),
                  onTap: () async {
                    _file = await ImagePicker.pickImage(
                        source: ImageSource.gallery);
                    setState(() {
                      _image = _file != null
                          ? Image.file(_file)
                          : Image.asset("assets/images/profile.jpg");
                    });
                  },
                ),
              ),
              TextField(
                keyboardType: TextInputType.emailAddress,
                controller: _emailController,
                decoration: InputDecoration(
                  labelText: "Email",
                  prefixIcon: Icon(FontAwesomeIcons.pencilAlt),
                ),
              ),
              TextField(
                controller: _passwordController,
                decoration: InputDecoration(
                  labelText: "Password",
                  prefixIcon: Icon(FontAwesomeIcons.pencilAlt),
                ),
              ),
              TextField(
                controller: _givenNameController,
                decoration: InputDecoration(
                  labelText: "First Name",
                  prefixIcon: Icon(FontAwesomeIcons.pencilAlt),
                ),
              ),
              TextField(
                controller: _familyNameController,
                decoration: InputDecoration(
                  labelText: "Last name",
                  prefixIcon: Icon(FontAwesomeIcons.pencilAlt),
                ),
              ),
              RaisedButton(
                child: Row(
                  children: <Widget>[
                    Spacer(),
                    Icon(FontAwesomeIcons.userPlus),
                    SizedBox(width: 20),
                    Text("Register"),
                    Spacer(),
                  ],
                ),
                // onPressed: () => _onSignup(authState.signUp, context),
                onPressed: () => null,
              ),
            ],
          ),
        ),
      ),
    );
  }
}
