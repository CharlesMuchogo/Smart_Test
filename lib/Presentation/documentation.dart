// ignore_for_file: prefer_const_constructors, prefer_const_literals_to_create_immutables

import 'package:flutter/material.dart';

import '../functions/constants.dart';

class Instruction extends StatelessWidget {
  const Instruction({super.key});

  @override
  Widget build(BuildContext context) {
    return  Column(
      children: [
        Text(FIRST_HEADING, style: Theme.of(context).textTheme.titleMedium!.copyWith(color: Colors.blue.shade800, fontWeight: FontWeight.bold),),
        Text(FIRST_INSTRUCTIONS),
        Text(SECOND_INSTRUCTIONS),
        Text(SECOND_HEADING,  style: Theme.of(context).textTheme.titleMedium!.copyWith(color: Colors.blue.shade800, fontWeight: FontWeight.bold),),
        Text(STEP_TWO_INSTRUCTIONS),
        Text(THIRD_HEADING,  style: Theme.of(context).textTheme.titleMedium!.copyWith(color: Colors.blue.shade800, fontWeight: FontWeight.bold),),
        Text(STEP_THREE_INSTRUCTIONS),
        Text(RECOMENDATIONS_HEADING,  style: Theme.of(context).textTheme.titleMedium!.copyWith(color: Colors.blue.shade800, fontWeight: FontWeight.bold),),
        Text(RECOMENDATIONS),
        Text(IMPORTANT_INFORMATION_HEADING,  style: Theme.of(context).textTheme.titleMedium!.copyWith(color: Colors.blue.shade800, fontWeight: FontWeight.bold),),
        Text(IMPORTANT_INFORMATION),
        Text(PRECAUTIONS_HEAD,  style: Theme.of(context).textTheme.titleMedium!.copyWith(color: Colors.blue.shade800, fontWeight: FontWeight.bold),),
        Text(PRECAUTIONS),
        Text(READING_HEAD,  style: Theme.of(context).textTheme.titleMedium!.copyWith(color: Colors.blue.shade800, fontWeight: FontWeight.bold),),
        Text(READING),
        Text(INTERPRETATION_HEADING,  style: Theme.of(context).textTheme.titleMedium!.copyWith(color: Colors.blue.shade800, fontWeight: FontWeight.bold),),
        Text(INTERPRETATION_NEGATIVE),
        Text(POSITIVE_TEST_HEAD,  style: Theme.of(context).textTheme.titleMedium!.copyWith(color: Colors.blue.shade800, fontWeight: FontWeight.bold),),
        Text(POSITIVE_TEST),
        Text(TESTING_AND_ME_HEADING,  style: Theme.of(context).textTheme.titleMedium!.copyWith(color: Colors.blue.shade800, fontWeight: FontWeight.bold),),
        Text(NEGATIVE_RESULT_EXPLANATION),
        Text(POSITIVE_RESULT_EXPLANATION),
        Text(WARNINGS_AND_PRECAUTIONS,  style: Theme.of(context).textTheme.titleMedium!.copyWith(color: Colors.blue.shade800, fontWeight: FontWeight.bold),),
        Text(WARNINGS_AND_PRECAUTIONS_INTRO),
        Text(WARNINGS),
      ],
    );
  }
}
