part of 'login_bloc.dart';

class LoginState extends Equatable {
  final List login;
  final LoginStatus status;
  final String message;
  final bool loggedIn;

  const LoginState(
      {this.login = const [],
      this.status = LoginStatus.initial,
      this.loggedIn = false,
      this.message = ""});

  LoginState copyWith({
    List? login,
    String? message,
    LoginStatus? status,
    bool? loggedIn,
  }) {
    return LoginState(
        login: login ?? this.login,
        status: status ?? this.status,
        loggedIn: loggedIn ?? this.loggedIn,
        message: message ?? this.message);
  }

  @override
  List<Object> get props => [login, status];

  Map<String, dynamic> toMap() {
    return <String, dynamic>{
      'login': login,
      'loggedIn': loggedIn,
      'status': status.index,
    };
  }

  factory LoginState.fromMap(Map<String, dynamic> map) {
    int index = map['status'];

    return LoginState(
      login: List.from((map['login'] as List)),
      loggedIn: map['loggedIn'],
      status: LoginStatus.values[index],
    );
  }

  String toJson() => json.encode(toMap());

  factory LoginState.fromJson(String source) =>
      LoginState.fromMap(json.decode(source) as Map<String, dynamic>);
}

enum LoginStatus {
  initial,
  loading,
  loaded,
  error,
  failed,
  success,
}
