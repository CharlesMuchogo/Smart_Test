// ignore_for_file: public_member_api_docs, sort_constructors_first
part of 'results_bloc.dart';

abstract class ResultsEvent extends Equatable {
  const ResultsEvent();

  @override
  List<Object> get props => [];
}

class ResultsInitial extends ResultsEvent {}

class GetTestResults extends ResultsEvent {
  final BuildContext context ;
  const GetTestResults({required this.context});
}


class UploadResults extends ResultsEvent {
  final ResultsDTO resultsDTO;
  const UploadResults(
      {required this.resultsDTO
});
}
