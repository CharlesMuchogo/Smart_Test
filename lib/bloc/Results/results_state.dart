part of 'results_bloc.dart';

class ResultsState extends Equatable {
  final List Results;
  final ResultsStatus status;
  final String message;
  final bool loggedIn;

  const ResultsState(
      {this.Results = const [],
      this.status = ResultsStatus.initial,
      this.loggedIn = false,
      this.message = ""});

  ResultsState copyWith({
    List? Results,
    String? message,
    ResultsStatus? status,
    bool? loggedIn,
  }) {
    return ResultsState(
        Results: Results ?? this.Results,
        status: status ?? this.status,
        loggedIn: loggedIn ?? this.loggedIn,
        message: message ?? this.message);
  }

  @override
  List<Object> get props => [Results, status];

  Map<String, dynamic> toMap() {
    return <String, dynamic>{
      'Results': Results,
      'loggedIn': loggedIn,
      'status': status.index,
    };
  }

  factory ResultsState.fromMap(Map<String, dynamic> map) {
    int index = map['status'];

    return ResultsState(
      Results: List.from((map['Results'] as List)),
      loggedIn: map['loggedIn'],
      status: ResultsStatus.values[index],
    );
  }

  String toJson() => json.encode(toMap());

  factory ResultsState.fromJson(String source) =>
      ResultsState.fromMap(json.decode(source) as Map<String, dynamic>);
}

enum ResultsStatus {
  initial,
  loading,
  loaded,
  error,
  failed,
  success,
}
