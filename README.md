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
</resources>
```

**Activities overview:**
1. Login activity: User/Password authentication using Parse
2. Sign-up activity: Allows users to create a user for the application
3. Home Screen activity: Stream with recent activities, searches, ...
4. Search filter activity: Take parameters "when" "where" and "pet type"(predefined drop down).
5. Search results
    1. Search Results Map: shows a map with sitters available accordingly to the search filter/criteria
    2. Search Results List: shows a list of sitters with profile image, services accordingly to the search filter/criteria
6. Pet Sitter detail
	1. Sitter profile information
	2. Services provided
	3. Schedule/Availability
	4. Owner can request service from sitter
	
 
Time spent: **** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **sign up** with a username/password against Parse
* [x] User can **login** with a username/password against Parse
  * [x] User credentials are cached for subsequent logins
* [x] User can edit his **profile** information
  * [x] User can add a **profile image** to his profile
  * [x] User can configure his profile with full name, location/address, contact information (phone, email, etc)
* [x] Pet owner can add/modify **pets** under its profile
  * [x] Pet owner can add a **pet profile image** to the pet profile information
  * [x] Pet owner can add pet type (dog/cat/etc), name, picture, breed, emergency contact, special needs, etc
  * [ ] Pet owner can delete pets
* [x] User can mark himself as a Pet Sitter
  * [ ] Pet sitter can configure his profile with specialties (dogs, cats, etc), services provided
  * [ ] Pet sitter can add **photos** of his pet sitting spaces
* [x] After sign-up or authentication user will be directed to the home screen
  * [ ] Home screen will contain a Navigation Drawer
  * [ ] Home screen will show list of preferred Pet Sitters
* [ ] User can search applying a filter and get results 
  * [ ] Results will show map with pet sitters that match the search filter
  * [ ] Results will show a list with pet sitters that match the search filter (profile, name, stars, likes count, services, ...)
  * [ ] Results will allow the user to view the pet sitter details
  * [ ] User can use **pull down to refresh ** the results
* [ ] Owner can **request a sitter** from the pet sitter details page 
  * [ ] User can click a “Contact sitter”
* [ ] Sitter can accept a service request
* [ ] User can **open the app offline and see most recent persisted data**.

The following **additional** features are implemented:

* [ ] User can sign-in with **facebook credentials**
* [ ] User can **review** other users/pets
* [ ] User can view a list with detailed reviews about other users (sitters/owners)
* [ ] User can post a general request for any Pet Sitters in case he doesn't find a sitter with his search criteria
* [ ] Users can exchange direct messages
* [ ] User can see embedded image media within any direct messages
* [ ] When a user leaves the contact view without sending the message and there is existing text, prompt to save or delete the draft.  The draft can be resumed from the compose view.

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/2OxSEhv.gifv' title='Video Walkthrough' width='' alt='Video Walkthrough' />

Link: http://i.imgur.com/2OxSEhv.gifv

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

## Open-source libraries used

- [Parse SDK for Android](https://github.com/ParsePlatform/Parse-SDK-Android) - A library that gives you access to the powerful Parse cloud platform from your Android app.
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android

## License


    Copyright 2016 Aoi Masuba, Eduardo Przysiezny, Mugdha Khade


    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at


        http://www.apache.org/licenses/LICENSE-2.0


    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License
