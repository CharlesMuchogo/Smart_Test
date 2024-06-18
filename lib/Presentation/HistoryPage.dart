import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:research/bloc/Results/results_bloc.dart';
import 'package:research/models/testResults/TestResults.dart';

class ResultsHistory extends StatelessWidget {
  const ResultsHistory({super.key});

  @override
  Widget build(BuildContext context) {
    context.read<ResultsBloc>().add(GetTestResults(context: context));
    return Scaffold(
        appBar: AppBar(
          title: const Text(
            "Test History",
          ),
          centerTitle: true,
          elevation: 0,
        ),
        body: SizedBox(
          height: MediaQuery.of(context).size.height,
          width: MediaQuery.of(context).size.height,
          child: Center(
            child: BlocBuilder<ResultsBloc, ResultsState>(
                builder: (context, state) {
              if (state.status == ResultsStatus.loading &&
                  state.Results.isEmpty) {
                return const CircularProgressIndicator();
              } else if (state.status == ResultsStatus.error &&
                  state.Results.isEmpty) {
                Text(state.message);
              } else if (state.Results.isEmpty) {
                const Text("You have not uploaded any test results yet");
              }
              List<TestResult> results =
                  state.Results.map((e) => TestResult.fromJson(e)).toList();
              return ListView.separated(
                  itemBuilder: (context, index) {
                    return testResults(results[index]);
                  },
                  separatorBuilder: (context, index) {
                    return const Divider(thickness: 0.5);
                  },
                  itemCount: results.length);
            }),
          ),
        ));
  }
}

Widget testResults(TestResult result) {
  return Container(
    padding: const EdgeInsets.all(10),
    height: 120,
    width: double.infinity,
    child: Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      children: [
        Row(
          children: [
            const Text(
              "Results: ",
              style: TextStyle(fontWeight: FontWeight.w500),
            ),
            Text(result.results),
          ],
        ),
        Row(
          children: [
            const Text(
              "Partner Results: ",
              style: TextStyle(fontWeight: FontWeight.w500),
            ),
            Text(result.partnerResults),
          ],
        ),
        Row(
          children: [
            const Text(
              "Selected care option: ",
              style: TextStyle(fontWeight: FontWeight.w500),
            ),
            Text(result.careOption),
          ],
        ),
        Row(
          children: [
            const Text(
              "Date: ",
              style: TextStyle(fontWeight: FontWeight.w500),
            ),
            Text(result.date),
          ],
        ),
      ],
    ),
  );
}
