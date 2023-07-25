part of 'devices_bloc.dart';

class DevicesState extends Equatable {
  final List Devices;
  final DevicesStatus status;
  final String message;
  final bool loggedIn;

  const DevicesState(
      {this.Devices = const [],
      this.status = DevicesStatus.initial,
      this.loggedIn = false,
      this.message = ""});

  DevicesState copyWith({
    List? Devices,
    String? message,
    DevicesStatus? status,
    bool? loggedIn,
  }) {
    return DevicesState(
        Devices: Devices ?? this.Devices,
        status: status ?? this.status,
        loggedIn: loggedIn ?? this.loggedIn,
        message: message ?? this.message);
  }

  @override
  List<Object> get props => [Devices, status];

  Map<String, dynamic> toMap() {
    return <String, dynamic>{
      'Devices': Devices,
      'loggedIn': loggedIn,
      'status': status.index,
    };
  }

  factory DevicesState.fromMap(Map<String, dynamic> map) {
    int index = map['status'];

    return DevicesState(
      Devices: List.from((map['Devices'] as List)),
      loggedIn: map['loggedIn'],
      status: DevicesStatus.values[index],
    );
  }

  String toJson() => json.encode(toMap());

  factory DevicesState.fromJson(String source) =>
      DevicesState.fromMap(json.decode(source) as Map<String, dynamic>);
}

enum DevicesStatus {
  initial,
  loading,
  loaded,
  error,
  failed,
  success,
}
