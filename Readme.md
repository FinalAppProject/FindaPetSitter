# *Find a PetSitter*


**Find a PetSitter** is an android app that allows a user to find pet sitter around his area who can take care of his pets whom he can find based on location and dates. Any kind of pet (legal) can find a pet sitter through here. A user can be a pet sitter and a pet owber both.


Activities overview:
1. Login activity: login [Take in name, and zip code](additional: login with fb)
2. Search activity: Take parameters "when" "where" and "pet type"(predefined drop down).  
3. Petsitter info activity: There will be "Host a pet" button on the search activity which will take us to this page. Details such as "About me, location(not exact of course), my pets, profile picture, "pets I am experienced with".
4. Profile activity: Shows picture, ratings, number of reviews. Tabs with "Info", "Review details"
5. Payment activity: (do you want to make this a part of optionals?)


Time spent: **** hours spent in total


## User Stories


The following **required** functionality is completed:


* [ ] User can **sign in to the app** using OAuth login
* [ ] User can configure a profile
 * [ ] Pet owner can configure his profile with full name, location/address, contact information (phone, email, etc) and at least one pet
 * [ ] Pet owner can add pets to his profile type (dog/cat/etc), name, picture, breed, emergency contact, special needs, etc
 * [ ] Pet sitter can configure his profile full name, specialties (dogs, cats, etc), services provided
* [ ] User can search applying a filter and get results 
  * [ ] Results will Picture, name, contain ratings, reviews count.
* [ ] User can view petsitters profiles from the search results by clicking on the result
* [ ] User can **Contact a sitter** from the search results that he likes. 
  * [ ] User can click a “Contact sitter”
  * [ ] User can send a note along with the filter criteria (fragment)
  * [ ] User has a history of messages has sent and received and it appears in that.
* [ ] User can use **pull down to refresh **
* [ ] User can **open the app offline and see last loaded ......**. Persisted in SQLite data is refreshed on every application launch. 
* [ ] Improve the user interface
* [ ] All separate functionalities are build using modal overlay

The following **optional** features are implemented:

* [ ] User can sign-in with 3rd party OAuth providers
 * [ ] User can login with fb
 * [ ] User can login with google
* [ ] User can tap a review to **open a detailed review view**
* [ ] User can see embedded image media within any direct messages
* [ ] When a user leaves the contact view without sending the message and there is existing text, prompt to save or delete the draft.  The draft can be resumed from the compose view.




The following **bonus** features are implemented:




The following **additional** features are implemented:


* [ ] List anything else that you can get done to improve the app functionality!


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
