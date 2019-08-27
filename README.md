
Sample tested with Android Studio 3.4.2 on August 27, 2019

To Run this sample code you need to:
1. Copy your twitter app's API Key and Secret Key to the code, by [Setup Twitter account and creating an app on twitter] ()
2. Download google-services.json file from firebase and put it to the app module's directory of this sample project, by making [Firebase configurations for android application - Firebase authentication] ()
3. Build/Run the Android Sample Project and enjoy! :)

Setup Twitter account and creating an app on twitter
-------------------------------------------------------
Go to [Twitter Developer Account] (https://developer.twitter.com/en/apps)
Sign up and request to Twitter for your Developer account
When successfully create a developer account you will have a dashboard as below by clicking the [link] (https://developer.twitter.com/en/apps)
5.jpg
Click "Create an app" button to create a twitter application.
6.jpg
- Fill your application information required input fields.
- Check "Enable Sign in with Twitter" option
- Add callback url **twittersdk://**
- Add "Tell us how this app will be used field" with your desired description, I have added the description mentioned below:
 "Application will be used only to get twitter user login information to manage user accounts and provide them ease with logging in through twitter in our - application."
- Fill "Terms of Service Url" and "Privacy policy Url" field.
7.jpg
- Click "Create" button
- On success you will get your application page, with readable information as screenshot attached below:
8.jpg
- Select "Keys and Tokens", copy API and Secret key and paste somewhere and move towards next section of this guide, and Click "Save"
- You will get twitter Sign-in provider enabled as shown in the screenshot below:
14.jpg
- After Go to Project settings by clicking the gear icon at top-left position on left-pane
10.jpg
- Under General Tab, register an app for android through Android Icon as shown in the screenshot
15.jpg
- Put your application package name (**com.hassanjamil.hTwitterSample** in this case), nick, and your SHA-1 and Register App as in screenshot below:
17.jpg
- After, Download the config file (google-services.json) and paste in your app module directory as shown below:
18.jpg
- You can have Debug SHA-1 from your android studio, selecting gradle tab, then ":app>>Tasks>>android then right-click on signingReport option and Run the script in console output you will find SHA-1 as shown:
19.jpg




Firebase account configurations for android application (Firebase authentication)
----------------------------------------------------------------------------
Go to [Firebase Console] (https://console.firebase.google.com/)
Click Add Project, name your project.
Click Continue
1.jpg
Setup Google Analytics for your project or select "Not now" option and click "Create Project"
2.jpg
Wait for sometime firebase is creating your project, when created click "Continue"
3.jpg
Choose "Authentication" option under "Develop" section of left-pane, click "Setup Sign-in method" button
4.jpg
Under "Sign-in method" method you will find twitter Sign-In provider, enable it and put API Key and Secret Key there in their respective fields
Copy the callback url as shown in the screenshot below:
11.jpg
Go to Twitter Developer account, choose your app and then under app details tab you will find edit, click edit details, add that copied callback url from firebase project there in twitter app's callback field as shown in the screenshot below:
13.jpg
