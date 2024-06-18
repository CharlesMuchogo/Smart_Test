import 'package:flutter/material.dart';
import 'package:path/path.dart';
import 'dart:io';

class Terms extends StatelessWidget {
  final File file;

  @override
  Widget build(BuildContext context) {
    final name = basename(file.path);
    return Scaffold(
        appBar: AppBar(
          centerTitle: true,
          title: Text("Terms And Conditions"),
        ),
        body: Container());
  }

  Terms(this.file);
}
