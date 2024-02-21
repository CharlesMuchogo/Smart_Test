import 'package:equatable/equatable.dart';

class TestResult extends Equatable {
  final int id;
  final String results;
  final String partnerResults;
  final String image;
  final String partnerImage;
  final String careOption;
  final int userId;
  final String date;

  TestResult({
    required this.id,
    required this.results,
    required this.partnerResults,
    required this.image,
    required this.partnerImage,
    required this.careOption,
    required this.userId,
    required this.date,
  });

  @override
  List<Object?> get props => [
    id,
    results,
    partnerResults,
    image,
    partnerImage,
    careOption,
    userId,
    date,
  ];

  factory TestResult.fromJson(Map<String, dynamic> json) {
    return TestResult(
      id: json['id'],
      results: json['results'],
      partnerResults: json['partnerResults'],
      image: json['image'],
      partnerImage: json['partnerImage'],
      careOption: json['care_option'],
      userId: json['userId'],
      date: json['date'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'results': results,
      'partnerResults': partnerResults,
      'image': image,
      'partnerImage': partnerImage,
      'care_option': careOption,
      'userId': userId,
      'date': date,
    };
  }
}
