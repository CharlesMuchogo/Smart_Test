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

const FIRST_HEADING = "Results in three Easy Steps/ Matokeo katika Hatua tatu Rahisi";
const FIRST_INSTRUCTIONS = '''Step 1: Collect''';

const SECOND_INSTRUCTIONS = '''Gently swab the pad along your upper gums once and your lower gums once. You may use either side of the flat pad.
Finyilia utambi wa Kifaa juu ya ufizi, pangusa ufizi wote wa juu mara moja na pia ufizi wote wa chini mara moja ''';

const SECOND_HEADING = "Step 2: Insert/Ingiza";

const STEP_TWO_INSTRUCTIONS = '''Put the test directly into the test tube where indicated in the upper portion of the test kit - "Test Holder". 
Ingiza Kifaa ndani ya kichupa hadi Utambi utumbukie ndani ya maji''';

const THIRD_HEADING = "Step 3: Read";
const STEP_THREE_INSTRUCTIONS = '''The test window will turn pink for a few minutes. Do not read your results before 20 minutes have passed. Once your results are ready, you must read between 20 and 40 minutes from the start time.
Wacha kifaa ndani ya kichupa kwa dakika 20 kabla ya kusoma tokeo. Usisome tokeo baada ya dakika 40''';

const RECOMENDATIONS_HEADING = "Reading your OraQuick In-Home results/ Kusoma Matokeo";
const RECOMENDATIONS = '''Negative Test''';

const IMPORTANT_INFORMATION = '''If there's one line next to the "C" and no line next to the "T", your result is negative.
Mstari mmoja kwenye eneo “c” na hakuna mstari kwenye eneo “T”, tokeo lako ni hasi, huna virusi vya ukimwi.''';

const PRECAUTIONS_HEAD = "Positive Test";
const PRECAUTIONS = '''If there are two complete lines, one next to the "C" and any line next to the "T"—even a faint line—you may have HIV.
Laini mbili kamili, hataingawa laini ni hafifu, inamaanisha huenda ukawa na virusi vya ukimwi na unahitaji vipimo zaidi.
 ''';

const READING_HEAD = "Negative result explanation";
const READING = '''If your result is negative and if it has been at least 3 months since you have had a risk event and you have followed the directions carefully, then you likely do not have HIV. If your test result is negative and you engage in activities that put you at risk for HIV, you should test regularly.
The most important thing to keep in mind is that HIV is preventable. Understanding how you can avoid getting HIV is important to protect yourself and your partner(s).''';

const INTERPRETATION_HEADING  = "Positive result explanation";
const INTERPRETATION_NEGATIVE = '''If your result is positive, there are a couple of important things you should do next.
A clinic or healthcare professional must confirm your test result.
There are also some things that you should know about HIV that may ease some of the stress or confusion that you may be feeling:
    • You are not alone
    • Medical treatments are available to help people live long, healthy, lives
    • Having HIV does not mean that you have or will get AIDS
With new treatments, many people who are HIV-positive continue to live long and active lives. They are also able to have normal relationships with HIV-negative individuals without the risk of infecting them with the virus. Ongoing research is finding better ways to treat HIV nearly every day. The key is to identify the infection as early as possible before irreparable damage is done.''';

const POSITIVE_TEST_HEAD = '''NOTE: FOR FURTHER INSTRUCTIONS, REFER TO THE INSTRUCTION MANUAL ACCOMPANYING THE TEST KITS''';


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
