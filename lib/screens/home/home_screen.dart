import 'package:flutter/material.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {

    return Scaffold(
      appBar: AppBar(
        title: Text("Ghi số điện nước"),
      ),

      body: Padding(
        padding: const EdgeInsets.all(16),
        child: GridView.count(
          crossAxisCount: 2,
          crossAxisSpacing: 15,
          mainAxisSpacing: 15,
          children: [

            menuCard(
              context,
              "Khách hàng",
              Icons.people,
              () {
                Navigator.pushNamed(
                  context,
                  "/customer-list",
                );
              },
            ),

            menuCard(
              context,
              "Sync",
              Icons.sync,
              () {
                Navigator.pushNamed(context, "/sync");
              },
            ),

            menuCard(
              context,
              "History",
              Icons.history,
              () {
                Navigator.pushNamed(context, "/history");
              },
            ),

            menuCard(
              context,
              "Profile",
              Icons.person,
              () {
                Navigator.pushNamed(context, "/profile");
              },
            ),
          ],
        ),
      ),
    );
  }

  Widget menuCard(
      BuildContext context,
      String title,
      IconData icon,
      VoidCallback onTap,
      ) {
    return Card(
      elevation: 4,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(12),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(icon, size: 50, color: Theme.of(context).primaryColor),
            const SizedBox(height: 10),
            Text(
              title,
              style: const TextStyle(fontWeight: FontWeight.bold),
            ),
          ],
        ),
      ),
    );
  }
}