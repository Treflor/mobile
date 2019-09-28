import 'dart:io';

import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:image_picker/image_picker.dart';

class CustomImageFormField extends FormField<File> {
  CustomImageFormField({
    FormFieldSetter<File> onSaved,
    FormFieldValidator<File> validator,
    File initialValue,
    double height = 120,
    double width = 120,
    @required bool dark,
  }) : super(
          onSaved: onSaved,
          validator: validator,
          initialValue: initialValue,
          builder: (FormFieldState<File> state) {
            return Column(
              children: <Widget>[
                state.value == null
                    ? Hero(
                        child: ClipOval(
                          child: Image.asset(
                            "assets/images/profile.jpg",
                            fit: BoxFit.cover,
                            width: width,
                            height: height,
                          ),
                        ),
                        tag: "profile-pic",
                      )
                    : ClipOval(
                        child: Image.file(
                          state.value,
                          fit: BoxFit.cover,
                          width: width,
                          height: height,
                        ),
                      ),
                Container(
                  margin: EdgeInsets.only(top: 10),
                  decoration: BoxDecoration(
                      color: dark ? Colors.white30 : Colors.black38,
                      borderRadius: BorderRadius.circular(15)),
                  padding: EdgeInsets.all(3),
                  child: Wrap(
                    direction: Axis.horizontal,
                    children: <Widget>[
                      SizedBox(
                        width: 15,
                      ),
                      IconButton(
                        onPressed: () async {
                          var file = await ImagePicker.pickImage(
                              source: ImageSource.camera);
                          state.didChange(file);
                        },
                        icon: Icon(
                          FontAwesomeIcons.camera,
                          color: dark ? Colors.white70 : Colors.black87,
                          size: 26,
                        ),
                      ),
                      SizedBox(
                        width: 15,
                      ),
                      IconButton(
                        onPressed: () async {
                          var file = await ImagePicker.pickImage(
                              source: ImageSource.gallery);
                          state.didChange(file);
                        },
                        icon: Icon(
                          FontAwesomeIcons.photoVideo,
                          color: dark ? Colors.white70 : Colors.black87,
                          size: 26,
                        ),
                      ),
                      SizedBox(
                        width: 15,
                      ),
                    ],
                  ),
                ),
              ],
            );
          },
        );
}
