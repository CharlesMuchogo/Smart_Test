import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:research/Presentation/common/AppButton.dart';
import 'package:research/Presentation/common/AppTextField.dart';
import 'package:research/bloc/Login/login_bloc.dart';
import 'package:research/functions/app_functions.dart';
import 'package:research/functions/constants.dart';
import 'package:research/models/dto/UpdateUserDTO.dart';

import '../Presentation/common/AppLoadingButtonContent.dart';

class DemographicDataPage extends StatefulWidget {
  const DemographicDataPage({super.key});

  @override
  State<DemographicDataPage> createState() => _DemographicDataPageState();
}

class _DemographicDataPageState extends State<DemographicDataPage> {
  GlobalKey<FormState> _formKey = GlobalKey<FormState>();
  TextEditingController ageController = TextEditingController();

  List<String> educationChoices = [
    "N/A",
    "KCPE",
    "KCSE",
    "Certificate",
    "Bachelors Degree",
    "Masters Degree",
    "PHD"
  ];

  List<String> genders = ["Male", "Female", "Prefer not to say"];

  String? gender = null;

  String? educationLevel = null;

  bool testedBefore = false;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Personal identification"),
        leading: IconButton(
          onPressed: () {
            SystemNavigator.pop(animated: true);
          },
          icon: Icon(Icons.close),
        ),
      ),
      body: Form(
        key: _formKey,
        child: ListView(
          padding: EdgeInsets.symmetric(horizontal: 12),
          children: [
            AppTextField(
              controller: ageController,
              label: "Age",
              textInputType: TextInputType.number,
              onSaved: () {},
            ),
            Padding(
              padding: const EdgeInsets.symmetric(vertical: 8.0),
              child: DropdownButtonFormField<String>(
                hint: Text("Select your education level"),
                decoration: InputDecoration(
                  labelText: 'Education Level',
                  border: OutlineInputBorder(),
                  contentPadding: EdgeInsets.symmetric(horizontal: 12),
                ),
                value: educationLevel,
                onChanged: (newValue) {
                  setState(() {
                    educationLevel = newValue.toString();
                  });
                },
                validator: (val) => educationLevel == null
                    ? "Please select your education level"
                    : null,
                items: educationChoices.map((choice) {
                  return DropdownMenuItem(
                    child: Text(choice),
                    value: choice,
                  );
                }).toList(),
              ),
            ),
            Padding(
              padding: const EdgeInsets.symmetric(vertical: 8.0),
              child: DropdownButtonFormField<String>(
                hint: Text("Select your gender"),
                decoration: InputDecoration(
                  labelText: 'Gender',
                  border: OutlineInputBorder(),
                  contentPadding: EdgeInsets.symmetric(horizontal: 12),
                ),
                value: gender,
                onChanged: (newValue) {
                  setState(() {
                    gender = newValue.toString();
                  });
                },
                validator: (val) =>
                    gender == null ? "Please select your gender" : null,
                items: genders.map((choice) {
                  return DropdownMenuItem(
                    child: Text(choice),
                    value: choice,
                  );
                }).toList(),
              ),
            ),
            SizedBox(height: 16),
            CheckboxListTile(
              title: Text("I have self-tested myself before"),
              splashRadius: 0,
              controlAffinity: ListTileControlAffinity.leading,
              contentPadding: EdgeInsets.zero,
              value: testedBefore,
              onChanged: (value) {
                setState(
                  () {
                    testedBefore = value ?? !testedBefore;
                  },
                );
              },
            ),
            SizedBox(height: 16),
            AppButton(
              content: BlocConsumer<LoginBloc, LoginState>(
                listener: (context, state) {
                  if(state.status == LoginStatus.failed){
                    AppFunctions().snackbar(context, state.message, Colors.red);
                  }
                  if(state.status == LoginStatus.success){
                    AppFunctions().snackbar(context, state.message, Colors.green);
                  }
                },
                  builder: (context, state) {
                  if(state.status == LoginStatus.loading){
                    return AppLoadingButtonContent(message: "submitting...");
                  }
                  return Text(
                    "Submit",
                    style: TextStyle(color: Colors.white),
                  );
                },
              ),
              onClick: () {
                if (!_formKey.currentState!.validate()) {
                  return;
                }
                UpdateUserDTO userDTO = UpdateUserDTO(
                    age: ageController.text,
                    educationLevel: educationLevel!,
                    testedBefore: testedBefore,
                    gender: gender!);
                context
                    .read<LoginBloc>()
                    .add(UpdateDetails(userDTO: userDTO, context: context));
              },
            )
          ],
        ),
      ),
    );
  }
}
