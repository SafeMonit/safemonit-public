
One of the best features of SafeMonit is it's built in __Intrusion Monitoring__ system. This works like to your standard alarm systems that you're familiar with, but using your SmartThings components. Setup is easy, just follow the steps below

## Disable SmartThings Smart Home Monitor

First, it is recommended that you disable __Smart Home Monitor__ if you have it currently configured within SmartThings. For our intrusion monitoring, we only use Location Modes to simplify and add more customization around the monitoring experience. You can disable __Smart Home Monitor__ by removing any actions tied to specific sensors in the configuration panel accessible from the SmartThings mobile app dashboard.

## Add SmartThings Location Modes

SmartThings allows you to set a __Mode__ for your location which specifies the current "state" of your system. Your location usually comes with a few default modes, usually __Home__, __Away__, and __Night__.

To make the Intrusion Monitoring feature work, we need a few more modes. Luckily, this is easy to configure.

1. First, you will want to navigate your web browser to <https://graph.api.smartthings.com>.

2. From there, click __My Locations__ in the top header, and then click the name of the Location you want to integrate with SafeMonit.

3. Next, on this Location Details page, under the __Modes__ section, click the __create new__ link. Using this form, add __Intrusion Alarm__ and __Intrusion Alarm Pending__ modes. You should now have the following modes:

    | Modes |
    |-------|
    | __Home__ |
    | __Away__ |
    | __Night__ |
    | __Intrusion Alarm__ |
    | __Intrusion Alarm Pending__ |
    | __Open__ (if business) |
    | __Closed__ (if business) |

4. That's it! Now you're all set to enable Intrusion Monitoring.

### Why do I need these modes?

**Intrusion Alarm Pending** mode is entered if your location is set to __Away__ or __Night__ and one of the configured sensors is triggered. It gives you approximately 20 seconds to switch to the __Home__ mode before the system switches to __Intrusion Alarm__ mode.

Once __Intrusion Alarm__ mode is entered. Any sirens are activated, and if SafeMonit Monitoring is enabled, a new __Incident__ is created.


