// ignore_for_file: prefer_const_constructors

import 'dart:io';

import 'package:firebase_storage/firebase_storage.dart';
import 'package:flutter/material.dart';
import 'package:image_cropper/image_cropper.dart';
import 'package:image_picker/image_picker.dart';

import '../bloc/Results/results_bloc.dart';
import '../common/enums.dart';

// ... (other code)

class TestPage extends StatefulWidget {
  const TestPage({Key? key}) : super(key: key);

  @override
  State<TestPage> createState() => _TestPageState();
}

class _TestPageState extends State<TestPage> {
  TestResults _selectedResult = TestResults.Negative;
  var imageFile;
  var imageUrl;

  bool hasImage = false;

  ResultsBloc resultsBloc = ResultsBloc();

  _pickImage() async {
    var pictureFile =
    await ImagePicker.platform.getImageFromSource(source: ImageSource.camera);

    final firebasestorage = FirebaseStorage.instance;

    if (pictureFile!.path.isNotEmpty) {
      showDialog(
          context: context,
          builder: (context) {
            return Center(
              child: CircularProgressIndicator(),
            );
          });
      var croppedImage = await ImageCropper.platform.cropImage(
        sourcePath: pictureFile.path,
        aspectRatio: CropAspectRatio(ratioX: 1, ratioY: 1),
        uiSettings: [
          AndroidUiSettings(
              toolbarColor: Colors.purple,
              toolbarWidgetColor: Colors.white,
              initAspectRatio: CropAspectRatioPreset.original,
              lockAspectRatio: false)
        ],
        compressQuality: 100,
        maxHeight: 700,
        maxWidth: 700,
        compressFormat: ImageCompressFormat.jpg,
      );
      setState(() {
        imageFile = File(croppedImage!.path);
        hasImage = true;
      });
      //Upload to Firebase
      var snapshot = await firebasestorage
          .ref()
          .child('images/${DateTime.now()}')
          .putFile(imageFile);
      var downloadUrl = await snapshot.ref.getDownloadURL();
      setState(() {
        imageUrl = downloadUrl;
      });


      Navigator.pop(context);
    } else {
      Navigator.pop(context);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: Text("Test"),
      ),
      body: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          children: [
            Center(child: Text("Select test results"),),
            SizedBox(height: 30,),
             Row(
               mainAxisAlignment: MainAxisAlignment.spaceBetween,
               children: [
               Column(
                 children: [
                   RadioMenuButton<TestResults>(
                     value: TestResults.Negative,
                     groupValue: _selectedResult,
                     onChanged: (value) {
                       setState(() {
                         _selectedResult = value!;
                       });
                     },
                     child: Text('Negative'),
                   ),
                   RadioMenuButton<TestResults>(
                     value: TestResults.Positive,
                     groupValue: _selectedResult,
                     onChanged: (value) {
                       setState(() {
                         _selectedResult = value!;
                       });
                     },
                     child: Text('Positive'),
                   ),
                   RadioMenuButton<TestResults>(
                     value: TestResults.Invalid,
                     groupValue: _selectedResult,
                     onChanged: (value) {
                       setState(() {
                         _selectedResult = value!;
                       });
                     },
                     child: Text('Invalid'),
                   ),
                 ],
               ),


                 GestureDetector(
                     onTap: _pickImage,
                     child: Container(height:  150, width: 150, color: Colors.blue,
                     child: hasImage ?  Image.file(imageFile) : Center(child: Text("Add photo"),),
                     ))

             ],),
            SizedBox(height: 20,),

            Padding(
              padding: const EdgeInsets.all(15.0),
              child: ElevatedButton(onPressed: (){}, child: Text("Submit"),),
            )

          ],
        ),
      ),


    );
  }
}
