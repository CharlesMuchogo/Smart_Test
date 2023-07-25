// ignore_for_file: prefer_const_constructors, constant_identifier_names
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';
import 'package:http/http.dart' as http ;

import 'package:path_provider/path_provider.dart';
import 'package:path/path.dart';

import '../auth/terms.dart';

//local server
const String BASEURL =  "http://192.168.100.6:8080";
// const String BASEURL = "http://13.246.207.31:8001";
const String TermsAndConditions = "https://firebasestorage.googleapis.com/v0/b/flutter-notifications-a462c.appspot.com/o/T%26C%20for%20App.pdf?alt=media&token=b0b8d75d-addd-4451-8d22-37a76dad8d13";

class AppConstants {

   Future<File> loadNetwork() async {
    final response = await http.get(Uri.parse(TermsAndConditions));
    final bytes = response.bodyBytes;

    return _storeFile(TermsAndConditions, bytes);
  }

   void openPDF(BuildContext context, File file) => Navigator.of(context).push(
     MaterialPageRoute(builder: (context) => Terms( file)),
   );

  Future<File> _storeFile(String url, List<int> bytes) async {
    final filename = basename(url);
    final dir = await getApplicationDocumentsDirectory();

    final file = File('${dir.path}/$filename');
    await file.writeAsBytes(bytes, flush: true);
    return file;
  }


  void alertDialog(
      {required BuildContext context,
      required Widget content,
      required String title,
      required VoidCallback onpress}) {
    showDialog(
        context: context,
        builder: (BuildContext context) {
          return AlertDialog(
            title: Text(title),
            content: content,
            actions: [
              MaterialButton(
                onPressed: () {
                  Navigator.of(context).pop();
                },
                child: Text("Cancel"),
              ),
              MaterialButton(
                onPressed: onpress,
                child: Text("submit"),
              ),
            ],
          );
        });
  }
}
