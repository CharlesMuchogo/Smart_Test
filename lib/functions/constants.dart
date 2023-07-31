// ignore_for_file: prefer_const_constructors, constant_identifier_names
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:hydrated_bloc/hydrated_bloc.dart';
import 'package:http/http.dart' as http ;

import 'package:path_provider/path_provider.dart';
import 'package:path/path.dart';

import '../auth/terms.dart';

//local server
//const String BASEURL =  "http://192.168.100.6:9000";
 const String BASEURL = "http://13.246.207.31:9000";
const String TermsAndConditions = "https://firebasestorage.googleapis.com/v0/b/flutter-notifications-a462c.appspot.com/o/T%26C%20for%20App.pdf?alt=media&token=b0b8d75d-addd-4451-8d22-37a76dad8d13";

const String INSTRUCTIONS = '''
You can test for HIV in a safe place
Self-testing is a convenient and private option for finding out your HIV status. An HIV self-test allows you to test for HIV at home, or wherever you feel most comfortable. This means you don’t have to worry about facing anyone’s judgement for taking a test. And just to reassure you – nobody should be judging anyway and that includes yourself. Taking regular HIV tests is the right thing to do for your health – it deserves a high five!

You can get super speedy results (but you have to test at the right time)
You can usually get results from a self-test kit within 20 minutes.

Self-test kits work by testing either your saliva (spit) or blood for antibodies to HIV (these are proteins your body makes to try to fight off HIV). Keep in mind that these rapid antibody tests can’t detect HIV straight after infection. For an accurate result you should wait three months after exposure before testing.

Just to remind you, exposure to HIV happens during sex if you don’t use a condom, or the condom breaks.

HIV self-test kits are easy to buy or sometimes provided free by health facilities
You can buy HIV self-test kits in pharmacies or online and you don’t need a medical note. If you’re happy to go to a clinic you may be able to get them for free. Costs will vary so it can be worth shopping around. As a rough guide, in Kenya-Africa start from around Sh 499 to  999 (just over 3 dollars).
HIV self-testing is reliable. You can rest assured that an HIV self-test is reliable – the World Health Organization has been recommending it as a safe and effective way to test since 2016. They even say that doing your own is no less accurate than having one done by a trained healthcare worker.

On rare occasions a self-test can produce a positive result which is later found to be incorrect. If you get a positive result from your home test you should go to an HIV clinic as soon as possible. They will always offer you another test to confirm the result and make sure you get the treatment you need to keep you healthy.

An HIV self-test is completely private
Occasionally, people find themselves in a situation where they personally know their health provider and it feels awkward to go to see them. Or you may worry that your information will not be kept private. In this case an HIV self-test can be an excellent solution.

It’s easy to HIV self-test and it doesn’t hurt
These infographics give a step-by-step example of what is involved in testing either your blood or your saliva for HIV. Each test kit will have slight variations so always read the full instructions in your kit before beginning.

Pricking your finger to get blood might sound a bit scary – but it really doesn’t hurt – it’s a bit like the ping you can get from an elastic band.

    ''';


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
