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
  TestResults _selectedPartnerResult = TestResults.Negative;

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

  _pickPartnerImage() async {
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
        partnerImageFile = File(croppedImage!.path);
        hasPartnerImage = true;
      });
      //Upload to Firebase
      var snapshot = await firebasestorage
          .ref()
          .child('images/${DateTime.now()}')
          .putFile(partnerImageFile);
      var downloadUrl = await snapshot.ref.getDownloadURL();
      setState(() {
        partnerImageUrl = downloadUrl;
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
                              groupValue: _selectedPartnerResult,
                              onChanged: (value) {
                                setState(() {
                                  _selectedPartnerResult = value!;
                                });
                              },
                              child: Text('Negative'),
                            ),
                            RadioMenuButton<TestResults>(
                              value: TestResults.Positive,
                              groupValue: _selectedPartnerResult,
                              onChanged: (value) {
                                setState(() {
                                  _selectedPartnerResult = value!;
                                });
                              },
                              child: Text('Positive'),
                            ),
                            RadioMenuButton<TestResults>(
                              value: TestResults.Invalid,
                              groupValue: _selectedPartnerResult,
                              onChanged: (value) {
                                setState(() {
                                  _selectedPartnerResult = value!;
                                });
                              },
                              child: Text('Invalid'),
                            ),
                          ],
                        ),


                        GestureDetector(
                            onTap: _pickPartnerImage,
                            child: Column(
                              children: [
                                Padding(
                                  padding: const EdgeInsets.all(8.0),
                                  child: Container(
                                    height: 150, width: 150, color: Colors.grey.shade300,
                                    child: hasPartnerImage ? Image.file(partnerImageFile) : Center(
                                      child: Text("Add photo"),),
                                  ),
                                ),
                                showPartnerError ? Text("Please select an image", style: TextStyle(color: Colors.red),): SizedBox()
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
                    return Column(
                      children: [
                        state.status == ResultsStatus.loaded ? Text("Your test results were submitted successfully", style: TextStyle(color: Colors.green, fontSize: 16)):
                        state.status == ResultsStatus.error ?  Text("There was an error submitting your test results ", style: TextStyle(color: Colors.red, fontSize: 16),) : SizedBox(),

                       state.status == ResultsStatus.loaded ? SizedBox(): Padding(
                          padding: const EdgeInsets.all(15.0),
                          child: ElevatedButton(
                            onPressed: () {
                              String individualTestType = "";
                              switch (_selectedResult) {
                                case TestResults.Negative:
                                  individualTestType ="Negative";
                                  break;
                                case TestResults.Positive:
                                  individualTestType ="Positive";
                                  break;
                                case TestResults.Invalid:
                                  individualTestType ="Invalid";
                                  break;
                              }


                              String partnerTestType = "";
                              switch (_selectedPartnerResult) {
                                case TestResults.Negative:
                              partnerTestType ="Negative";
                                  break;
                                case TestResults.Positive:
                                  partnerTestType ="Positive";
                                  break;
                                case TestResults.Invalid:
                                  partnerTestType ="Invalid";
                                  break;
                              }

                              if(!hasImage){
                                setState(() {
                                  showError = true;
                                });
                                return;
                              }else{
                                setState(() {
                                  showError = false;
                                });
                              }

                              if(!hasPartnerImage && widget.couples){
                                setState(() {
                                  showPartnerError = true;
                                });
                                return;
                              }else{
                                setState(() {
                                  showPartnerError = false;
                                });
                              }
                              resultsBloc.add(UploadResults(results: individualTestType , partnerResults: widget.couples? partnerTestType :"N/A", image: imageUrl, partnerImage:  widget.couples? partnerImageUrl :"N/A"));
                            }, child: Text("Submit Results"),),
                        ),
                      ],
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
