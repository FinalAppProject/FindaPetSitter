# *Find a PetSitter*


**Find a PetSitter** is an android application that allows users to find pet sitter around an area to take care of their pets. Pet sitters can be searched based on location and date/schedulle availability. Any kind of pet (legal) can find a pet sitter through here. A user can be a pet owner, a pet sitter or both.

**IMPORTANT:**
In order to use the application, developer needs to create a secrets.xml file accordingly to the["Secrets in resource files" codepath.com guide](http://guides.codepath.com/android/Storing-Secret-Keys-in-Android#secrets-in-resource-files) and store the following strings:

* **parse_app_id** : Parse App Id
* **parse_server_url** : Parse Server URL

**secrets.xml** sample:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="parse_server_url"> </string>
    <string name="parse_app_id"> </string>
    <string name="api_key_google_maps"> </string>
</resources>
```


**Application flow:**
1. User opens app sees login screen. You can either login using existing credentials or sign up to create an account. 
2. User clicks on sign up and gives basic info and click on sign up and is taken to home screen OR user adds correct username and password and clicks on "Login" and is taken to home screen
3. On the homescreen user can see viewpager with three tabs: 
    1. List of Pet sitters 
    2. Map view of neighboring sitters. 
    3. Reqests tab which contains a list of requests sent to sitters along wiht the status of the request.
4. User can click on a sitter from the list or from the map view. User is taken to the sitter profile where he can see the sitter's details. He can click on "Request" button to book the sitter. This will open up a window where the owner can select a date range, the pet type and any notes he wants to send the sitter, he then clicks on Send (or cancel and it closes the window)
5. You can navigate to the Request tab and see a new entry created with Status as "PENDING"
6. User can edit profile by going to the navigation drawer and opening "My profile" option. He sees his profile and to edit anything in the profile he clicks on "Edit Profile"
7. In Edit Profile activity user can add details like Address, phone number, Photo. He can choose to be a Pet Sitter by clicking on the "Can host" checkbox. He can also add his pets with "Add Pet" button which opens up the PetProfile activity.
8. In PetProfile user can add details of his pet such as Name, Type, Breed Photo, Description, Special needs. You can select save pet which will show up in the list of pets of the user. You can add multiple pets. These will be a part of the user's profile and can be opened by clicking on their picture.
9. In the navigation drawer "My Requests" option will show requests received by the user. He can click on the request and sees the details of hte request. He can click on accept or .
10. Once the sitter has responded, the user can open up the Requests sent tab and see the updated Status.
11. User can logout by selecting "Log out" option and he will be taken to login screen.



**Activities overview:**
1. Login activity: User/Password authentication using Parse
2. Sign-up activity: Allows users to create a user for the application
3. Home Screen activity: Stream with sitters nearby, mapview of nearby sitters, requests sent
4. User Profile
5. Pet Profile
  
 
Time spent: **** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **sign up** with a username/password against Parse
* [x] User can **login** with a username/password against Parse
  * [x] User credentials are cached for subsequent logins
* [x] User can edit his **profile** information
  * [x] User can add a **profile image** to his profile
  * [x] User can configure his profile with full name, location/address, contact information (phone, email, etc)
  * [x] App uses the **Google Maps Geocoding API** to retrieve/save the user geolocation
* [x] Pet owner can add/modify **pets** under its profile
  * [x] Pet owner can add a **pet profile image** to the pet profile information
  * [x] Pet owner can add pet type (dog/cat/etc), name, picture, breed, emergency contact, special needs, etc
* [x] User can mark himself as a Pet Sitter
  * [x] Pet sitter can configure his profile with specialties (dogs, cats, etc), services provided
  * [ ] Pet sitter can add **photos** of his pet sitting spaces
* [x] After sign-up or authentication user will be directed to the home screen
  * [x] Home screen will contain a Navigation Drawer
* [x] Owner can **request a sitter** from the pet sitter details page 
  * [x] User can click a “Contact sitter”
* [x] Sitter can accept a service request
* [x] User can **review** other users/pets
* [x] User can use **pull down to refresh ** the results

* [x] User can view a list with detailed reviews about other users (sitters/owners)


The following **additional** features are implemented:

* [ ] User can sign-in with **facebook credentials**
* [ ] User can post a general request for any Pet Sitters in case he doesn't find a sitter with his search criteria
* [ ] Users can exchange direct messages
* [ ] User can see embedded image media within any direct messages
* [ ] When a user leaves the contact view without sending the message and there is existing text, prompt to save or delete the draft.  The draft can be resumed from the compose view.
* [ ] User can search applying a filter and get results 
  * [ ] Results will show map with pet sitters that match the search filter
  * [ ] Results will show a list with pet sitters that match the search filter (profile, name, stars, likes count, services, ...)
  * [ ] Results will allow the user to view the pet sitter details
* [ ] User can **open the app offline and see most recent persisted data**.
* [ ] Home screen will show list of preferred Pet Sitters, Favorites
* [ ] Pet owner can delete pets


## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/vEg3EYh.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

Link: http://i.imgur.com/vEg3EYh.gif

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

## Open-source libraries used

- [Java Client for Google Maps API](https://github.com/googlemaps/google-maps-services-java) - This library brings the Google Maps API Web Services to your Java application.
- [Parse SDK for Android](https://github.com/ParsePlatform/Parse-SDK-Android) - A library that gives you access to the powerful Parse cloud platform from your Android app.
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android

## License


    Copyright 2016 Aoi Matsuba, Eduardo Przysiezny, Mugdha Khade


    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at


        http://www.apache.org/licenses/LICENSE-2.0


    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License