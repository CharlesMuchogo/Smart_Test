import 'package:flutter/material.dart';


class AppFunctions{


  void snackbar(BuildContext context, String message, Color color) {
    final snackBar = SnackBar(
      content: Text(message),
      backgroundColor: color,
      elevation: 1000,
      behavior: SnackBarBehavior.floating,
      action: SnackBarAction(
        label: '',
        onPressed: () {},
      ),
    );
    ScaffoldMessenger.of(context).showSnackBar(snackBar);
  }



}