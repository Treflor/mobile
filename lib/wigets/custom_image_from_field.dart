import 'dart:io';

import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:image_picker/image_picker.dart';

class CustomImageFormField extends FormField<File> {
  final double width;
  final double height;
  CustomImageFormField({
    FormFieldSetter<File> onSaved,
    FormFieldValidator<File> validator,
    File initialValue,
    this.height = 120,
    this.width = 120,
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
                      color: Colors.white30,
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
