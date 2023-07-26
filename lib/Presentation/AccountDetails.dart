// ignore_for_file: prefer_const_constructors

import 'package:flutter/material.dart';

class AccountDetails extends StatelessWidget {
  final String title, subtitle;
  const AccountDetails({Key? key, required this.title, required this.subtitle})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: Text(
        title,
        style: TextStyle(
            fontSize: 16, color: Colors.black, fontWeight: FontWeight.w500),
      ),
      subtitle: Text(subtitle,
          style: TextStyle(
            fontSize: 14,
          )),
    );
  }
}
