# How To Configure SafeMonit Intrusion Monitoring

One of the best features of SafeMonit is it's built in __Intrusion Monitoring__ system. This functions similarly to your standard alarm systems that you're familiar with, but using your SmartThings components. Setup is easy, first start with adding the necessary __Location Modes__.

SmartThings allows you to set a __Mode__ for your location which specifies the current "state" of your system. Your location usually comes with a few default modes, usually __Home__, __Away__, and __Night__.

## Add SmartThings Location Modes

To make the Intrusion Monitoring feature work, we need a few more modes. Luckily, this is easy to configure.

1. First, you will want to navigate your web browser to <https://graph.api.smartthings.com>.

2. From there, click __My Locations__ in the top header, and then click the name of the Location you want to integrate with SafeMonit.

3. Next, on this Location Details page, under the __Modes__ section, click the __create new__ link. Using this form, add __Police Alarm__ and __Police Alarm Pending__ modes.

4. Make sure you have the following modes now: __Home__, __Away__, __Night__, __Police Alarm__, and __Police Alarm Pending__. If not, add any that are missing. That's it! Now you're all set to enable Intrusion Monitoring.

### Why do I need these modes?

**Police Alarm Pending** mode is entered if your location is set to __Away__ or __Night__ and one of the configured sensors is triggered. It gives you approximately 20 seconds to switch to the __Home__ mode before the system switches to **Police Alarm** mode. Once this mode is entered. Any sirens are activated, and if SafeMonit Monitoring is enabled, a new __Incident__ is created.


