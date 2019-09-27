import 'dart:io';

import 'package:flutter/material.dart';
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
            return InkWell(
              borderRadius: BorderRadius.circular(100),
              child: state.value == null
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
              onTap: () async {
                var file =
                    await ImagePicker.pickImage(source: ImageSource.gallery);
                state.didChange(file);
              },
            );
          },
        );
}
