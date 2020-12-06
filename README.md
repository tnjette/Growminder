# Growminder

Growminder application built for Android. All data is internal, thereby eliminating any network dependencies. Built on a SQLite instance, with all of the usual Android stuff, including Notifications, which at this time, may be obsolete since it seems like Android Notifications have undergone some changes since the last development iteration. In other words, the Notification system may not function as it should. 

Features: 

Growminder configures to crop variety and user location specifications. Once the user configures their location, the system refers to the internal database, and retrieves the growing information that is relevant to the varieties that are capable of being grown in that location. This is based on USDA Agricultural zones across the US. The database is organized per agricultural zone, per variety, and therefore the information made available to the user is completely dependent on the location they enter into the system. Subsequently, based on the user's growing interests, the system provides information per crop variety that is configured to that specific agricultural zone. 
    
Once configured, the system compiles and stores formatted Notifications per variety, each of which are refered to by the notification system, by date, to be sent to the user's device when relevant. 
    
The system allows for the user to explore the array of crop varieties in their configured agricultural zone, including calendars, growing cycle visualizations, and crop demands that may change throughout each growing cycle.

<img src = "screenshots/growminder_screenshot1.jpg" width = "400"> 
<img src = "screenshots/growminder_screenshot2.jpg" width = "400">
<img src = "screenshots/growminder_screenshot3.jpg" width = "400">
<img src = "screenshots/growminder_screenshot4.jpg" width = "400">

# Future iterations : 

Needs to be built with cross-platform framework, such as React Native or Flutter, to increase user base. Notification system needs to be updated.
And, perhaps most importantly, needs to be built with thorough testing throughout the ENTIRE development timeline, and on a wide range of devices, not just your 5 year old Android phone, hard-wired to your laptop, Tyler.

