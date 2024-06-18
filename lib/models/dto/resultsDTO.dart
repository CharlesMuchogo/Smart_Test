import 'dart:io';

import 'package:flutter/cupertino.dart';

class ResultsDTO {
  ResultsDTO({
    required this.results,
    required this.partnerResults,
    required this.careOption,
    required this.context,
    required this.resultsPhoto,
    required this.partnerResultsPhoto,
  });

  final String? results;
  final String? partnerResults;
  final String? careOption;
  final BuildContext context;
  final File? resultsPhoto;
  final File? partnerResultsPhoto;
}
