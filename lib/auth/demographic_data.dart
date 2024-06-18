import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:research/Presentation/common/AppButton.dart';
import 'package:research/Presentation/common/AppTextField.dart';

class DemographicDataPage extends StatefulWidget {
  const DemographicDataPage({super.key});

  @override
  State<DemographicDataPage> createState() => _DemographicDataPageState();
}

class _DemographicDataPageState extends State<DemographicDataPage> {
  GlobalKey<FormState> _key = GlobalKey<FormState>();
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
        key: _key,
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
              content: Text("Submit", style: TextStyle(color: Colors.white),),
              onClick: (){},
            )
          ],
        ),
      ),
    );
  }
}
