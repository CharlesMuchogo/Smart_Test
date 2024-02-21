import 'package:flutter/material.dart';
import 'package:research/Presentation/home.dart';
import 'package:research/Presentation/profile.dart';

import 'HistoryPage.dart';

class BottomNavigation extends StatefulWidget {
  const BottomNavigation({super.key});

  @override
  State<BottomNavigation> createState() => _BottomNavigationState();
}

class _BottomNavigationState extends State<BottomNavigation> {
  List<Map<String, dynamic>> pages = [
    {'page': Homepage(), 'label': "Home", "icon": Icons.home},
    {'page': ResultsHistory(), 'label': "History", "icon": Icons.history},
    {'page': Profile(), 'label': "Profile", "icon": Icons.person},
  ];

  int selectedPageIndex = 0;


  void selectPage(int index) {
    setState(() {
      selectedPageIndex = index;
    });
  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: pages[selectedPageIndex]["page"],
      bottomNavigationBar: NavigationBar(
        destinations: pages.map((e) => NavigationDestination(
          icon: Icon(e["icon"], color: e["label"] == pages[selectedPageIndex]["label"] ? Colors.white : Colors.grey,) ,
          label: e["label"],
        ),) .toList(),
        selectedIndex: selectedPageIndex,
        indicatorColor: Theme.of(context).primaryColor,
        onDestinationSelected: selectPage,
      ),
    );
  }
}
