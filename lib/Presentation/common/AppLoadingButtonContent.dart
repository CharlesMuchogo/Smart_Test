import 'package:flutter/material.dart';

class AppLoadingButtonContent extends StatelessWidget {
  const AppLoadingButtonContent({super.key, this.message = 'Loading...'});
  final String message;

  @override
  Widget build(BuildContext context) {
    return  Center(
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          SizedBox(
              height: 20,
              width: 20,
              child: CircularProgressIndicator(
                color: Colors.white,
                strokeWidth: 2,
              )),
          SizedBox(width: 10),
          Text(
            message,
            style: TextStyle(color: Colors.white),
          )
        ],
      ),
    );;
  }
}
