
# Voice Based E-commerce Application (VBE)
**Final Year Project**
## Getting Started
**This repository consist of three branches**
- Master(Buyer Application)
- Seller(Seller Appliation)
- Admin(Admin Application)

**_Download_ _code_ _from_ _all_ _three_ _branches_ _as_ _a_ _seperate_ _android_ _project_ _folders_**

## Prerequisites
- Android Studio
- Account on Firebase console

## Installing

### For VBE Buyer
- Unzip project folder from **master** branch download zip file
- Open the VBE Buyer Project in Android Studio
- Go to Firebase Console on your browser. Create a new project.
- Enter Project name
- Select default Account for firebase
- Select Android
- On next screen add package name of project that is **com.biluutech.vbebuyer**
- Add project nick name (any)
- Add project SHA that is **7F:E7:B2:D4:C3:81:24:CD:BD:F9:5E:EF:A5:F7:C3:83:23:E2:CA:A4**
- Now download the *google-services.json* file and copy that file, paste it inside the app folder.(Guide image will also be shown)
- Add Firebase SDK dependencies according to the instructions.
- Than click next
- Almost done,
- Click on Authentication and get started button
- In sign in method, Enable the Email/Password option
- Than click on the Realtime Database option in the side menu, and click Create Database Button
- Select United State as location
- Than go to Rules option on top
- Change the rules to true in front of both read and wirte
- Select Storage from the side menu
- Than click next and done
- Now go to your Android Studio follow Tools>Firebase
- An assistant panel will open. 
- Select Authentication, than select authentication using custom Authentication system and add both dependencies
- Select Realtime Database and similarly add Both dependencies
- Select Storage, and add both dependencies
- Your VBE Buyer end is done.

### For VBE Seller
- Unzip project folder from **seller** branch download zip file
- Open the VBE Seller Project in Android Studio
- Go to Firebase Console on your browser. Select the project that you have made.
- Go to Project OverView and select Add App option, than select android.
- On next screen add package name of project that is **com.designer.vbeseller**
- Add project nick name (any)
- Add project SHA that is **7F:E7:B2:D4:C3:81:24:CD:BD:F9:5E:EF:A5:F7:C3:83:23:E2:CA:A4**
- Now download the *google-services.json* file and copy that file, paste it inside the app folder.(Guide image will also be shown)
- Add Firebase SDK dependencies according to the instructions.
- Than click next
- Almost done,
- Click on Authentication and get started button
- In sign in method, Enable the Email/Password option
- Than click on the Realtime Database option in the side menu, and click Create Database Button
- Select United State as location
- Than go to Rules option on top
- Change the rules to true in front of both read and wirte
- Select Storage from the side menu
- Than click next and done
- Now go to your Android Studio follow Tools>Firebase
- An assistant panel will open. 
- Select Authentication, than select authentication using custom Authentication system and add both dependencies
- Select Realtime Database and similarly add Both dependencies
- Select Storage, and add both dependencies
- Your VBE Seller end is done.

### For VBE Admin
- Unzip project folder from **admin** branch download zip file
- Open the VBE Admin Project in Android Studio
- Go to Firebase Console on your browser. Select the project that you have made.
- Go to Project OverView and select Add App option, than select android.
- On next screen add package name of project that is **com.alimedia.vbeadmin**
- Add project nick name (any)
- Add project SHA that is **7F:E7:B2:D4:C3:81:24:CD:BD:F9:5E:EF:A5:F7:C3:83:23:E2:CA:A4**
- Now download the *google-services.json* file and copy that file, paste it inside the app folder.(Guide image will also be shown)
- Add Firebase SDK dependencies according to the instructions.
- Than click next
- Almost done,
- Click on the Realtime Database option in the side menu, and click Create Database Button
- Select United State as location
- Than go to Rules option on top
- Change the rules to true in front of both read and wirte
- In data tab, add a node named **Admin**. Under this node add two child named **email** having value **admin@gmail.com** and second child named **password** having value **123admin**
- Select Storage from the side menu
- Than click next and done
- Now go to your Android Studio follow Tools>Firebase
- An assistant panel will open. 
- Select Realtime Database and add Both dependencies
- Select Storage, and add both dependencies
- Your VBE Admin end is done.
