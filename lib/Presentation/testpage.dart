// ignore_for_file: prefer_const_constructors

import 'dart:io';

import 'package:firebase_storage/firebase_storage.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:image_cropper/image_cropper.dart';
import 'package:image_picker/image_picker.dart';

import '../bloc/Results/results_bloc.dart';
import '../common/enums.dart';

// ... (other code)

class TestPage extends StatefulWidget {
  const TestPage({Key? key, required this.couples}) : super(key: key);

  final bool couples;

  @override
  State<TestPage> createState() => _TestPageState();
}

class _TestPageState extends State<TestPage> {
  TestResults _selectedResult = TestResults.Negative;
  var imageFile;
  var imageUrl;



  bool hasImage = false;
  bool showError = false;
  //partner

  var partnerImageFile;
  var partnerImageUrl;

  bool hasPartnerImage = false;
  bool showPartnerError = false;


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
      body: BlocProvider(
        create: (context) => resultsBloc,
        child: Padding(
          padding: const EdgeInsets.all(8.0),
          child: SingleChildScrollView(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.start,
              children: [
                Column(
                  children: [
                    Center(child: Text("Select your test results"),),
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
                            child: Column(
                              children: [
                                Padding(
                                  padding: const EdgeInsets.all(8.0),
                                  child: Container(
                                    height: 150, width: 150, color: Colors.grey.shade300,
                                    child: hasImage ? Image.file(imageFile) : Center(
                                      child: Text("Add photo"),),
                                  ),
                                ),
                                showError ? Text("Please select an image", style: TextStyle(color: Colors.red),): SizedBox()
                              ],
                            ),),

                      ],),
                    SizedBox(height: 20,),
                  ],
                ),

               !widget.couples ? SizedBox() : Column(
                  children: [
                    Center(child: Text("Select your partner's test results"),),
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
                            child: Column(
                              children: [
                                Padding(
                                  padding: const EdgeInsets.all(8.0),
                                  child: Container(
                                    height: 150, width: 150, color: Colors.grey.shade300,
                                    child: hasImage ? Image.file(imageFile) : Center(
                                      child: Text("Add photo"),),
                                  ),
                                ),
                                showError ? Text("Please select an image", style: TextStyle(color: Colors.red),): SizedBox()
                              ],
                            ),),

                      ],),
                    SizedBox(height: 20,),
                  ],
                ),

                BlocBuilder<ResultsBloc, ResultsState>(
                  builder: (context, state) {
                    if(state.status == ResultsStatus.loading){
                      return Center(child: CircularProgressIndicator(),);
                    }
                    return Padding(
                      padding: const EdgeInsets.all(15.0),
                      child: ElevatedButton(
                        onPressed: () {
                          String testType = "";
                          switch (_selectedResult) {
                            case TestResults.Negative:
                          testType ="Negative";
                              break;
                            case TestResults.Positive:
                              testType ="Positive";
                              break;
                            case TestResults.Invalid:
                              testType ="Invalid";
                              break;
                          }

                          if(!hasImage){
                            setState(() {
                              showError = true;
                            });
                            return;
                          }
                          resultsBloc.add(UploadResults(results: testType , partnerResults: "N/A", image: imageUrl, partnerImage: "N/A"));
                        }, child: Text("Submit Results"),),
                    );
                  },
                )

              ],
            ),
          ),
        ),
      ),


    );
  }
}
