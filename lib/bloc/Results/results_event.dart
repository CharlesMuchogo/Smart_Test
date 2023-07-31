// ignore_for_file: public_member_api_docs, sort_constructors_first
part of 'results_bloc.dart';

abstract class ResultsEvent extends Equatable {
  const ResultsEvent();

  @override
  List<Object> get props => [];
}

class ResultsInitial extends ResultsEvent {}

class UploadResults extends ResultsEvent {
  final String results;
  final String partnerResults;
  final String image;
  final String partnerImage;
  final BuildContext context;

  const UploadResults(
      {required this.results,
      required this.partnerResults,
      required this.image,
      required this.context,
      required this.partnerImage,
});
}
