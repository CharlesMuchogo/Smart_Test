// ignore_for_file: prefer_const_constructors, prefer_const_literals_to_create_immutables

import 'package:flutter/material.dart';

import '../functions/constants.dart';

class Instruction extends StatelessWidget {
  const Instruction({super.key});

  @override
  Widget build(BuildContext context) {
    return  Column(
      children: [
        Padding(
          padding: const EdgeInsets.all(8.0),
          child: Text(FIRST_HEADING, style: Theme.of(context).textTheme.titleLarge!.copyWith(color: Colors.blue.shade800, fontWeight: FontWeight.bold),),
        ),
        Text(FIRST_INSTRUCTIONS,  style: Theme.of(context).textTheme.titleMedium!.copyWith(color: Colors.black, fontWeight: FontWeight.bold)),

        Text(SECOND_INSTRUCTIONS),
        Image.asset("assets/images/pdf1.png"),
        Padding(
          padding: const EdgeInsets.all(8.0),
          child: Text(SECOND_HEADING,  style: Theme.of(context).textTheme.titleMedium!.copyWith(color: Colors.black, fontWeight: FontWeight.bold),),
        ),
        Text(STEP_TWO_INSTRUCTIONS),
        Image.asset("assets/images/pdf2.png"),
        Padding(
          padding: const EdgeInsets.all(8.0),
          child: Text(THIRD_HEADING,  style: Theme.of(context).textTheme.titleMedium!.copyWith(color: Colors.black, fontWeight: FontWeight.bold),),
        ),
        Text(STEP_THREE_INSTRUCTIONS),
        Image.asset("assets/images/pdf3.png"),

        Padding(
          padding: const EdgeInsets.all(8.0),
          child: Text(RECOMENDATIONS_HEADING,  style: Theme.of(context).textTheme.titleLarge!.copyWith(color: Colors.blue.shade800, fontWeight: FontWeight.bold),),
        ),
        Text(RECOMENDATIONS, style: Theme.of(context).textTheme.titleMedium!.copyWith(color: Colors.blue.shade800, fontWeight: FontWeight.bold),),
        Image.asset("assets/images/pdf4.png"),
        Text(IMPORTANT_INFORMATION, ),
        Padding(
          padding: const EdgeInsets.all(8.0),
          child: Text(PRECAUTIONS_HEAD,  style: Theme.of(context).textTheme.titleMedium!.copyWith(color: Colors.blue.shade800, fontWeight: FontWeight.bold),),
        ),
        Image.asset("assets/images/pdf5.png"),
        Text(PRECAUTIONS),
        Padding(
          padding: const EdgeInsets.all(8.0),
          child: Text(READING_HEAD,  style: Theme.of(context).textTheme.titleMedium!.copyWith(color: Colors.blue.shade800, fontWeight: FontWeight.bold),),
        ),
        Text(READING),
        Padding(
          padding: const EdgeInsets.all(8.0),
          child: Text(INTERPRETATION_HEADING,  style: Theme.of(context).textTheme.titleMedium!.copyWith(color: Colors.blue.shade800, fontWeight: FontWeight.bold),),
        ),
        Text(INTERPRETATION_NEGATIVE),
        Padding(
          padding: const EdgeInsets.all(8.0),
          child: Text(POSITIVE_TEST_HEAD,  style: Theme.of(context).textTheme.titleMedium!.copyWith(color: Colors.blue.shade800, fontWeight: FontWeight.bold),),
        ),

      ],
    );
  }
}
