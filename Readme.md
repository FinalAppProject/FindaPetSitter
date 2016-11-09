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
1. Login activity: 3rd party OAuth authentication

2. Sign-up activity: Allows users to create a profile as pet owner, sitter or both

3. Home Screen activity: Stream with recent activities, searches, ...

4. Search filter activity: Take parameters "when" "where" and "pet type"(predefined drop down).  

5. Search results

 5.1. Search Results Map: shows a map with sitters available accordingly to the search filter/criteria
 
 5.2. Search Results List: shows a list of sitters with profile image, services accordingly to the search filter/criteria
 
6. Pet Sitter detail

	6.1 Sitter profile information
	
	6.2 Services provided
	
	6.3 Schedule/Availability
	
	6.4. Owner can request service from sitter
	
 
Time spent: **** hours spent in total

## User Stories

The following **required** functionality is completed:

* [ ] User can **sign in to the app** using 3rd party OAuth authentication providers
* [ ] User must configure a profile
 * [ ] Pet owner can configure his profile with full name, location/address, contact information (phone, email, etc) and at least one pet
 * [ ] Pet owner can add pets to his profile type (dog/cat/etc), name, picture, breed, emergency contact, special needs, etc
 * [ ] Pet sitter can configure his profile full name, specialties (dogs, cats, etc), services provided
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

* [ ] User can sign-in with email/password
* [ ] User can tap a review to **open a detailed review view**
* [ ] User can view a list with detailed reviews about other users (sitters/owners)
* [ ] User can post a general request for any Pet Sitters in case he doesn't find a sitter with his search criterea
* [ ] User can see embedded image media within any direct messages
* [ ] When a user leaves the contact view without sending the message and there is existing text, prompt to save or delete the draft.  The draft can be resumed from the compose view.

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/2OxSEhv.gifv' title='Video Walkthrough' width='' alt='Video Walkthrough' />

Link: http://i.imgur.com/2OxSEhv.gifv

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

## Open-source libraries used

## License


    Copyright 2016 [name of copyright owner]


    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at


        http://www.apache.org/licenses/LICENSE-2.0


    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License
